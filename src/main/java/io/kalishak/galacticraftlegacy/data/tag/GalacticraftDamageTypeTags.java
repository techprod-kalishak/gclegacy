/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.damagesource.GalacticraftDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class GalacticraftDamageTypeTags extends TagsProvider<DamageType> {
    public GalacticraftDamageTypeTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, Registries.DAMAGE_TYPE, lookupProvider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(GalacticraftTags.DamageTypes.BYPASSES_SHIELD_CONTROLLER)
                .addOptional(GalacticraftDamageTypes.SUFFOCATION)
                .addOptional(GalacticraftDamageTypes.SUN_RADIATION);
    }
}
