package com.etparty.carpetetpaddition.mixins.rule.TargetBlockIgnoresProjectileHit;

import com.etparty.carpetetpaddition.CarpetETPSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.TargetBlock;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TargetBlock.class)
public class TargetBlockMixin {
    @Inject(
            method = "onProjectileHit",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile, CallbackInfo ci) {
        if (CarpetETPSettings.targetBlockIgnoresProjectileHit) {
            ci.cancel();
        }
    }
}
