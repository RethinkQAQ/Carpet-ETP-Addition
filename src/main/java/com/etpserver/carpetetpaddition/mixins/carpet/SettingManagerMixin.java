package com.etpserver.carpetetpaddition.mixins.carpet;

import carpet.api.settings.SettingsManager;
import com.etpserver.carpetetpaddition.CarpetETP;
import carpet.utils.Translations;
import carpet.utils.Messenger;
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
                            args = "stringValue=carpet.settings.command.version",
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
            Messenger.m(source,
                    Messenger.c(
                            String.format("g %s ", CarpetETP.MOD_NAME),
                            String.format("g %s: ", Translations.tr("carpet.settings.command.version", "version")),
                            String.format("g %s ", CarpetETP.getVersion())
                    )
            );
    }
}
