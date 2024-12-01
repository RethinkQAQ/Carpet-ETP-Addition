package com.etpserver.carpetetpaddition;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;

import java.util.logging.Logger;

public class CarpetETPClient implements ClientModInitializer {
    private static final CarpetETPClient INSTANCE = new CarpetETPClient();
    public static final Logger LOGGER = Logger.getLogger(CarpetETP.MOD_NAME);

    public static CarpetETPClient getInstance() {
        return INSTANCE;
    }

    public void onClientTick(MinecraftClient client) {
    }

    @Override
    public void onInitializeClient() {

    }
}
