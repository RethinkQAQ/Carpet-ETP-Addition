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

package com.etpserver.carpetetpaddition.mixins.rule.commandBlockFeedbackEnhanced;

import com.etpserver.carpetetpaddition.settings.CarpetETPSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 12105
//$$ import com.etpserver.carpetetpaddition.utils.HoverEventUtils;
//$$ import com.etpserver.carpetetpaddition.utils.ClickEventUtils;
//#endif

@Mixin(ServerCommandSource.class)
public class ServerCommandSourceMixin {
    @Shadow @Final private Vec3d position;

    @ModifyExpressionValue(
            method = "sendToOps",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;")
    )
    private MutableText getDisplayName(MutableText original) {
        if (CarpetETPSettings.commandBlockFeedbackEnhanced) {
            MutableText prefix = Text.literal("[命令方块]")
                    .styled(
                            style -> style
                                    //#if MC >= 12105
                                    //$$ .withHoverEvent(HoverEventUtils.showText(Text.literal("命令方块坐标: [" + this.position.x + ", " + this.position.y + ", " + this.position.z + "]\n点击可传送")))
                                    //$$ .withClickEvent(ClickEventUtils.runCommand("/tp " + this.position.x + " " + this.position.y + " " + this.position.z))
                                    //#else
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("命令方块坐标: [" + this.position.x + ", " + this.position.y + ", " + this.position.z + "]\n点击可传送")))
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + this.position.x + " " + this.position.y + " " + this.position.z))
                                    //#endif

                    );
            return prefix.append(original);
        }
        return original;
    }
}
