package com.etpserver.carpetetpaddition.mixins.rule.rideCommandCanRidePlayers;

import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Shadow
    @Nullable
    private Entity vehicle;

    @Inject(
            method = "startRiding(Lnet/minecraft/entity/Entity;Z)Z",
            at = @At(
                    value = "RETURN"
            )
    )
    private void sendRidePacket(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (CarpetETPSettings.rideCommandCanRidePlayers && entity instanceof ServerPlayerEntity) {
            if (this.vehicle != null && !this.vehicle.getWorld().isClient) {
                ((ServerPlayerEntity) vehicle).networkHandler.sendPacket(new EntityPassengersSetS2CPacket(vehicle));
            }
        }
    }
}
