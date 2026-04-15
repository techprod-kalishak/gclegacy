/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.galaxies;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiPredicate;

public abstract class CelestialObject implements Celestial {
    public static final Codec<Holder<CelestialObject>> CODEC = RegistryFixedCodec.create(GalacticraftRegistries.Keys.CELESTIAL_OBJECT);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<CelestialObject>> STREAM_CODEC = ByteBufCodecs.holderRegistry(GalacticraftRegistries.Keys.CELESTIAL_OBJECT);
    public static final BiPredicate<CelestialObject, CelestialObject> SAME_OWNER = (main, secondary) -> main.getOwner() != null && secondary.getOwner() != null && main.getOwner().getUUID().equals(secondary.getOwner().getUUID());
    protected final Holder<CelestialBodyType> celestialBodyType;
    public EntityReference<Player> owner;

    protected CelestialObject(Holder<CelestialBodyType> celestialBodyType) {
        this.celestialBodyType = celestialBodyType;
    }

    protected abstract ResourceKey<CelestialObject> id();

    @Override
    public Holder<CelestialBodyType> getCelestialBodyType() {
        return this.celestialBodyType;
    }

    public EntityReference<Player> getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(EntityReference<Player> owner) {
        this.owner = owner;
    }

    public String getDescriptionId() {
        return id().identifier().toLanguageKey(this.celestialBodyType.getRegisteredName());
    }

    public static boolean isOwnedByPlayer(CelestialObject celestialObject, Player player) {
        return celestialObject.getOwner() != null && celestialObject.getOwner().matches(player);
    }
}
