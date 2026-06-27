/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.dimension.transition;

import io.kalishak.galacticraftlegacy.config.ServerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class FixedPlanetaryTransition extends PlanetaryTransition {
    protected final BlockPos pos;
    private final boolean isSpaceStation;

    protected FixedPlanetaryTransition(BlockPos pos, boolean isSpaceStation) {
        super(TransitionType.FIXED_POSITION.get());
        this.pos = pos;
        this.isSpaceStation = isSpaceStation;
    }

    public static FixedPlanetaryTransition orbital() {
        return new FixedPlanetaryTransition(new BlockPos(0, 65, 0), false) {
            @Override
            public BlockPos getParachestSpawnLocation(ServerLevel level, ServerPlayer player) {
                return new  BlockPos(-8, 90, -1);
            }
        };
    }

    public static FixedPlanetaryTransition spaceStation() {
        return new FixedPlanetaryTransition(new BlockPos(0, 65, 0), true);
    }

    @Override
    public boolean useParachute() {
        return false;
    }

    @Override
    public BlockPos getPlayerSpawnLocation(ServerLevel level, ServerPlayer player) {
        return this.pos;
    }

    @Override
    public BlockPos getEntitySpawnLocation(ServerLevel level, Entity entity) {
        return this.pos;
    }

    @Override
    public @Nullable BlockPos getParachestSpawnLocation(ServerLevel level, ServerPlayer player) {
        return null;
    }

    @Override
    public void onDimensionChange(Level newLevel, ServerPlayer player, boolean isRidingAutoRocket) {
        if (this.isSpaceStation && ServerConfig.SPACE_STATIONS_PERMISSIONS.get() && !newLevel.isClientSide()) {
            player.sendSystemMessage(
                    Component.translatable("gui.spacestation.type_command").withStyle(ChatFormatting.YELLOW)
                            .append(Component.literal("/ssinvite").withStyle(ChatFormatting.AQUA))
                            .append(Component.translatable("gui.spacestation.playername", Component.translatable("gui.spacestation.to_allow_entry").withStyle(ChatFormatting.YELLOW))
            ));
        }
    }
}
