/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.registry;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import net.minecraft.ChatFormatting;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;

import java.util.HashSet;
import java.util.Set;

public class SchematicVariants {
    private static final Set<ResourceKey<SchematicVariant>> VARIANTS = new HashSet<>(5);

    public static final ResourceKey<SchematicVariant> TIER_1_ROCKET = Constants.key(GalacticraftRegistries.Keys.SCHEMATIC, "tier_1_rocket");
    public static final ResourceKey<SchematicVariant> MOON_BUGGY = Constants.key(GalacticraftRegistries.Keys.SCHEMATIC, "moon_buggy");
    public static final ResourceKey<SchematicVariant> TIER_2_ROCKET = Constants.key(GalacticraftRegistries.Keys.SCHEMATIC, "tier_2_rocket");
    public static final ResourceKey<SchematicVariant> CARGO_ROCKET = Constants.key(GalacticraftRegistries.Keys.SCHEMATIC, "cargo_rocket");
    public static final ResourceKey<SchematicVariant> TIER_3_ROCKET = Constants.key(GalacticraftRegistries.Keys.SCHEMATIC, "tier_3_rocket");
    public static final ResourceKey<SchematicVariant> ASTRO_MINER = Constants.key(GalacticraftRegistries.Keys.SCHEMATIC, "astro_miner");

    public static void bootstrap(BootstrapContext<SchematicVariant> cxt) {
        simple(cxt, 1, FeatureTier.TIER_1, MOON_BUGGY);
        simple(cxt, 2, FeatureTier.TIER_1, TIER_2_ROCKET);
        simple(cxt, 3, FeatureTier.TIER_2, CARGO_ROCKET);
        simple(cxt, 4, FeatureTier.TIER_2, TIER_3_ROCKET);
        simple(cxt, 5, FeatureTier.TIER_3, ASTRO_MINER);
    }

    private static void simple(BootstrapContext<SchematicVariant> cxt, int index, FeatureTier tier, ResourceKey<SchematicVariant> schematicId) {
        cxt.register(
                schematicId,
                new SchematicVariant(
                        index,
                        tier,
                        schematicId.identifier(),
                        Component.translatable(Constants.translatable(schematicId, "title")).withStyle(ChatFormatting.YELLOW)
                )
        );
        VARIANTS.add(schematicId);
    }

    public static Set<ResourceKey<SchematicVariant>> getVariants() {
        return VARIANTS;
    }
}
