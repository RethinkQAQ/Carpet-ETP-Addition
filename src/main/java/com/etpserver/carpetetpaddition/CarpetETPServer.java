package com.etpserver.carpetetpaddition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.etpserver.carpetetpaddition.network.XaeroMapPayload;
import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import com.etpserver.carpetetpaddition.utils.CarpetETPAdditionTranslations;
import com.etpserver.carpetetpaddition.utils.XaeroMapProtocol;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;

import org.apache.logging.log4j.Logger;

import java.util.Map;

public class CarpetETPServer implements CarpetExtension{
    private static final CarpetETPServer INSTANCE = new CarpetETPServer();
    public static final String compactName = CarpetETP.MOD_ID;
    public static final Logger LOGGER = CarpetETP.LOGGER;
    public static MinecraftServer minecraftServer;

    public static void loadExtension() {
        CarpetServer.manageExtension(new CarpetETPServer());
    }

    @Override
    public String version() {
        return CarpetETP.MOD_ID;
    }

    public static CarpetETPServer getInstance() { return INSTANCE; }

    public static void init() {
        CarpetServer.manageExtension(new CarpetETPServer());
        PayloadTypeRegistry.playS2C().register(new CustomPayload.Id<>(XaeroMapProtocol.WORLD_KEY), XaeroMapPayload.CODEC);
    }

    @Override
    public void onGameStarted() {
        CarpetETP.LOGGER.info("Carpet-ETP-Addition started");
        CarpetServer.settingsManager.parseSettingsClass(CarpetETPSettings.class);
    }

    @Override
    public void onServerLoaded(MinecraftServer server){
        minecraftServer = server;
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return CarpetETPAdditionTranslations.getTranslationsFromResource(lang);
    }
}