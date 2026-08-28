/*
 * This file is part of the CarpetETPAddition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Rethink_QAQ and contributors
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

package com.etpserver.carpetetpaddition.commands.checkslot;

import com.etpserver.carpetetpaddition.utils.ClickEventUtils;
import com.etpserver.carpetetpaddition.utils.HoverEventUtils;
import com.etpserver.carpetetpaddition.utils.command.checkslot.ContainerRegistry;
import com.etpserver.carpetetpaddition.utils.command.checkslot.ContainerType;
import com.etpserver.carpetetpaddition.utils.command.checkslot.SearchRequest;
import com.etpserver.carpetetpaddition.utils.command.checkslot.SearchRequestParser;
import com.etpserver.carpetetpaddition.utils.command.checkslot.SlotMatcher;
import com.etpserver.carpetetpaddition.utils.command.checkslot.scanner.ContainerScanner;
import com.etpserver.carpetetpaddition.utils.command.checkslot.scanner.SearchHit;
import com.etpserver.carpetetpaddition.utils.command.checkslot.scanner.SearchResult;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CheckSlotCommand {

    private static final int MAX_RADIUS = 50;
    private static final int MAX_CHAT_HITS = 32;
    private static final int MAX_HIGHLIGHTS = 256;

    public static void register() {
        HighlightManager.register();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("checkslot")
                    //#if MC < 12110
                    .requires(source -> source.hasPermission(2))
                    //#else
                    //$$ .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                    //#endif
                    .then(Commands.literal("clear")
                            .executes(CheckSlotCommand::executeClear))
                    .then(Commands.argument("radius", IntegerArgumentType.integer(1, MAX_RADIUS))
                            .then(Commands.argument("container.slot", StringArgumentType.greedyString())
                                    .suggests(CheckSlotCommand::suggestSlotExpressions)
                                    .executes(CheckSlotCommand::execute))));
        });
    }

    private static CompletableFuture<Suggestions> suggestSlotExpressions(
            CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        ContainerRegistry.bootstrap();

        List<String> suggestions = new ArrayList<>();
        for (ContainerType type : ContainerRegistry.values()) {
            String id = type.id();
            suggestions.add(id + ".*");
            suggestions.add(id + ".0");
            for (String alias : type.definition().aliases()) {
                suggestions.add(id + "." + alias);
            }
        }
        return SharedSuggestionProvider.suggest(suggestions, builder);
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        ServerPlayer player;
        try {
            player = source.getPlayerOrException();
        } catch (CommandSyntaxException e) {
            source.sendFailure(Component.literal("该命令仅限玩家使用。"));
            return 0;
        }

        int radius = IntegerArgumentType.getInteger(context, "radius");
        String expression = StringArgumentType.getString(context, "container.slot");
        ServerLevel level = source.getLevel();

        ContainerRegistry.bootstrap();

        SearchRequest request;
        IntList slots;
        try {
            request = SearchRequestParser.parse(radius, expression);
            slots = SlotMatcher.resolve(request.containerType().definition(), request.slotSelector());
        } catch (CommandSyntaxException e) {
            source.sendFailure(Component.literal(e.getRawMessage().getString()));
            return 0;
        }

        // 新查询替换旧高亮
        HighlightManager.clearAll();

        SearchResult result = ContainerScanner.scan(level, player.blockPosition(), request, slots,
                pos -> HighlightManager.flash(level, pos));

        if (result.isEmpty()) {
            source.sendSystemMessage(Component.literal(
                    "§7[checkslot] 在半径 " + radius + " 格内未找到装有物品的 " + request.containerType().id()
                            + "(扫描了 " + result.scannedContainers() + " 个容器,耗时 "
                            + String.format("%.1f", result.elapsedTime()) + " ms)"));
            return 0;
        }

        int spawned = 0;
        for (SearchHit hit : result.hits()) {
            if (spawned >= MAX_HIGHLIGHTS) {
                break;
            }
            HighlightManager.markMatched(level, hit.pos());
            spawned++;
        }

        sendFeedback(source, level, request, radius, result, spawned);
        return result.hitCount();
    }

    private static int executeClear(CommandContext<CommandSourceStack> context) {
        int removed = HighlightManager.clearAll();
        context.getSource().sendSystemMessage(Component.literal(
                "§a[checkslot] §f已清除 " + removed + " 个高亮。"));
        return removed;
    }

    private static void sendFeedback(CommandSourceStack source, ServerLevel level, SearchRequest request,
                                     int radius, SearchResult result, int spawned) {
        String containerId = request.containerType().id();

        MutableComponent summary = Component.literal("§a[checkslot] §f找到 §e" + result.hitCount() + "§f 处匹配的 "
                + containerId + "(扫描了 " + result.scannedContainers() + " 个容器,耗时 "
                + String.format("%.1f", result.elapsedTime()) + " ms)。");

        if (spawned < result.hitCount()) {
            summary.append(Component.literal("§7(红色高亮已达上限,仅标记前 " + spawned + " 处)"));
        } else {
            summary.append(Component.literal("§7(命中处已标红,输入 /checkslot clear 清除)"));
        }
        source.sendSystemMessage(summary);

        int shown = 0;
        for (SearchHit hit : result.hits()) {
            if (shown >= MAX_CHAT_HITS) {
                source.sendSystemMessage(Component.literal(
                        "§7…其余 " + (result.hitCount() - shown) + " 处略,请查看场上的高亮方块。"));
                break;
            }
            Container container = HighlightManager.resolveContainer(level, hit.pos(), request.containerType());
            source.sendSystemMessage(buildHitLine(hit, containerId, container));
            shown++;
        }
    }

    private static MutableComponent buildHitLine(SearchHit hit, String containerId, @Nullable Container container) {
        BlockPos pos = hit.pos();
        String slotList = hit.matchedSlots().isEmpty() ? "" : hit.matchedSlots().toString();

        MutableComponent line = Component.literal(" §8- §e[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]§r")
                .withStyle(style -> style
                        .withClickEvent(ClickEventUtils.runCommand(
                                "/tp @s " + (pos.getX() + 0.5) + " " + (pos.getY() + 1) + " " + (pos.getZ() + 0.5)))
                        .withHoverEvent(HoverEventUtils.showText(Component.literal(
                                "点击传送到 " + pos.getX() + " " + pos.getY() + " " + pos.getZ()))));
        line.append(Component.literal(" " + containerId + " 槽位 " + slotList));

        String itemSummary = buildSlotSummary(hit.matchedSlots(), container);
        if (!itemSummary.isEmpty()) {
            line.append(Component.literal(" §7(" + itemSummary + ")"));
        }
        return line;
    }

    private static String buildSlotSummary(IntList slots, @Nullable Container container) {
        if (container == null) {
            return "";
        }
        StringBuilder summary = new StringBuilder();
        int shown = 0;
        for (int i = 0; i < slots.size() && shown < 4; i++) {
            int slot = slots.getInt(i);
            if (slot >= container.getContainerSize()) {
                continue;
            }
            ItemStack stack = container.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }
            if (shown > 0) {
                summary.append(", ");
            }
            summary.append("#").append(slot)
                    .append(" ").append(stack.getCount()).append("×").append(stack.getHoverName().getString());
            shown++;
        }
        return summary.toString();
    }
}
