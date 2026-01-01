/*
 * This file is part of the CarpetETPAddition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Rethink_QAQ and contributors
 *
 * CarpetETPAddition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CarpetETPAddition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CarpetETPAddition.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.etpserver.carpetetpaddition.mixins.rule.pearlTickets;

import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.concurrent.ExecutionException;

// 从PearlTicket 移植
@Mixin(ThrownEnderpearl.class)
public abstract class EnderPearlEntityMixin extends ThrowableItemProjectile {

    @Unique
    private static final TicketType<ChunkPos> ENDER_PEARL_TICKET =
            TicketType.create("ender_pearl", Comparator.comparingLong(ChunkPos::toLong), 2);

    @Unique
    private boolean sync = true;
    @Unique
    private Vec3 realPos = null;
    @Unique
    private Vec3 realVelocity = null;

    protected EnderPearlEntityMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
        super(entityType, world);
    }

    @Unique
    private static boolean isEntityTickingChunk(LevelChunk chunk) {
        return (chunk != null && chunk.getFullStatus() == FullChunkStatus.ENTITY_TICKING);
    }

    @Unique
    private static int getHighestMotionBlockingY(CompoundTag nbtCompound) {
        int highestY = Integer.MIN_VALUE;
        if (nbtCompound != null) {
            for (long element : nbtCompound.getCompound("Heightmaps").getLongArray("MOTION_BLOCKING")) {
                for (int i = 0; i < 7; i++) {
                    int y = (int) (element & 0b111111111) - 1;
                    if (y > highestY) highestY = y;
                    element = element >> 9;
                }
            }
        }
        return highestY;
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void skippyChunkLoading(CallbackInfo ci) {
        if (CarpetETPSettings.pearlTickets){
            Level world = this.getCommandSenderWorld();

            if (world instanceof ServerLevel) {
                Vec3 currPos = this.position().add(Vec3.ZERO);
                Vec3 currVelocity = this.getDeltaMovement().add(Vec3.ZERO);

                if (this.sync) {
                    this.realPos = currPos;
                    this.realVelocity = currVelocity;
                }

                // next pos
                Vec3 nextPos = this.realPos.add(this.realVelocity);
                Vec3 nextVelocity = this.realVelocity.scale(0.99F).subtract(0, this.getGravity(), 0);

                ChunkPos currChunkPos = new ChunkPos(new BlockPos((int) currPos.x, (int) currPos.y, (int) currPos.z));
                ChunkPos nextChunkPos = new ChunkPos(new BlockPos((int) nextPos.x, (int) nextPos.y, (int) nextPos.z));

                ServerChunkCache serverChunkManager = ((ServerLevel) world).getChunkSource();

                if (!this.sync || !isEntityTickingChunk(serverChunkManager.getChunkNow(nextChunkPos.x, nextChunkPos.z))) {
                    CompoundTag nbtCompound1;
                    CompoundTag nbtCompound2;
                    try {
                        nbtCompound1 = serverChunkManager.chunkMap.read(currChunkPos).get().orElse(null);
                        nbtCompound2 = serverChunkManager.chunkMap.read(nextChunkPos).get().orElse(null);
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException("NbtCompound Exception");
                    }

                    int highestMotionBlockingY = Integer.max(getHighestMotionBlockingY(nbtCompound1), getHighestMotionBlockingY(nbtCompound2));

                    // compatible with none-zero minimum y value dimension
                    DimensionType worldDimensionType = world.dimensionType();
                    highestMotionBlockingY += worldDimensionType.minY();

                    // skip chunk loading
                    if (this.realPos.y > highestMotionBlockingY && nextPos.y > highestMotionBlockingY && nextPos.y + nextVelocity.y > highestMotionBlockingY) {
                        // /stay put
                        serverChunkManager.addRegionTicket(ENDER_PEARL_TICKET, currChunkPos, 2, currChunkPos);
                        this.setDeltaMovement(Vec3.ZERO);
                        this.setPos(currPos);
                        this.sync = false;
                    } else {
                        // move
                        serverChunkManager.addRegionTicket(ENDER_PEARL_TICKET, nextChunkPos, 2, nextChunkPos);
                        this.setDeltaMovement(this.realVelocity);
                        this.setPos(this.realPos);
                        this.sync = true;
                    }
                }

                // update real pos and velocity
                this.realPos = nextPos;
                this.realVelocity = nextVelocity;
            }
        }
    }
}