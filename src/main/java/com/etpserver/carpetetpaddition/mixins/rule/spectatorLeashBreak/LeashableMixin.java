package com.etpserver.carpetetpaddition.mixins.rule.spectatorLeashBreak;

import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Leashable;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Leashable.class)
public interface LeashableMixin {
    @Shadow void detachLeash();

    @Inject(
            method = "beforeLeashTick",
            at = @At("HEAD")
    )
    default void beforeLeashTick(Entity leashHolder, float distance, CallbackInfoReturnable<Boolean> cir) {
        if (CarpetETPSettings.spectatorLeashBreak) {
            if (leashHolder instanceof ServerPlayerEntity player && player.isSpectator()) {
                this.detachLeash();
            }
        }
    }
}
