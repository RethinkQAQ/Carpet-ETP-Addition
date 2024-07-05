package com.etparty.carpetetpaddition.mixins.rule;

import com.etparty.carpetetpaddition.CarpetETPSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static net.minecraft.block.PistonBlock.EXTENDED;

@Mixin(ComparatorBlock.class)
public class ComparatorBlockMixin {
    @WrapOperation(
            method = "getPower",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;isSolidBlock(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z")
    )
    protected boolean getPower(BlockState state, BlockView blockView, BlockPos blockPos, Operation<Boolean> original) {
        if(CarpetETPSettings.unExternPistonCanBeCompare &&(state.getBlock() instanceof PistonBlock)){
            return !state.get(EXTENDED);
        } else {
            return original.call(state, blockView, blockPos);
        }
    }
}
