/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.attachment.entity;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.VehicleCraftingPage;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.VehicleCraftingPages;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceTeam;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.VisibleForTesting;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class Schematics {
    public static final Codec<Schematics> CODEC = ResourceKey.codec(GalacticraftRegistries.Keys.SCHEMATIC).listOf().xmap(Schematics::new, schematics -> schematics.unlockedSchematics);
    public static final StreamCodec<ByteBuf, Schematics> STREAM_CODEC = ResourceKey.streamCodec(GalacticraftRegistries.Keys.SCHEMATIC).apply(ByteBufCodecs.list())
            .map(Schematics::new, schematics -> schematics.unlockedSchematics);
    private final List<ResourceKey<SchematicVariant>> unlockedSchematics;
    private boolean shouldSort;

    private Schematics(List<ResourceKey<SchematicVariant>> unlockedSchematics) {
        this.unlockedSchematics = unlockedSchematics;
        this.shouldSort = true;
    }

    public static Schematics empty() {
        return new Schematics(new ArrayList<>());
    }

    public static Schematics copyOf(Schematics schematics) {
        List<ResourceKey<SchematicVariant>> copied = new ArrayList<>(schematics.unlockedCount());
        copied.addAll(schematics.unlockedSchematics);

        return new Schematics(copied);
    }
    public void sync(Schematics other) {
        this.unlockedSchematics.clear();
        this.unlockedSchematics.addAll(other.unlockedSchematics);
    }

    public boolean unlock(ResourceKey<SchematicVariant> key) {
        if (isUnlocked(key)) {
            return false;
        }

        this.unlockedSchematics.add(key);
        this.shouldSort = true;
        return true;
    }

    public boolean remove(ResourceKey<SchematicVariant> key) {
        if (!isUnlocked(key)) {
            return false;
        }

        this.unlockedSchematics.remove(key);
        this.shouldSort = true;
        return true;
    }

    public void schematicUnlockedByTeam(ResourceKey<SchematicVariant> schematicId) {
        unlock(schematicId);
    }

    public void updateSpaceRaceTeam(SpaceRaceTeam spaceRaceTeam) {
        for (ResourceKey<SchematicVariant> schematicKey : this.unlockedSchematics) {
            spaceRaceTeam.getUnlockedSchematics().unlock(schematicKey);
        }
    }

    public boolean isUnlocked(ResourceKey<SchematicVariant> schematicId) {
        return this.unlockedSchematics.stream().anyMatch(id -> id.identifier().equals(schematicId.identifier()));
    }

    public int unlockedCount() {
        return this.unlockedSchematics.size();
    }

    public boolean isEmpty() {
        return this.unlockedSchematics.isEmpty();
    }

    public List<String> asStringList() {
        return this.unlockedSchematics.stream()
                .map(key -> key.identifier().toString())
                .toList();
    }


    public @Nullable ResourceKey<SchematicVariant> getSorted(RegistryAccess registryAccess, int index) {
        Registry<SchematicVariant> schematics = registryAccess.lookupOrThrow(GalacticraftRegistries.Keys.SCHEMATIC);
        int validateCount = schematics.size();
        if (this.shouldSort) {

            this.unlockedSchematics.sort(Comparator.comparingInt(key -> schematics.get(key).map(holder -> holder.value().orderIndex()).orElse(-1)));
            this.shouldSort = false;
        }

        logAllOwned();

        if (index >= validateCount) {
            return null;
        }

        if (index >= unlockedCount()) {
            index = Math.min(index, unlockedCount() - 1);
        }

        return this.unlockedSchematics.get(index);
    }

    public Optional<ResourceKey<SchematicVariant>> getOptionalSorted(RegistryAccess registryAccess, int index) {
        return Optional.ofNullable(getSorted(registryAccess, index));
    }

    @VisibleForTesting
    public void logAllOwned() {
        List<String> keys = new ArrayList<>(this.unlockedSchematics.size());

        for (int i = 0; i < this.unlockedSchematics.size(); i++) {
            keys.add(i + ": " + this.unlockedSchematics.get(i).identifier() + "\n");
        }

        Galacticraft.LOGGER.debug("Owns keys: {}", keys);
    }

    public static @Nullable Holder<VehicleCraftingPage> getPage(Player player, int currentPageIndex, int newPageIndex) {
        Schematics playerSchematics = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA).getSchematics();

        if (playerSchematics.isEmpty() || newPageIndex <= 0 ||  currentPageIndex >= playerSchematics.unlockedCount()) {
            return null;
        }

        Optional<ResourceKey<SchematicVariant>> schematic = playerSchematics.getOptionalSorted(player.registryAccess(), newPageIndex);

        return schematic
                .flatMap(
                        key -> player.registryAccess()
                                .lookupOrThrow(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_PAGE)
                                .get(VehicleCraftingPages.fromSchematic(key))
                )
                .orElse(null);
    }

    public static int getPageNumber(VehicleCraftingPage page) {
        return page.schematic().value().orderIndex();
    }
}
