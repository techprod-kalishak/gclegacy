/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class GalacticraftEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public GalacticraftEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(GalacticraftTags.EntityTypes.CAN_EQUIP_PARACHUTE)
                .add(EntityType.PLAYER);
        tag(GalacticraftTags.EntityTypes.CAN_EQUIP_GEAR)
                .add(EntityType.CREEPER)
                .add(EntityType.PLAYER)
                .add(EntityType.SKELETON)
                .add(EntityType.WOLF)
                .add(EntityType.ZOMBIE);
        tag(EntityTypeTags.SKELETONS)
                .add(GalacticraftEntityType.EVOLVED_SKELETON.get());
        tag(EntityTypeTags.ZOMBIES)
                .add(GalacticraftEntityType.EVOLVED_ZOMBIE.get());
    }
}
