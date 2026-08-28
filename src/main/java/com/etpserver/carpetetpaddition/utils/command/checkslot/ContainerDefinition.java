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

import java.util.*;

public class ContainerDefinition {

    private final int size;

    private final Map<String, Integer> aliases;

    private ContainerDefinition(int size, Map<String, Integer> aliases) {
        if (size <= 0) {
            throw new IllegalArgumentException("Container size must be greater than 0.");
        }

        this.size = size;
        this.aliases = Collections.unmodifiableMap(new LinkedHashMap<>(aliases));
    }

    public int size() {
        return size;
    }

    public boolean isValidSlot(int slot) {
        return slot >= 0 && slot < size;
    }

    public boolean hasAlias(String alias) {
        return aliases.containsKey(alias);
    }

    public int getSlot(String alias) {
        Integer slot = aliases.get(alias);
        if (slot == null) {
            throw new IllegalArgumentException("Unknown slot alias: " + alias);
        }
        return slot;
    }

    public Collection<String> aliases() {
        return aliases.keySet();
    }


    public Map<String, Integer> aliasMap() {
        return aliases;
    }

    public static Builder builder(int size) {
        return new Builder(size);
    }


    public static final class Builder {

        private final int size;
        private final Map<String, Integer> aliases = new LinkedHashMap<>();

        private Builder(int size) {
            if (size <= 0) {
                throw new IllegalArgumentException("Container size must be greater than 0.");
            }
            this.size = size;
        }

        public Builder alias(String name, int slot) {
            Objects.requireNonNull(name, "name");

            if (slot < 0 || slot >= size) {
                throw new IllegalArgumentException("Slot index out of range: " + slot);
            }
            aliases.put(name.toLowerCase(), slot);
            return this;
        }

        public ContainerDefinition build() {
            return new ContainerDefinition(size, aliases);
        }
    }
}
