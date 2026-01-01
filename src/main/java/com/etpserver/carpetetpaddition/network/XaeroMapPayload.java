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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.zip.CRC32;


public class XaeroMapPayload<T extends XaeroMapPayload<T>> implements CustomPacketPayload {

    private final ResourceLocation id;

    public static final StreamCodec<FriendlyByteBuf, XaeroMapPayload<?>> CODEC = StreamCodec.ofMember(XaeroMapPayload::write, XaeroMapPayload::new);

    public XaeroMapPayload(ResourceLocation id) {
        this.id = id;
    }

    public XaeroMapPayload(FriendlyByteBuf buf) {
        this(buf.readResourceLocation());
    }

    public void write(FriendlyByteBuf buf) {
        CRC32 crc32 = new CRC32();
        byte[] data = CarpetETPSettings.xaeroMapName.getBytes();
        crc32.update(data, 0, data.length);
        buf.writeByte(0);
        buf.writeInt((int) crc32.getValue());
    }


    @Override
    public Type<? extends XaeroMapPayload<T>> type() {
        return new CustomPacketPayload.Type<>(id);
    }
}
