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

    @Rule(categories = {ETP})
    public static boolean pearlTickets = false;

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
}