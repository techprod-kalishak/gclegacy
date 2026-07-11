/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.references.GalacticraftEntityIds;
import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypeIds;

import java.util.concurrent.CompletableFuture;

public class GalacticraftEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public GalacticraftEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(GalacticraftTags.EntityTypes.BYPASSES_CELESTIAL_GRAVITY)
                .add(EntityTypeIds.SULFUR_CUBE);
        tag(GalacticraftTags.EntityTypes.CAN_EQUIP_PARACHUTE)
                .add(EntityTypeIds.PLAYER);
        tag(GalacticraftTags.EntityTypes.CAN_EQUIP_GEAR)
                .add(EntityTypeIds.CREEPER)
                .add(EntityTypeIds.PLAYER)
                .add(EntityTypeIds.SKELETON)
                .add(EntityTypeIds.WOLF)
                .add(EntityTypeIds.ZOMBIE);
        tag(EntityTypeTags.SKELETONS)
                .add(GalacticraftEntityIds.EVOLVED_SKELETON);
        tag(EntityTypeTags.ZOMBIES)
                .add(GalacticraftEntityIds.EVOLVED_ZOMBIE);
        tag(GalacticraftTags.EntityTypes.SPACE_MOB)
                .add(GalacticraftEntityIds.EVOLVED_SKELETON)
                .add(GalacticraftEntityIds.EVOLVED_ZOMBIE);
    }
}
