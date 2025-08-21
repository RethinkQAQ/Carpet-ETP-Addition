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

import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.zip.CRC32;


public class XaeroMapPayload<T extends XaeroMapPayload<T>> implements CustomPayload {

    private final Identifier id;

    public static final PacketCodec<PacketByteBuf, XaeroMapPayload<?>> CODEC = PacketCodec.of(XaeroMapPayload::write, XaeroMapPayload::new);

    public XaeroMapPayload(Identifier id) {
        this.id = id;
    }

    public XaeroMapPayload(PacketByteBuf buf) {
        this(buf.readIdentifier());
    }

    public void write(PacketByteBuf buf) {
        CRC32 crc32 = new CRC32();
        byte[] data = CarpetETPSettings.xaeroMapName.getBytes();
        crc32.update(data, 0, data.length);
        buf.writeByte(0);
        buf.writeInt((int) crc32.getValue());
    }


    @Override
    public Id<? extends XaeroMapPayload<T>> getId() {
        return new CustomPayload.Id<>(id);
    }
}
