package com.etpserver.carpetetpaddition.mixins.rule.rideCommandCanRidePlayers;

import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @SuppressWarnings("UnreachableCode")
    @Inject(
            method = "tickRiding",
            at = @At("HEAD")
    )
    private void reduceBoundingBox(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        Box box = player.getBoundingBox();
        if (player.getVehicle() != null && player.getVehicle() instanceof ServerPlayerEntity) {
            player.getDimensions(EntityPose.SITTING).scaled(0.9f);
        }
    }
}