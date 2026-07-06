/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.worldgen;

import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;

public interface GalacticraftCarvers {
    static void bootstrap(BootstrapContext<ConfiguredWorldCarver<?>> cxt) {
        HolderGetter<Block> blocks = cxt.lookup(Registries.BLOCK);
        MoonCarvers.bootstrap(cxt, blocks);
    }

    static ResourceKey<ConfiguredWorldCarver<?>> key(String name) {
        return Constants.key(Registries.CONFIGURED_CARVER, name);
    }
}
