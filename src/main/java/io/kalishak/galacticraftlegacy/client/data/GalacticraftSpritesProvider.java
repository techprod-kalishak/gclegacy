/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.data;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GalacticraftSpritesProvider extends SpriteSourceProvider {
    public static final Identifier CELESTIAL_BODIES = Constants.id("celestial_bodies");
    public static final Identifier SCHEMATICS = Constants.id("schematics");
    public static final Identifier PARACHUTES = Constants.id("parachutes");

    public GalacticraftSpritesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Galacticraft.MODID);
    }

    @Override
    protected void gather() {
        atlas(AtlasIds.ARMOR_TRIMS).addSource(new PalettedPermutations(
                List.of(),
                Identifier.withDefaultNamespace("trims/color_palettes/trim_palette"),
                Map.of(
                        "steel", Constants.id("trims/color_palettes/steel"),
                        "cheese", Constants.id("trims/color_palettes/cheese"),
                        "desh", Constants.id("trims/color_palettes/desh"),
                        "titanium", Constants.id("trims/color_palettes/titanium")
                ),
                PalettedPermutations.DEFAULT_SEPARATOR
        ));
        atlas(AtlasIds.CHESTS).addSource(new SingleFile(Constants.id("parachest")));
        atlas(AtlasIds.GUI).addSource(new DirectoryLister("gui/sprites", ""));
        atlas(CELESTIAL_BODIES).addSource(new DirectoryLister("galaxy", ""));
        atlas(PARACHUTES).addSource(new DirectoryLister("entity/equipment/galacticraftlegacy/parachute", ""));
        atlas(SCHEMATICS).addSource(new DirectoryLister("schematic", ""));
    }
}
