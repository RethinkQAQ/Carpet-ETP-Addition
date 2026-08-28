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

package com.etpserver.carpetetpaddition.utils.command.checkslot;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.network.chat.Component;

import java.util.stream.Collectors;

public final class SearchRequestParser {

    private static final SimpleCommandExceptionType INVALID_FORMAT =
            new SimpleCommandExceptionType(
                    Component.literal("格式无效,应为 <容器>.<槽位>,例如 chest.0 或 furnace.fuel")
            );

    private static final SimpleCommandExceptionType UNKNOWN_CONTAINER =
            new SimpleCommandExceptionType(
                    Component.literal("未知的容器类型")
            );

    private static final SimpleCommandExceptionType INVALID_SLOT =
            new SimpleCommandExceptionType(
                    Component.literal("无效的槽位")
            );

    private SearchRequestParser() {}

    public static SearchRequest parse(int radius, String expression) throws CommandSyntaxException {

        int separator = expression.indexOf('.');
        if (separator < 0 || separator != expression.lastIndexOf('.')) {
            throw INVALID_FORMAT.create();
        }

        String containerId = expression.substring(0, separator);
        String slotExpression = expression.substring(separator + 1);

        ContainerType container = parseContainer(containerId);

        SlotSelector slotSelector = parseSlot(container, slotExpression);

        return  new SearchRequest(radius, container, slotSelector);
    }

    private static ContainerType parseContainer(String id) throws CommandSyntaxException {
        ContainerType type = ContainerRegistry.byId(id.toLowerCase());

        if (type == null) {
            String ids = ContainerRegistry.values().stream()
                    .map(ContainerType::id)
                    .collect(Collectors.joining(", "));
            throw new CommandSyntaxException(UNKNOWN_CONTAINER, Component.literal("未知的容器类型: " + id + ",可用: " + ids));
        }
        return type;
    }

    private static SlotSelector parseSlot(ContainerType container, String value) throws CommandSyntaxException {
        if (value.equals("*")) {
            return SlotSelector.any();
        }

        if (isInteger(value)) {
            int slot = Integer.parseInt(value);
            if (!container.definition().isValidSlot(slot)) {
                throw new CommandSyntaxException(INVALID_SLOT, Component.literal(
                        "无效的槽位: " + container.id() + "." + value + ",槽位范围为 0 ~ " + (container.definition().size() - 1)));
            }
            return  SlotSelector.index(slot);
        }

        if (!container.definition().hasAlias(value)) {
            String aliases = String.join(", ", container.definition().aliases());
            throw new CommandSyntaxException(INVALID_SLOT, Component.literal(
                    "未知的槽位别名: " + container.id() + "." + value + (aliases.isEmpty() ? ",该容器没有可用别名" : ",可用: " + aliases)));
        }

        return SlotSelector.alias(value);
    }

    private static boolean isInteger(String value) {
        if (value.isEmpty()) {
            return false;
        }

        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
