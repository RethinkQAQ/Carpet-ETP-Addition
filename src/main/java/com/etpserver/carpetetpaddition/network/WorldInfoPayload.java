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

package com.etpserver.carpetetpaddition.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class WorldInfoPayload<T extends WorldInfoPayload<T>> implements CustomPacketPayload {

    public final String worldID;

    public static final ResourceLocation WORLD_INFO_PACKET_ID = ResourceLocation.parse("worldinfo:world_id");

    public static final StreamCodec<FriendlyByteBuf, WorldInfoPayload<?>> CODEC = StreamCodec.ofMember(WorldInfoPayload::write, WorldInfoPayload::new);

    public WorldInfoPayload(String worldID) {
        this.worldID = worldID;
    }

    public WorldInfoPayload(FriendlyByteBuf buf) {
        this.worldID = buf.readUtf(32767);
    }

    private void write(FriendlyByteBuf buf) {
        if (worldID != null) {
            buf.writeByte(0);
            buf.writeByte(42);
            buf.writeByte(worldID.length());
            buf.writeBytes(worldID.getBytes());
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return new Type<>(WORLD_INFO_PACKET_ID);
    }
}
