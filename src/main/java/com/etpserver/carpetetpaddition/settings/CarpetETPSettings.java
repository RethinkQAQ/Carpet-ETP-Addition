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

package com.etpserver.carpetetpaddition.settings;

import carpet.api.settings.Rule;

import static carpet.api.settings.RuleCategory.*;

public class CarpetETPSettings {
    //ETP Rule categories
    public static final String ETP = "ETP";
    public static final String LOGGER = "logger";
    public static final String MULTIWORLDID = "#none";

    @Rule(categories = {ETP,BUGFIX})
    public static boolean redStoneDontConnectToTrapDoor = false;

    @Rule(categories = {ETP,BUGFIX})
    public static boolean redStoneWireCanRunOnTrapDoor = false;

    @Rule(categories = ETP)
    public static boolean unExternPistonCanBeCompare = false;

    @Rule(categories = ETP)
    public static boolean CrafterCanQC = false;

    @Rule(categories = ETP)
    public static boolean InstantaneousRedstoneLamp = false;

    @Rule(categories = {ETP, CREATIVE})
    public static boolean targetBlockIgnoresProjectileHit = false;

    @Rule(categories = {ETP}, strict = false)
    public static String xaeroMapName = MULTIWORLDID;

    @Rule(categories = {LOGGER, ETP})
    public static boolean villagerGiftsToHeroCD = false;

    @Rule(categories = {CREATIVE, ETP})
    public static boolean rideCommandCanRidePlayers = false;

    //#if MC <= 12102
    @Rule(categories = {ETP})
    public static boolean pearlTickets = false;
    //#endif

    @Rule(
            categories = {ETP},
            strict = true,
            options = {"OFF", "NonPlayer", "ALL"}
    )
    public static String disableCreatePortal = "OFF";

    @Rule(categories = {ETP, CREATIVE})
    public static boolean disableEggSpawnChicken = false;

    @Rule(categories = {ETP, CREATIVE})
    public static boolean testMode = false;

    @Rule(categories = {ETP, SURVIVAL})
    public static boolean spectatorLeashBreak = false;

    @Rule(categories = {ETP, CREATIVE})
    public static boolean commandBlockFeedbackEnhanced = false;

    @Rule(
            categories = {ETP, CREATIVE},
            strict = false,
            options = {"6000", "12000", "never"}
    )
    public static String itemDespawnTime = "6000";
}