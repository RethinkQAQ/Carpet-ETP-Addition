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

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class WorldInfoPayload<T extends WorldInfoPayload<T>> implements CustomPayload {

    public final String worldID;

    public static final Identifier WORLD_INFO_PACKET_ID = Identifier.of("worldinfo:world_id");

    public static final PacketCodec<PacketByteBuf, WorldInfoPayload<?>> CODEC = PacketCodec.of(WorldInfoPayload::write, WorldInfoPayload::new);

    public WorldInfoPayload(String worldID) {
        this.worldID = worldID;
    }

    public WorldInfoPayload(PacketByteBuf buf) {
        this.worldID = buf.readString(32767);
    }

    private void write(PacketByteBuf buf) {
        if (worldID != null) {
            buf.writeByte(0);
            buf.writeByte(42);
            buf.writeByte(worldID.length());
            buf.writeBytes(worldID.getBytes());
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return new Id<>(WORLD_INFO_PACKET_ID);
    }
}
