package com.etpserver.carpetetpaddition.mixins.rule.testMode;

import carpet.patches.EntityPlayerMPFake;
import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServePlayerEntityMixin extends PlayerEntity {

    public ServePlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Shadow public abstract boolean changeGameMode(GameMode gameMode);

    @Shadow public abstract boolean isSpectator();


    @SuppressWarnings("all")
    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void changeToSpectator(CallbackInfo ci) {
        if (CarpetETPSettings.testMode && !this.isSpectator() && !((ServerPlayerEntity)(Object)this instanceof EntityPlayerMPFake)) {
            this.changeGameMode(GameMode.SPECTATOR);
        }
    }
}
