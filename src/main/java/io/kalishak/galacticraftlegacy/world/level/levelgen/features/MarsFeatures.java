/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.features;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class MarsFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> MARS_COPPER_ORE = GalacticraftFeatures.key("mars_copper_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MARS_TIN_ORE = GalacticraftFeatures.key("mars_tin_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MARS_DESH_ORE = GalacticraftFeatures.key("mars_desh_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MARS_IRON_ORE = GalacticraftFeatures.key("mars_iron_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MARS_PACKED_ICE_PATCH = GalacticraftFeatures.key("mars_packed_ice_patch");

    static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> cxt) {

    }
}
