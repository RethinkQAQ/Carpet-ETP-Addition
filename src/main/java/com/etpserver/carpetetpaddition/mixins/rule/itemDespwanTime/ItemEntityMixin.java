/*
 * This file is part of the CarpetETPAddition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Rethink_QAQ and contributors
 *
 * CarpetETPAddition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CarpetETPAddition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CarpetETPAddition.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.etpserver.carpetetpaddition.mixins.rule.itemDespwanTime;

import com.etpserver.carpetetpaddition.CarpetETP;
import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(net.minecraft.world.entity.item.ItemEntity.class)
public class ItemEntityMixin {
    @Shadow
    private int age;

    @Definition(id = "age", field = "Lnet/minecraft/world/entity/item/ItemEntity;age:I")
    @Expression("this.age >= 6000")
    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "MIXINEXTRAS:EXPRESSION"
            )
    )
    public boolean modifyItemDespawnTime(boolean original) {
        if (!CarpetETPSettings.itemDespawnTime.equals("6000") && !CarpetETPSettings.itemDespawnTime.equals("never")) {
            try {
                int despawnTime = Integer.parseInt(CarpetETPSettings.itemDespawnTime);
                return this.age >= despawnTime;
            } catch (NumberFormatException e) {
                CarpetETP.LOGGER.error("Invalid item despawn time", e);
            }
        }
        return original;
    }


    @Definition(id = "age", field = "Lnet/minecraft/world/entity/item/ItemEntity;age:I")
    @Expression("this.age != -32768")
    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "MIXINEXTRAS:EXPRESSION"
            )
    )
    public boolean stopAgeIncrement(boolean original) {
        return !CarpetETPSettings.itemDespawnTime.equals("never") && original;
    }
}