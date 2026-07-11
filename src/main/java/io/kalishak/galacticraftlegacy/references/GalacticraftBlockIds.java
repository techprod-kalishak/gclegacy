/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.references;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public class GalacticraftBlockIds {
    public static final ResourceKey<Block> EMPTY_AIR = create("empty_air");
    public static final ResourceKey<Block> OXYGEN_AIR = create("oxygen_air");

    private static ResourceKey<Block> create(String name) {
        return Constants.key(Registries.BLOCK, name);
    }
}
