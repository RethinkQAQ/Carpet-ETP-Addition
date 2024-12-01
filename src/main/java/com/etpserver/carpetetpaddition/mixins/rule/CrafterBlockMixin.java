package com.etpserver.carpetetpaddition.mixins.rule;

import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.CrafterBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CrafterBlock.class)
public class CrafterBlockMixin {
    @WrapOperation(
            method = "neighborUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"
        )
    )
    private boolean isReceivingRedstonePower(World world, BlockPos blockPos, Operation<Boolean> original) {
        return world.isReceivingRedstonePower(blockPos) || (world.isReceivingRedstonePower(blockPos.up()) && CarpetETPSettings.CrafterCanQC);
    }
}
