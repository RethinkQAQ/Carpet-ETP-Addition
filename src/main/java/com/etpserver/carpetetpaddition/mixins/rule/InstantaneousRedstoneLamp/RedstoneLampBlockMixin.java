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

package com.etpserver.carpetetpaddition.mixins.rule.InstantaneousRedstoneLamp;

import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#if MC >= 12103
//$$ import net.minecraft.world.block.WireOrientation;
//#endif

import static net.minecraft.block.RedstoneLampBlock.LIT;

@Mixin(RedstoneLampBlock.class)
public class RedstoneLampBlockMixin {

    @Inject(
            method = "neighborUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"
        ),
            cancellable = true)
    private void onNeighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock,
                                  //#if MC >= 12103
                                  //$$ WireOrientation wireOrientation,
                                  //#elseif
                                  BlockPos sourcePos,
                                  //#endif
                                  boolean notify, CallbackInfo ci) {
        if(CarpetETPSettings.InstantaneousRedstoneLamp) {
            ci.cancel();
            world.setBlockState(pos, state.cycle(LIT),Block.NOTIFY_LISTENERS);
        }
    }
}
