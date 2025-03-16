package com.etpserver.carpetetpaddition.mixins.rule.giftsToHeroCD;

import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import net.minecraft.entity.ai.brain.task.GiveGiftsToHeroTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GiveGiftsToHeroTask.class)
public class GiveGiftsToHeroTaskMixin {
    @Shadow private int ticksLeft;

    @Inject(
            method = "shouldRun(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;)Z",
            at = @At("HEAD")
    )
    private void shouldRun(ServerWorld serverWorld, VillagerEntity villagerEntity, CallbackInfoReturnable<Boolean> cir) {
        if (CarpetETPSettings.villagerGiftsToHeroCD){
            villagerEntity.setCustomName(Text.of(String.valueOf(ticksLeft)));
            villagerEntity.setCustomNameVisible(true);
        }
    }
}
