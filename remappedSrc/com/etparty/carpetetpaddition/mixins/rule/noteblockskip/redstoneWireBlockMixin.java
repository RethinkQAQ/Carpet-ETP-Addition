package com.etpserver.carpetetpaddition.mixins.rule.noteblockskip;

import com.etpserver.carpetetpaddition.CarpetETPSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.TrapdoorBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;

@Mixin(RedstoneWireBlock.class)
public abstract class redstoneWireBlockMixin {
    @WrapOperation(
            method = "getRenderConnectionType(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Z)Lnet/minecraft/block/enums/WireConnection;",
            constant = @Constant(classValue = TrapdoorBlock.class)
    )
    private boolean getRenderConnectionType(Object object, Operation<Boolean> original){
        return !CarpetETPSettings.redStoneDontConnectToTrapDoor && original.call(object);
    }

    @ModifyExpressionValue(
            method = "getStateForNeighborUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/RedstoneWireBlock;canRunOnTop(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z")
    )
    private boolean getStateForNeighborUpdate(boolean original){
        return CarpetETPSettings.redStoneWireCanRunOnTrapDoor||original;
    }
}
