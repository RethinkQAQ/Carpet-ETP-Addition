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

public final class SearchRequest {

    private final int radius;

    private final ContainerType containerType;

    private final SlotSelector slotSelector;

    public SearchRequest(int radius, ContainerType containerType, SlotSelector slotSelector) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be greater than 0");
        }

        this.radius = radius;
        this.containerType = containerType;
        this.slotSelector = slotSelector;
    }

    public int radius() {
        return radius;
    }

    public ContainerType containerType() {
        return containerType;
    }

    public SlotSelector slotSelector() {
        return slotSelector;
    }

    @Override
    public String toString() {
        return "SearchRequest{" + "radius=" + radius + ", containerType=" + containerType + '}';
    }
}
