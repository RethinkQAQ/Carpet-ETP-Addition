/*
 * This file is part of the CarpetETPAddition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Rethink_QAQ and contributors
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

package com.etpserver.carpetetpaddition.utils.command.checkslot;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.jetbrains.annotations.Nullable;

public class ChestContainerProvider implements ContainerProvider{

    public static final ChestContainerProvider INSTANCE = new ChestContainerProvider();

    private ChestContainerProvider() {

    }

    @Override
    public @Nullable Container getContainer(ServerLevel level, BlockPos pos, BlockEntity blockEntity) {
        if (!(blockEntity instanceof ChestBlockEntity chest)) {
            return null;
        }

        if (!(chest.getBlockState().getBlock() instanceof ChestBlock chestBlock)) {
            return null;
        }

        return ChestBlock.getContainer(
                chestBlock,
                chest.getBlockState(),
                level,
                chest.getBlockPos(),
                true
        );
    }
}
