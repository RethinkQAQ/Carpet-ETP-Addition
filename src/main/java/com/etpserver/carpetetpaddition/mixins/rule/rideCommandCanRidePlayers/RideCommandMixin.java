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
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.commands.RideCommand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RideCommand.class)
public class RideCommandMixin {
    //#if MC < 26.1
    @WrapOperation(
            method = "mount",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getType()Lnet/minecraft/world/entity/EntityType;"
            )
    )
    private static EntityType<?> canRidePlayers(Entity entity, Operation<EntityType<?>> original) {
        return CarpetETPSettings.rideCommandCanRidePlayers?  null: original.call(entity);
    }
    //#else
    //$$@WrapOperation(
    //$$        method = "mount",
    //$$        at = @At(
    //$$                value = "INVOKE",
    //$$                target = "Lnet/minecraft/world/entity/Entity;is(Ljava/lang/Object;)Z"
    //$$        )
    //$$)
    //$$private static boolean canRidePlayers(Entity instance, Object o, Operation<Boolean> original) {
    //$$    return !CarpetETPSettings.rideCommandCanRidePlayers && original.call(instance, o);
    //$$}
    //#endif
}
