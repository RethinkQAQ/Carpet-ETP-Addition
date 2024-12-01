package com.etpserver.carpetetpaddition.network;

import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;


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
        buf.writeByte(0);
        buf.writeInt(CarpetETPSettings.xaeroWorldMapID);
    }


    @Override
    public Id<? extends XaeroMapPayload<T>> getId() {
        return new CustomPayload.Id<>(id);
    }
}
