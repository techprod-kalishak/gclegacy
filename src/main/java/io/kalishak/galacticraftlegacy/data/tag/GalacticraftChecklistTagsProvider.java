package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.registry.Checklist;
import io.kalishak.galacticraftlegacy.registry.ChecklistEntry;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;

import java.util.concurrent.CompletableFuture;

public class GalacticraftChecklistTagsProvider extends KeyTagProvider<ChecklistEntry> {
    public GalacticraftChecklistTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, GalacticraftRegistries.Keys.CHECKLIST, registries, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(GalacticraftTags.Checklist.OVERWORLD_CHECKLIST)
                .add(Checklist.EQUIP_PARACHUTE);
        tag(GalacticraftTags.Checklist.MOON_CHECKLIST)
                .add(Checklist.EQUIP_OXYGEN_SUIT);
        tag(GalacticraftTags.Checklist.SATELLITE_CHECKLIST)
                .add(Checklist.EQUIP_OXYGEN_SUIT)
                .add(Checklist.EQUIP_GRAPPLING_HOOK);
        tag(GalacticraftTags.Checklist.MARS_CHECKLIST)
                .add(Checklist.EQUIP_OXYGEN_SUIT)
                .add(Checklist.EQUIP_THERMAL_PADDING);
        tag(GalacticraftTags.Checklist.ASTEROIDS_CHECKLIST)
                .add(Checklist.EQUIP_OXYGEN_SUIT)
                .add(Checklist.EQUIP_THERMAL_PADDING)
                .add(Checklist.EQUIP_GRAPPLING_HOOK);
        tag(GalacticraftTags.Checklist.VENUS_CHECKLIST)
                .add(Checklist.EQUIP_OXYGEN_SUIT)
                .add(Checklist.EQUIP_ISOTHERMAL_PADDING)
                .add(Checklist.EQUIP_SHIELD_CONTROLLER);
    }
}
