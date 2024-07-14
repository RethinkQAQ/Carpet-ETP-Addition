package com.etparty.carpetetpaddition;

import carpet.api.settings.Rule;
import static carpet.api.settings.RuleCategory.*;

public class CarpetETPSettings {
    //ETP Rule categories
    public static final String ETP = "ETP";
    public static final String LOGGER = "logger";
    public static final String CARPET_MOD = "carpet-mod";

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

    public static void onWorldLoadingStarted(){
        CarpetETP.LOGGER.info("CAT settings loading started");
    }
}