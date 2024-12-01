package com.etpserver.carpetetpaddition.mixins.rule.InstantaneousRedstoneLamp;

import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.block.RedstoneLampBlock.LIT;

@Mixin(RedstoneLampBlock.class)
public class RedstoneLampBlockMixin {

    @Inject(
            method = "neighborUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"
        ),
            cancellable = true)
    private void onNeighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        if(CarpetETPSettings.InstantaneousRedstoneLamp) {
            ci.cancel();
            world.setBlockState(pos, state.cycle(LIT),Block.NOTIFY_LISTENERS);
        }
    }
}
