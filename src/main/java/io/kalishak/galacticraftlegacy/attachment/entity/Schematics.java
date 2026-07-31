/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.attachment.entity;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.registry.SchematicVariants;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.VehicleCraftingPage;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.VehicleCraftingPages;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceTeam;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;

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

        this.shouldSort = true;
        return this.unlockedSchematics.add(key);
    }

    public boolean remove(ResourceKey<SchematicVariant> key) {
        if (!isUnlocked(key)) {
            return false;
        }

        this.shouldSort = true;
        return this.unlockedSchematics.remove(key);
    }

    public void updateSpaceRaceTeam(SpaceRaceTeam spaceRaceTeam) {
        for (ResourceKey<SchematicVariant> schematicKey : this.unlockedSchematics) {
            spaceRaceTeam.getUnlockedSchematics().unlock(schematicKey);
        }
    }

    public boolean isUnlocked(ResourceKey<SchematicVariant> schematicId) {
        return schematicId.equals(SchematicVariants.TIER_1_ROCKET) || this.unlockedSchematics.contains(schematicId);
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

    public boolean canPickAnother(@Nullable ResourceKey<SchematicVariant> key, Picker picker) {
        return key == null || picker.pick(this, key).isPresent();
    }

    public @Nullable ResourceKey<SchematicVariant> getNext(ResourceKey<SchematicVariant> current) {
        int indexOf = indexOf(current);

        return indexOf < unlockedCount() - 1 ? this.unlockedSchematics.get(indexOf + 1) : null;
    }

    public @Nullable ResourceKey<SchematicVariant> getPrevious(ResourceKey<SchematicVariant> current) {
        int indexOf = indexOf(current);

        if (indexOf < 1) {
            return SchematicVariants.TIER_1_ROCKET;
        }

        return this.unlockedSchematics.get(indexOf - 1);
    }

    private int indexOf(ResourceKey<SchematicVariant> key) {
        return key.equals(SchematicVariants.TIER_1_ROCKET) ? -1 : this.unlockedSchematics.indexOf(key);
    }

    private static void sort(RegistryAccess registryAccess, Schematics schematics) {
        if (schematics.shouldSort) {
            schematics.unlockedSchematics.sort(Comparator.comparingInt(key -> registryAccess.get(key).map(holder -> holder.value().orderIndex()).orElse(-1)));
            schematics.shouldSort = false;
        }
    }

    public static @Nullable Holder<VehicleCraftingPage> getPage(Player player, ResourceKey<SchematicVariant> currentSchematic, Picker picker) {
        Schematics playerSchematics = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA).getSchematics();

        if (!playerSchematics.canPickAnother(currentSchematic, picker)) {
            return null;
        }

        sort(player.registryAccess(), playerSchematics);
        Optional<ResourceKey<SchematicVariant>> next = picker.pick(playerSchematics, currentSchematic);

        return next
                .flatMap(
                        key -> player.registryAccess()
                                .lookupOrThrow(GalacticraftRegistries.Keys.VEHICLE_CRAFTING_PAGE)
                                .get(VehicleCraftingPages.fromSchematic(key))
                )
                .orElse(null);
    }

    public static @Nullable Holder<VehicleCraftingPage> getLastPage(Player player) {
        Schematics playerSchematics = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA).getSchematics();

        return playerSchematics.isEmpty() ? null : player.registryAccess().get(VehicleCraftingPages.fromSchematic(playerSchematics.unlockedSchematics.getLast())).map(Holder.Reference::getDelegate).orElseThrow();
    }

    public enum Picker {
        NEXT(Schematics::getNext),
        PREVIOUS(Schematics::getPrevious);

        public static final StreamCodec<RegistryFriendlyByteBuf, Picker> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Picker.class);

        private final BiFunction<Schematics, ResourceKey<SchematicVariant>, @Nullable ResourceKey<SchematicVariant>> pickFactory;

        Picker(BiFunction<Schematics, ResourceKey<SchematicVariant>, @Nullable ResourceKey<SchematicVariant>> pickFactory) {
            this.pickFactory = pickFactory;
        }

        public Optional<ResourceKey<SchematicVariant>> pick(Schematics schematics, ResourceKey<SchematicVariant> currentSchematic) {
            return Optional.ofNullable(this.pickFactory.apply(schematics, currentSchematic));
        }
    }
}
