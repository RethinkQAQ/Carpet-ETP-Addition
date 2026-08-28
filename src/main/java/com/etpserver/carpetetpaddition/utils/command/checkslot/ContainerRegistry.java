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

import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.*;

public final class ContainerRegistry {

    private static final Map<String, ContainerType> BY_ID = new LinkedHashMap<>();

    private static final Map<BlockEntityType<?>, ContainerType> BY_BLOCK_ENTITY = new IdentityHashMap<>();

    private static boolean bootstrapped = false;

    private ContainerRegistry() {

    }

    public static void bootstrap() {
        if (bootstrapped) {
            return;
        }

        bootstrapped = true;

        registerContainers();
    }

    public static ContainerType byId(String id) {
        return BY_ID.get(id.toLowerCase());
    }

    public static ContainerType byBlockEntity(BlockEntityType<?> type) {
        return BY_BLOCK_ENTITY.get(type);
    }

    public static Collection<ContainerType> values() {
        return Collections.unmodifiableCollection(BY_ID.values());
    }

    private static void register(String id, BlockEntityType<?> blockEntityType, ContainerDefinition definition, ContainerProvider provider) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(blockEntityType);
        Objects.requireNonNull(definition);
        Objects.requireNonNull(provider);

        ContainerType type = new ContainerType(id, definition, provider);

        if (BY_ID.putIfAbsent(id, type) != null) {
            throw new IllegalStateException("Duplicate container id '" + id + "'");
        }

        if (BY_BLOCK_ENTITY.putIfAbsent(blockEntityType, type) != null) {
            throw new IllegalStateException("Duplicate container block entity id '" + id + "'");
        }
    }

    private static void registerContainers() {

        register("chest", BlockEntityType.CHEST, Definitions.CHEST_COMBINED, ChestContainerProvider.INSTANCE);

        register("trapped_chest", BlockEntityType.TRAPPED_CHEST, Definitions.CHEST_COMBINED, ChestContainerProvider.INSTANCE);

        register("barrel", BlockEntityType.BARREL, Definitions.CHEST, DefaultContainerProvider.INSTANCE);

        register("furnace", BlockEntityType.FURNACE, Definitions.FURNACE, DefaultContainerProvider.INSTANCE);

        register("blast_furnace", BlockEntityType.BLAST_FURNACE, Definitions.FURNACE, DefaultContainerProvider.INSTANCE);

        register("smoker", BlockEntityType.SMOKER, Definitions.FURNACE, DefaultContainerProvider.INSTANCE);

        register("hopper", BlockEntityType.HOPPER, Definitions.HOPPER, DefaultContainerProvider.INSTANCE);

        register("brewing_stand", BlockEntityType.BREWING_STAND, Definitions.BREWING_STAND, DefaultContainerProvider.INSTANCE);

        register("crafter", BlockEntityType.CRAFTER, Definitions.CRAFTER, DefaultContainerProvider.INSTANCE);

        register("dispenser", BlockEntityType.DISPENSER, Definitions.DISPENSER, DefaultContainerProvider.INSTANCE);

        register("dropper", BlockEntityType.DROPPER, Definitions.DISPENSER, DefaultContainerProvider.INSTANCE);

        register("shulker_box", BlockEntityType.SHULKER_BOX, Definitions.CHEST, DefaultContainerProvider.INSTANCE);

        register("chiseled_bookshelf", BlockEntityType.CHISELED_BOOKSHELF, Definitions.CHISELED_BOOKSHELF, DefaultContainerProvider.INSTANCE);

        register("decorated_pot", BlockEntityType.DECORATED_POT, Definitions.DECORATED_POT, DefaultContainerProvider.INSTANCE);

    }

    private static final class Definitions {
        // 普通单格容器沿用 27;箱子/陷阱箱用 54,与双箱合并后的 GUI 槽位编号保持一致
        private static final ContainerDefinition CHEST = ContainerDefinition.builder(27).build();

        private static final ContainerDefinition CHEST_COMBINED = ContainerDefinition.builder(54).build();

        private static final ContainerDefinition FURNACE = ContainerDefinition
                .builder(3)
                .alias("input", 0)
                .alias("fuel", 1)
                .alias("output", 2)
                .build();

        private static final ContainerDefinition HOPPER = ContainerDefinition.builder(5).build();

        private static final ContainerDefinition BREWING_STAND = ContainerDefinition
                .builder(5)
                .alias("left", 0)
                .alias("middle", 1)
                .alias("right", 2)
                .alias("ingredient", 3)
                .alias("fuel", 4)
                .build();

        private static final ContainerDefinition CRAFTER = ContainerDefinition.builder(9).build();

        private static final ContainerDefinition DISPENSER = ContainerDefinition.builder(9).build();

        private static final ContainerDefinition CHISELED_BOOKSHELF = ContainerDefinition.builder(6).build();

        private static final ContainerDefinition DECORATED_POT = ContainerDefinition.builder(1).build();

        private Definitions() {
        }
    }
}
