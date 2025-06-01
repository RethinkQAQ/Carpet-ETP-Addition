package com.etpserver.carpetetpaddition.mixins.rule.disableEggSpawnChicken;

import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import net.minecraft.entity.projectile.thrown.EggEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EggEntity.class)
public class EggEntityMixin {
    @Inject(
            method = "onCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            ),
            cancellable = true)
    private void disableEggSpawnChicken(CallbackInfo ci) {
        if (CarpetETPSettings.disableEggSpawnChicken) {
            ci.cancel();
        }
    }
}