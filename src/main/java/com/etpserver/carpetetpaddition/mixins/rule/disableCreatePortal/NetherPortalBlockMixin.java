package com.etpserver.carpetetpaddition.mixins.rule.disableCreatePortal;

import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin {

    @Inject(
            method = "getOrCreateExitPortalTarget",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/dimension/PortalForcer;createPortal(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction$Axis;)Ljava/util/Optional;"
            ),
            cancellable = true)
    private void disableEntityCreatePortal(ServerWorld world, Entity entity, BlockPos pos, BlockPos scaledPos, boolean inNether, WorldBorder worldBorder, CallbackInfoReturnable<TeleportTarget> cir) {
        if (!CarpetETPSettings.disableCreatePortal.equals("OFF")) {
            if (("ALL".equals(CarpetETPSettings.disableCreatePortal) ||
                    ("NonPlayer".equals(CarpetETPSettings.disableCreatePortal) && !(entity instanceof PlayerEntity)))) {
                cir.setReturnValue(null);
            }
        }
    }
}
