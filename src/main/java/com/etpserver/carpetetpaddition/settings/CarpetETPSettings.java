package com.etpserver.carpetetpaddition.settings;

import carpet.api.settings.Rule;
import com.etpserver.carpetetpaddition.CarpetETP;

import static carpet.api.settings.RuleCategory.*;

public class CarpetETPSettings {
    //ETP Rule categories
    public static final String ETP = "ETP";
    public static final String LOGGER = "logger";
    public static final String CARPET_MOD = "carpet-mod";
    public static final String XaeroMapNoneName = "#none";

    @Rule(categories = {ETP,BUGFIX})
    public static boolean redStoneDontConnectToTrapDoor = false;

    @Rule(categories = {ETP,BUGFIX})
    public static boolean redStoneWireCanRunOnTrapDoor = false;

    @Rule(categories = ETP)
    public static boolean unExternPistonCanBeCompare = false;

    @Rule(categories = ETP)
    public static boolean CrafterCanQC = false;

    @Rule(categories = ETP)
    public static boolean WitherSkullWillDiscard =false;

    @Rule(categories = ETP)
    public static boolean InstantaneousRedstoneLamp = false;

    @Rule(categories = {ETP, CREATIVE})
    public static boolean targetBlockIgnoresProjectileHit = false;

    @Rule(categories = {ETP}, strict = false)
    public static String xaeroMapName = XaeroMapNoneName;

    public static void onWorldLoadingStarted(){
        CarpetETP.LOGGER.info("CAT settings loading started");
    }
}