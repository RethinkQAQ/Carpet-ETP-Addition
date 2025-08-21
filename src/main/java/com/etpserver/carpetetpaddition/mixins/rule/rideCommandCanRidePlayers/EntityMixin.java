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

package com.etpserver.carpetetpaddition.mixins.rule.rideCommandCanRidePlayers;

import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin{

    @Shadow
    @Nullable
    private Entity vehicle;

    @Inject(
            method = "startRiding(Lnet/minecraft/entity/Entity;Z)Z",
            at = @At(
                    value = "RETURN"
            )
    )
    private void sendRidePacket(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (this.vehicle != null && this.vehicle instanceof ServerPlayerEntity &&!this.vehicle.getWorld().isClient) {
                ((ServerPlayerEntity) vehicle).networkHandler.sendPacket(new EntityPassengersSetS2CPacket(vehicle));
        }
    }

    @SuppressWarnings("UnreachableCode")
    @Inject(
            method = "removePassenger",
            at = @At(
                    value = "RETURN")
    )
    private void sendDismountPacket(Entity passenger, CallbackInfo ci) {
        if ((Entity)(Object)this instanceof ServerPlayerEntity player){
            player.networkHandler.sendPacket(new EntityPassengersSetS2CPacket(player));
        }
    }
}
