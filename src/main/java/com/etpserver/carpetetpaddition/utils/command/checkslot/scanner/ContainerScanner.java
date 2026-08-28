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

package com.etpserver.carpetetpaddition.utils.command.checkslot.scanner;

import com.etpserver.carpetetpaddition.utils.command.checkslot.ContainerRegistry;
import com.etpserver.carpetetpaddition.utils.command.checkslot.ContainerType;
import com.etpserver.carpetetpaddition.utils.command.checkslot.SearchRequest;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public final class ContainerScanner {

    private ContainerScanner() {

    }

    /**
     * @param scannedCallback 每扫描到一个请求类型的容器时回调(用于生成白色闪光高光)
     */
    public static SearchResult scan(ServerLevel level, BlockPos center, SearchRequest request, IntList slots,
                                    Consumer<BlockPos> scannedCallback) {
        long startNanos = System.nanoTime();
        SearchResult result = new SearchResult();

        ContainerType type = request.containerType();
        int radius = request.radius();
        // 双箱的两个半边都会被扫到,记录已处理的方块避免重复
        Set<BlockPos> visitedHalves = new HashSet<>();
        int scanned = 0;

        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-radius, -radius, -radius),
                center.offset(radius, radius, radius))) {

            if (!level.isLoaded(pos)) {
                continue;
            }

            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity == null) {
                continue;
            }

            ContainerType found = ContainerRegistry.byBlockEntity(blockEntity.getType());
            if (found != type) {
                continue;
            }

            Container container = type.provider().getContainer(level, pos, blockEntity);
            if (container == null) {
                continue;
            }

            scannedCallback.accept(pos);

            if (container instanceof CompoundContainer compound) {
                if (visitedHalves.contains(pos)) {
                    continue;
                }
                scanCompoundContainer(result, compound, slots, visitedHalves);
            } else {
                scanSingleContainer(result, pos, container, slots);
            }
            scanned++;
        }

        result.setScannedContainers(scanned);
        result.setElapsedTime((System.nanoTime() - startNanos) / 1_000_000.0);
        return result;
    }

    private static void scanSingleContainer(SearchResult result, BlockPos pos, Container container, IntList slots) {
        IntList matched = new IntArrayList();
        for (int i = 0; i < slots.size(); i++) {
            int slot = slots.getInt(i);
            // 箱子定义大小为 54(双箱编号),单箱实际只有 27 格,越界部分直接跳过
            if (slot >= container.getContainerSize()) {
                continue;
            }
            if (!container.getItem(slot).isEmpty()) {
                matched.add(slot);
            }
        }

        if (!matched.isEmpty()) {
            result.addHit(new SearchHit(pos.immutable(), matched));
        }
    }

    private static void scanCompoundContainer(SearchResult result, CompoundContainer compound, IntList slots,
                                              Set<BlockPos> visitedHalves) {
        BlockPos firstPos = containerPos(compound.container1);
        BlockPos secondPos = containerPos(compound.container2);
        visitedHalves.add(firstPos);
        visitedHalves.add(secondPos);

        int firstSize = compound.container1.getContainerSize();
        int totalSize = firstSize + compound.container2.getContainerSize();
        IntList firstSlots = new IntArrayList();
        IntList secondSlots = new IntArrayList();
        for (int i = 0; i < slots.size(); i++) {
            int slot = slots.getInt(i);
            if (slot >= totalSize) {
                continue;
            }
            if (compound.getItem(slot).isEmpty()) {
                continue;
            }
            if (slot < firstSize) {
                firstSlots.add(slot);
            } else {
                secondSlots.add(slot);
            }
        }

        if (!firstSlots.isEmpty() && firstPos != null) {
            result.addHit(new SearchHit(firstPos, firstSlots));
        }
        if (!secondSlots.isEmpty() && secondPos != null) {
            result.addHit(new SearchHit(secondPos, secondSlots));
        }
    }

    @Nullable
    private static BlockPos containerPos(Container container) {
        if (container instanceof BlockEntity blockEntity) {
            return blockEntity.getBlockPos();
        }
        return null;
    }
}
