/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.component.ChecklistEntry;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.registry.BuiltinChecklist;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class GalacticraftChecklistTagsProvider extends TagsProvider<ChecklistEntry> {
    public GalacticraftChecklistTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, GalacticraftRegistries.Keys.CHECKLIST, registries, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(GalacticraftTags.Checklist.OVERWORLD_CHECKLIST)
                .add(BuiltinChecklist.EQUIP_PARACHUTE);
        tag(GalacticraftTags.Checklist.MOON_CHECKLIST)
                .add(BuiltinChecklist.EQUIP_OXYGEN_SUIT);
        tag(GalacticraftTags.Checklist.SATELLITE_CHECKLIST)
                .add(BuiltinChecklist.EQUIP_OXYGEN_SUIT)
                .add(BuiltinChecklist.EQUIP_GRAPPLING_HOOK);
        tag(GalacticraftTags.Checklist.MARS_CHECKLIST)
                .add(BuiltinChecklist.EQUIP_OXYGEN_SUIT)
                .add(BuiltinChecklist.EQUIP_THERMAL_PADDING);
        tag(GalacticraftTags.Checklist.ASTEROIDS_CHECKLIST)
                .add(BuiltinChecklist.EQUIP_OXYGEN_SUIT)
                .add(BuiltinChecklist.EQUIP_THERMAL_PADDING)
                .add(BuiltinChecklist.EQUIP_GRAPPLING_HOOK);
        tag(GalacticraftTags.Checklist.VENUS_CHECKLIST)
                .add(BuiltinChecklist.EQUIP_OXYGEN_SUIT)
                .add(BuiltinChecklist.EQUIP_ISOTHERMAL_PADDING)
                .add(BuiltinChecklist.EQUIP_SHIELD_CONTROLLER);
    }
}
