package com.etpserver.carpetetpaddition;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CarpetETP implements ModInitializer {
    public static final String MOD_ID = "carpet-etp-addition";
    public static final String MOD_NAME = "Carpet ETP Addition";
    private static String VERSION;

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    @Override
    public void onInitialize() {
        VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();
        CarpetETPServer.init();
    }

    public static String getVersion() {
        return VERSION;
    }
}
