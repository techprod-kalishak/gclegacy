/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.galaxies;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.player.Player;

public interface Celestial {
    Holder<CelestialBodyType> getCelestialBodyType();

    void setOwner(EntityReference<Player> owner);
}
