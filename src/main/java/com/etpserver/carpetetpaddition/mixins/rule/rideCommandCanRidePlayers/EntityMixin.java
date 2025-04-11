package com.etpserver.carpetetpaddition.mixins.rule.rideCommandCanRidePlayers;

import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;

@Mixin(Entity.class)
public class EntityMixin {
    @WrapOperation(
            method = "method_37217",
            constant = @Constant(
                    classValue = ServerPlayerEntity.class
        )
    )
    private static boolean startRiding(Object object, Operation<Boolean> original) {
        return CarpetETPSettings.rideCommandCanRidePlayers || original.call(object);
    }
}
