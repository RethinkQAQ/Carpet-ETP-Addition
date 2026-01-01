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

import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
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
            //#if MC >= 12109
            //$$ method = "startRiding(Lnet/minecraft/world/entity/Entity;ZZ)Z",
            //#else
            method = "startRiding(Lnet/minecraft/world/entity/Entity;Z)Z",
            //#endif
            at = @At(
                    value = "RETURN"
            )
    )
    private void sendRidePacket(Entity entity, boolean force,
                                //#if MC >= 12109
                                //$$ boolean emitEvent,
                                //#endif
                                CallbackInfoReturnable<Boolean> cir)
    {
        if (this.vehicle != null && this.vehicle instanceof ServerPlayer &&!this.vehicle.level().isClientSide()) {
                ((ServerPlayer) vehicle).connection.send(new ClientboundSetPassengersPacket(vehicle));
        }
    }

    @SuppressWarnings("UnreachableCode")
    @Inject(
            method = "removePassenger",
            at = @At(
                    value = "RETURN")
    )
    private void sendDismountPacket(Entity passenger, CallbackInfo ci) {
        if ((Entity)(Object)this instanceof ServerPlayer player){
            player.connection.send(new ClientboundSetPassengersPacket(player));
        }
    }
}
