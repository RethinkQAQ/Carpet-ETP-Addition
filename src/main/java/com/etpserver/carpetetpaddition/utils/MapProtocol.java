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

package com.etpserver.carpetetpaddition.utils;

import com.etpserver.carpetetpaddition.network.WorldInfoPayload;
import com.etpserver.carpetetpaddition.network.XaeroMapPayload;
import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class MapProtocol {
    public static final String XAERO_WORLD_MAP = "xaeroworldmap";
    public static final String XAERO_MINI_MAP = "xaerominimap";

    public static final ResourceLocation WORLD_KEY = idWorld("main");
    public static final ResourceLocation MINI_KEY = idMini("main");

    @Contract("_ -> new")
    public static @NotNull ResourceLocation idWorld(String path) {
        return ResourceLocation.fromNamespaceAndPath(XAERO_WORLD_MAP, path);
    }

    @Contract("_ -> new")
    public static @NotNull ResourceLocation idMini(String path) {
        return ResourceLocation.fromNamespaceAndPath(XAERO_MINI_MAP, path);
    }

    public static void onSendWorldInfo(@NotNull ServerPlayer player) {
        ServerPlayNetworking.send(player, new XaeroMapPayload<>(WORLD_KEY));
        ServerPlayNetworking.send(player, new XaeroMapPayload<>(MINI_KEY));
        ServerPlayNetworking.send(player, new WorldInfoPayload<>(CarpetETPSettings.xaeroMapName));
    }
}
