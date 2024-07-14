package com.etparty.carpetetpaddition.mixins.rule;

import com.etparty.carpetetpaddition.CarpetETPSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WitherSkullEntity.class)
public abstract class WitherSkullEntityMixin extends ExplosiveProjectileEntity {
    @Unique
    private int LIVING_TIME = 400;

    protected WitherSkullEntityMixin(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    @Override
    public void tick() {
        super.tick();
        if (CarpetETPSettings.WitherSkullWillDiscard && this.LIVING_TIME > 0) {
            this.LIVING_TIME--;
            if (this.LIVING_TIME == 0) {
                this.discard();
            }
        }
    }
}
