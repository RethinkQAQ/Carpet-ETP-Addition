package com.etpserver.carpetetpaddition.mixins.rule.rideCommandCanRidePlayers;

import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.RideCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RideCommand.class)
public class RideCommandMixin {
    @WrapOperation(
            method = "executeMount",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;getType()Lnet/minecraft/entity/EntityType;"
            )
    )
    private static EntityType<?> canRidePlayers(Entity entity, Operation<EntityType<?>> original) {
        return CarpetETPSettings.rideCommandCanRidePlayers?  null: original.call(entity);
    }
}
