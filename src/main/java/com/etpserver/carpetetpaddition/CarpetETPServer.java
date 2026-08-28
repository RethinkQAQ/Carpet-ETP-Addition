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

package com.etpserver.carpetetpaddition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.etpserver.carpetetpaddition.commands.checkslot.CheckSlotCommand;
import com.etpserver.carpetetpaddition.commands.checkslot.HighlightManager;
import com.etpserver.carpetetpaddition.network.WorldInfoPayload;
import com.etpserver.carpetetpaddition.network.XaeroMapPayload;
import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import com.etpserver.carpetetpaddition.translations.CarpetETPAdditionTranslations;
import com.etpserver.carpetetpaddition.utils.MapProtocol;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
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
        CheckSlotCommand.register();
        // Register XaeroMap payload
        if (!FabricLoader.getInstance().isModLoaded("xaerominimap") && !FabricLoader.getInstance().isModLoaded("xaeroworldmap")) {
            PayloadTypeRegistry.playS2C().register(new CustomPacketPayload.Type<>(MapProtocol.WORLD_KEY), XaeroMapPayload.CODEC);
            PayloadTypeRegistry.playS2C().register(new CustomPacketPayload.Type<>(MapProtocol.MINI_KEY), XaeroMapPayload.CODEC);
            PayloadTypeRegistry.playS2C().register(new CustomPacketPayload.Type<>(WorldInfoPayload.WORLD_INFO_PACKET_ID), WorldInfoPayload.CODEC);
        }
    }

    @Override
    public void onGameStarted() {
        CarpetETP.LOGGER.info("Carpet-ETP-Addition started");
        CarpetServer.settingsManager.parseSettingsClass(CarpetETPSettings.class);
    }

    @Override
    public void onServerLoaded(MinecraftServer server){
        minecraftServer = server;
        HighlightManager.reset();
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return CarpetETPAdditionTranslations.getTranslationsFromResource(lang);
    }
}