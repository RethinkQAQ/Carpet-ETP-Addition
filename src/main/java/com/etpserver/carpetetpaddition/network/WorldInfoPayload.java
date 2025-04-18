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
