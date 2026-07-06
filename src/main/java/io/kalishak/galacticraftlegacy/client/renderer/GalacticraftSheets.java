/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer;

import com.google.common.collect.ImmutableList;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SpriteMapper;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.DyeColor;

import java.util.*;

public class GalacticraftSheets {
    public static final Identifier PARACHUTE_SHEET = Constants.id("textures/atlas/parachutes.png");
    public static final Identifier CELESTIAL_BODY_SHEET = Constants.id("textures/atlas/celestial_bodies.png");
    public static final Identifier SCHEMATIC_SHEET = Constants.id("textures/atlas/schematics.png");
    public static final SpriteId PARACHEST = Sheets.CHEST_MAPPER.apply(Constants.id("parachest"));
    public static final SpriteMapper PARACHUTE_MAPPER = new SpriteMapper(PARACHUTE_SHEET, "entity/equipment/galacticraftlegacy/parachute");
    public static final List<SpriteId> PARACHUTE_TEXTURE_LOCATION = Arrays.stream(DyeColor.values())
            .sorted(Comparator.comparingInt(DyeColor::getId))
            .map(GalacticraftSheets::createParachuteMaterial)
            .collect(ImmutableList.toImmutableList());
    public static final Map<FeatureTier, SpriteId> DUNGEON_CHESTS = Util.makeEnumMap(FeatureTier.class, featureTier -> Sheets.CHEST_MAPPER.apply(Constants.id(featureTier.getCelestialBodyName() + "_dungeon_chest")));

    public static SpriteId getParachuteMaterial(DyeColor color) {
        return PARACHUTE_TEXTURE_LOCATION.get(color.getId());
    }

    public static SpriteId createParachuteMaterial(DyeColor color) {
        return PARACHUTE_MAPPER.apply(Constants.id( color.getName()));
    }

    public static SpriteId getDungeonChestMaterial(FeatureTier featureTier) {
        return DUNGEON_CHESTS.get(featureTier);
    }
}
