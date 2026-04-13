package io.kalishak.galacticraftlegacy.galaxies;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.player.Player;

public interface Celestial {
    Holder<CelestialBodyType> getCelestialBodyType();

    void setOwner(EntityReference<Player> owner);
}
