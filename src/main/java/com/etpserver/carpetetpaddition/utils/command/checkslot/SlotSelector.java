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

import java.util.Objects;

public final class SlotSelector {

    public enum Type {
        ANY,
        INDEX,
        ALIAS
    }

    private static final SlotSelector ANY = new SlotSelector(Type.ANY, -1, null);

    private final Type type;

    private final int slot;

    private final String alias;

    private SlotSelector(Type type, int slot, String alias) {
        this.type = type;
        this.slot = slot;
        this.alias = alias;
    }

    public static SlotSelector any() {
        return ANY;
    }

    public static SlotSelector index(int slot) {
        if (slot < 0) {
            throw new IllegalArgumentException("Slot index must be >= 0");
        }

        return new SlotSelector(Type.INDEX, slot, null);
    }

    public static SlotSelector alias(String alias) {

        Objects.requireNonNull(alias);

        return new SlotSelector(Type.ALIAS, -1, alias);
    }

    public Type type() {
        return this.type;
    }

    public boolean isAny() {
        return this.type == Type.ANY;
    }
    public boolean isIndex() {
        return this.type == Type.INDEX;
    }
    public boolean isAlias() {
        return this.type == Type.ALIAS;
    }

    public int slot() {
        if (!isIndex()) {
            throw new IllegalArgumentException("Not an INDEX selector");
        }
        return this.slot;
    }

    public String alias() {
        if (!isAlias()) {
            throw new IllegalArgumentException("Not an ALIAS selector");
        }
        return this.alias;
    }

    @Override
    public String toString() {
        return switch (type) {
            case ANY -> "*";
            case INDEX -> Integer.toString(slot);
            case ALIAS -> alias;
        };
    }
}
