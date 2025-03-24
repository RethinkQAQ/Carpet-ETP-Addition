package com.etpserver.carpetetpaddition.mixins.rule.pealTickets;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ChunkLevelType;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;

// 从PearlTicket 移植
@Mixin(EnderPearlEntity.class)
public abstract class EnderPearlEntityMixin extends ThrownItemEntity {

    private static final ChunkTicketType<ChunkPos> ENDER_PEARL_TICKET =
            ChunkTicketType.create("ender_pearl", Comparator.comparingLong(ChunkPos::toLong), 2);

    private boolean sync = true;
    private Vec3d realPos = null;
    private Vec3d realVelocity = null;

    protected EnderPearlEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    private static boolean isEntityTickingChunk(WorldChunk chunk) {
        return (chunk != null && chunk.getLevelType() == ChunkLevelType.ENTITY_TICKING);
    }

    private static int getHighestMotionBlockingY(NbtCompound nbtCompound) {
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
        World world = this.getEntityWorld();

        if (world instanceof ServerWorld) {
            Vec3d currPos = this.getPos().add(Vec3d.ZERO);
            Vec3d currVel = this.getVelocity().add(Vec3d.ZERO);
        }
    }
}