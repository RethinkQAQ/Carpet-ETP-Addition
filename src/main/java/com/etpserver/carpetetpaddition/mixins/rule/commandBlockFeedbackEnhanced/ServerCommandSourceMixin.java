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
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("命令方块坐标: [" + this.position.x + ", " + this.position.y + ", " + this.position.z + "]\n点击可传送")))
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + this.position.x + " " + this.position.y + " " + this.position.z))

                    );
            return prefix.append(original);
        }
        return original;
    }
}
