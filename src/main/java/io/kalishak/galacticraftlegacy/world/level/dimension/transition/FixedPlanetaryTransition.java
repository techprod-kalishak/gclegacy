/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.dimension.transition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.config.ServerConfig;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class FixedPlanetaryTransition extends PlanetaryTransition {
    public static final MapCodec<FixedPlanetaryTransition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(transition -> transition.pos),
            BlockPos.CODEC.optionalFieldOf("parachest_pos").forGetter(transition -> transition.parachestPos),
            Codec.BOOL.optionalFieldOf("is_space_station", false).forGetter(transition -> transition.isSpaceStation)
    ).apply(instance, FixedPlanetaryTransition::new));

    protected final BlockPos pos;
    protected final Optional<BlockPos> parachestPos;
    private final boolean isSpaceStation;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public FixedPlanetaryTransition(BlockPos pos, Optional<BlockPos> optionalParachestPos, boolean isSpaceStation) {
        this.pos = pos;
        this.parachestPos = optionalParachestPos;
        this.isSpaceStation = isSpaceStation;
    }

    public static FixedPlanetaryTransition spaceStation() {
        return new FixedPlanetaryTransition(new BlockPos(0, 65, 0), Optional.of(new BlockPos(-8, 90, -1)), false);
    }

    public static FixedPlanetaryTransition orbital() {
        return new FixedPlanetaryTransition(new BlockPos(0, 65, 0), null, true);
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
        return this.parachestPos.orElse(null);
    }

    @Override
    public void onDimensionChange(Level newLevel, ServerPlayer player, boolean isRidingAutoRocket) {
        if (this.isSpaceStation && ServerConfig.SPACE_STATIONS_PERMISSIONS.get() && !newLevel.isClientSide()) {
            player.sendSystemMessage(GalacticraftComponents.SPACE_STATION_TYPE_COMMAND);
        }
    }

    @Override
    public MapCodec<FixedPlanetaryTransition> codec() {
        return CODEC;
    }
}
