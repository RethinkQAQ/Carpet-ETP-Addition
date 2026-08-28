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

import com.etpserver.carpetetpaddition.utils.command.checkslot.ContainerRegistry;
import com.etpserver.carpetetpaddition.utils.command.checkslot.ContainerType;
import com.mojang.math.Transformation;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Display;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class HighlightManager {

    public static final String TAG = "carpetetpaddition_checkslot";

    private static final String WHITE_TEAM_NAME = "carpetetp_checkslot_white";

    private static final String RED_TEAM_NAME = "carpetetp_checkslot_red";

    private static final int FLASH_TICKS = 4;

    private static final int MAX_FLASH = 512;

    private static final Transformation EXPAND = new Transformation(
            new Vector3f(0.0f, 0.0f, 0.0f),
            new Quaternionf(),
            new Vector3f(1.01f, 1.01f, 1.01f),
            new Quaternionf());

    private record Flash(CheckSlotHighlightEntity entity, long expireAtGameTime) {

    }

    private static final Map<BlockPos, Flash> FLASH = new HashMap<>();

    private static final List<CheckSlotHighlightEntity> MATCHED = new ArrayList<>();

    private HighlightManager() {

    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> tick(server.overworld().getGameTime()));
    }

    public static void flash(ServerLevel level, BlockPos pos) {
        BlockPos immutable = pos.immutable();
        if (FLASH.containsKey(immutable) || FLASH.size() >= MAX_FLASH) {
            return;
        }
        CheckSlotHighlightEntity entity = spawn(level, immutable);
        joinTeam(level, entity, WHITE_TEAM_NAME, ChatFormatting.WHITE);
        FLASH.put(immutable, new Flash(entity, level.getGameTime() + FLASH_TICKS));
    }

    public static void markMatched(ServerLevel level, BlockPos pos) {
        BlockPos immutable = pos.immutable();

        Flash flash = FLASH.remove(immutable);
        if (flash != null) {
            joinTeam(level, flash.entity(), RED_TEAM_NAME, ChatFormatting.RED);
            MATCHED.add(flash.entity());
            return;
        }

        CheckSlotHighlightEntity entity = spawn(level, immutable);
        joinTeam(level, entity, RED_TEAM_NAME, ChatFormatting.RED);
        MATCHED.add(entity);
    }

    public static int clearAll() {
        int count = 0;
        for (Flash flash : FLASH.values()) {
            discardIfAlive(flash.entity());
        }
        FLASH.clear();
        for (CheckSlotHighlightEntity entity : MATCHED) {
            discardIfAlive(entity);
            count++;
        }
        MATCHED.clear();
        return count;
    }

    public static void reset() {
        FLASH.clear();
        MATCHED.clear();
    }

    public static int activeCount() {
        return MATCHED.size();
    }

    public static @Nullable Container resolveContainer(ServerLevel level, BlockPos pos, ContainerType type) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) {
            return null;
        }
        ContainerType found = ContainerRegistry.byBlockEntity(blockEntity.getType());
        if (found != type) {
            return null;
        }
        return type.provider().getContainer(level, pos, blockEntity);
    }

    private static void tick(long now) {
        if (FLASH.isEmpty()) {
            return;
        }
        FLASH.values().removeIf(flash -> {
            if (now >= flash.expireAtGameTime()) {
                discardIfAlive(flash.entity());
                return true;
            }
            return false;
        });
    }

    private static CheckSlotHighlightEntity spawn(ServerLevel level, BlockPos pos) {
        CheckSlotHighlightEntity entity = new CheckSlotHighlightEntity(level);
        entity.setPos(pos.getX(), pos.getY(), pos.getZ());
        entity.setBlockState(level.getBlockState(pos));
        entity.setTransformation(EXPAND);
        entity.setGlowingTag(true);
        entity.setInvulnerable(true);
        entity.addTag(TAG);
        level.addFreshEntity(entity);
        return entity;
    }

    private static void joinTeam(ServerLevel level, CheckSlotHighlightEntity entity, String teamName, ChatFormatting color) {
        Scoreboard scoreboard = level.getScoreboard();
        PlayerTeam team = scoreboard.getPlayerTeam(teamName);
        if (team == null) {
            team = scoreboard.addPlayerTeam(teamName);
        }
        team.setColor(color);
        scoreboard.addPlayerToTeam(entity.getScoreboardName(), team);
    }

    private static void discardIfAlive(@Nullable CheckSlotHighlightEntity entity) {
        if (entity != null && !entity.isRemoved()) {
            entity.discard();
        }
    }
}
