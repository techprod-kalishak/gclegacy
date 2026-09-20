/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.features;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.level.levelgen.CraterSize;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;
import java.util.function.Predicate;

public class GalacticraftFeatures {
    private static final DeferredRegister<MapCodec<? extends Feature>> REGISTRY = DeferredRegister.create(Registries.FEATURE_TYPE, Galacticraft.MODID);
    public static final ResourceKey<Feature> SPACE_STATION_KEY = key("space_station");

    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<CrudeOilPoolFeature>> CRUDE_OIL_POOL = REGISTRY.register(
            "crude_oil_pool",
            () -> CrudeOilPoolFeature.MAP_CODEC
    );
    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<CraterFeature>> CRATER = REGISTRY.register(
            "crater",
            () -> CraterFeature.MAP_CODEC
    );
    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<FallenMeteorFeature>> FALLEN_METEOR = REGISTRY.register(
            "fallen_meteor",
            () -> FallenMeteorFeature.MAP_CODEC
    );
    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<SpaceStationFeature>> SPACE_STATION = REGISTRY.register(
            "space_station",
            () -> SpaceStationFeature.MAP_CODEC
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }

    public static void bootstrap(BootstrapContext<Feature> cxt) {
        AsteroidsFeatures.bootstrap(cxt);
        MarsFeatures.bootstrap(cxt);
        MoonFeatures.bootstrap(cxt);
        OverworldFeatures.bootstrap(cxt);
        VenusFeatures.bootstrap(cxt);

        cxt.register(SPACE_STATION_KEY, new SpaceStationFeature());
    }

    static ResourceKey<Feature> key(String name) {
        return Constants.key(Registries.FEATURE, name);
    }

    static void craterSimple(BootstrapContext<Feature> cxt, ResourceKey<Feature> key, CraterSize size, int uniformCount) {
        cxt.register(
                key,
                new CraterFeature(size, ConstantInt.of(uniformCount))
        );
    }

    public static Optional<Column> findBottom(WorldGenLevel levelGen, BlockPos pos) {
        Predicate<BlockState> inWater = state -> state.is(Blocks.WATER);
        Predicate<BlockState> notWater = state -> !state.is(Blocks.WATER);

        return Column.scan(levelGen, pos, 90, inWater, notWater);
    }
}
