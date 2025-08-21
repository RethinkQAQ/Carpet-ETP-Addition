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

package com.etpserver.carpetetpaddition.mixins.rule.spectatorLeashBreak;

import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Leashable;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 12108
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif


@Mixin(Leashable.class)
public interface LeashableMixin {
    @Shadow void detachLeash();

    @Inject(
            method = "beforeLeashTick",
            at = @At("HEAD")
    )
    // Entity leashHolder, CallbackInfo ci
    default void beforeLeashTick(Entity leashHolder,
                                 //#if MC >= 12108
                                 //$$ CallbackInfo ci
                                 //#else
                                 float distance, CallbackInfoReturnable<Boolean> cir
                                 //#endif
    ) {
        if (CarpetETPSettings.spectatorLeashBreak) {
            if (leashHolder instanceof ServerPlayerEntity player && player.isSpectator()) {
                this.detachLeash();
            }
        }
    }
}
