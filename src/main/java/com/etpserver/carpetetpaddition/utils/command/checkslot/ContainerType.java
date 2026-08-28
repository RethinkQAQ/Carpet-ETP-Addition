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

public final class ContainerType {

    private final String id;

    private final ContainerDefinition definition;

    private final ContainerProvider provider;

    public ContainerType(String id, ContainerDefinition definition, ContainerProvider provider) {
        this.id = Objects.requireNonNull(id, "id");
        this.definition = Objects.requireNonNull(definition, "definition");
        this.provider = Objects.requireNonNull(provider, "provider");
    }

    public String id() {
        return this.id;
    }

    public ContainerDefinition definition() {
        return this.definition;
    }

    public ContainerProvider provider() {
        return this.provider;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof  ContainerType other)) {
            return  false;
        }

        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
