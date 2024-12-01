package com.etpserver.carpetetpaddition.utils;

import com.etpserver.carpetetpaddition.network.XaeroMapPayload;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ProtocolUtil {
    @SuppressWarnings(("all"))
    public static void sendPayload(@NotNull ServerPlayerEntity player, Identifier id, Consumer<PacketByteBuf> consumer) {
//        player.networkHandler.sendPacket(new CustomPayloadS2CPacket(new XaeroMapPayload<>(consumer)));
    }
}
