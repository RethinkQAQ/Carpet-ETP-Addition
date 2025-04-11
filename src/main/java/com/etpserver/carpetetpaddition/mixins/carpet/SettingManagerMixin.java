package com.etpserver.carpetetpaddition.mixins.carpet;

import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;
import net.minecraft.server.command.MessageCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SettingsManager.class)
public abstract class SettingManagerMixin {
    @Inject(
            method = "listAllSettings",
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue==ui.version",
                            ordinal = 0
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lcarpet/api/settings/SettingsManager;getCategories()Ljava/lang/Iterable;",
                    ordinal = 0
    ),
            remap = false
    )
    private void printVersion(ServerCommandSource source, CallbackInfoReturnable<Integer> cir) {
        if((Object)this == CarpetServer.settingsManager) {
        }
    }
}
