/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.timeline;

import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.clock.WorldClock;

public interface GalacticraftWorldClocks {
    ResourceKey<WorldClock> MOON = Constants.key(Registries.WORLD_CLOCK, "moon");
    ResourceKey<WorldClock> MARS = Constants.key(Registries.WORLD_CLOCK, "mars");
    ResourceKey<WorldClock> VENUS = Constants.key(Registries.WORLD_CLOCK, "venus");
    ResourceKey<WorldClock> ASTEROIDS = Constants.key(Registries.WORLD_CLOCK, "asteroids");

    static void bootstrap(BootstrapContext<WorldClock> cxt) {
        register(cxt, MOON);
        register(cxt, MARS);
        register(cxt, VENUS);
        register(cxt, ASTEROIDS);
    }

    private static void register(BootstrapContext<WorldClock> cxt, ResourceKey<WorldClock> key) {
        cxt.register(key, new WorldClock());
    }
}
