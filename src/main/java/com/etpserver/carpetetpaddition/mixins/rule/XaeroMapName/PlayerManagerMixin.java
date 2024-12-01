package com.etpserver.carpetetpaddition.mixins.rule.XaeroMapName;

import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import com.etpserver.carpetetpaddition.utils.XaeroMapProtocol;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(
            method = "sendWorldInfo",
            at = @At("RETURN")
    )
    private void sendWorldInfo(ServerPlayerEntity player, ServerWorld world, CallbackInfo ci) {
        if (CarpetETPSettings.xaeroWorldMapID == -1) {
            return;
        }
        XaeroMapProtocol.onSendWorldInfo(player);
    }
}