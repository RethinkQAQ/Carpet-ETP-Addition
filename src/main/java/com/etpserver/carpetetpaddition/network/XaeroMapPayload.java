package com.etpserver.carpetetpaddition.network;

import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
