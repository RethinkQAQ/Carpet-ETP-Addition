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

package com.etpserver.carpetetpaddition.mixins.rule.testMode;

import carpet.patches.EntityPlayerMPFake;
import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServePlayerEntityMixin extends PlayerEntity {

    public ServePlayerEntityMixin(World world,
                                  //#if MC < 12108
                                  BlockPos pos, float yaw,
                                  //#endif
                                  GameProfile gameProfile) {
        super(world,
                //#if MC < 12108
                pos, yaw,
                //#endif
                gameProfile);
    }

    @Shadow public abstract boolean changeGameMode(GameMode gameMode);

    //#if MC < 12105
    @Shadow public abstract boolean isSpectator();
    //#endif


    @SuppressWarnings("all")
    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void changeToSpectator(CallbackInfo ci) {
        if (CarpetETPSettings.testMode && !this.isSpectator() && !((ServerPlayerEntity)(Object)this instanceof EntityPlayerMPFake)) {
            this.changeGameMode(GameMode.SPECTATOR);
        }
    }
}
