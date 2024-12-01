package com.etpserver.carpetetpaddition.utils;

import com.etpserver.carpetetpaddition.network.XaeroMapPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class XaeroMapProtocol {
    public static final String XAERO_WORLD_MAP = "xaeroworldmap";
    public static final String XAERO_MINI_MAP = "xaerominimap";

    public static final Identifier WORLD_KEY = idWorld("main");
    public static final Identifier MINI_KEY = idMini("main");

    @Contract("_ -> new")
    public static @NotNull Identifier idWorld(String path) {
        return Identifier.of(XAERO_WORLD_MAP, path);
    }

    @Contract("_ -> new")
    public static @NotNull Identifier idMini(String path) {
        return Identifier.of(XAERO_MINI_MAP, path);
    }

    public static void onSendWorldInfo(@NotNull ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new XaeroMapPayload<>(WORLD_KEY));
    }
}
