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
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.chat.Component;

public final class SlotMatcher {

    private static final SimpleCommandExceptionType INVALID_SLOT =
            new SimpleCommandExceptionType(
                    Component.literal("无效的槽位")
            );

    private SlotMatcher() {}

    public static IntList resolve(ContainerDefinition definition, SlotSelector selector) throws CommandSyntaxException {

        IntArrayList result = new IntArrayList();

        switch (selector.type()) {
            case ANY -> {
                for (int i = 0; i < definition.size(); i++) {
                    result.add(i);
                }
                return  result;
            }
            case INDEX -> {
                int slot = selector.slot();
                if (!definition.isValidSlot(slot)) {
                    throw INVALID_SLOT.create();
                }

                result.add(slot);
                return  result;
            }
            case ALIAS -> {
                if (!definition.hasAlias(selector.alias())) {
                    throw INVALID_SLOT.create();
                }
                result.add(definition.getSlot(selector.alias()));
                return  result;
            }
        }
        throw new IllegalArgumentException();
    }
}
