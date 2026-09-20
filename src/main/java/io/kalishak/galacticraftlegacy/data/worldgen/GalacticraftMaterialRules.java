/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.worldgen;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.levelgen.GalacticraftNoiseRouterData;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.material.VanillaMaterialRules;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.material.MaterialRules;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;
import net.minecraft.world.level.levelgen.material.rule.OreVeinRule;

public class GalacticraftMaterialRules {
    public static final MaterialRule MOON_TURF = MaterialRules.state(GalacticraftBlocks.MOON_TURF.get().defaultBlockState());
    public static final MaterialRule MOON_ROCK = MaterialRules.state(GalacticraftBlocks.MOON_ROCK.get().defaultBlockState());
    public static final MaterialRule MOON_DIRT = MaterialRules.state(GalacticraftBlocks.MOON_DIRT.get().defaultBlockState());
    public static final ResourceKey<MaterialRule> MOON = key("moon");
    public static final ResourceKey<MaterialRule> SURFACE_MOON = key("moon/surface");
    public static final ResourceKey<MaterialRule> UNDERGROUND_MOON = key("moon/underground");
    public static final ResourceKey<MaterialRule> OVERWORLD_ALUMINUM_ORE_VEIN = key("overworld/aluminum_ore_vein");
    public static final ResourceKey<MaterialRule> OVERWORLD_TIN_ORE_VEIN = key("overworld/tin_ore_vein");

    public static void bootstrap(BootstrapContext<MaterialRule> context) {
        HolderGetter<MaterialRule> rules = context.lookup(Registries.MATERIAL_RULE);

        bootstrapOreVeins(context);

        MaterialRule moonSurface = MaterialRules.sequence(
                MaterialRules.ifTrue(
                        MaterialRules.noiseCondition2d(Noises.CONTINENTALNESS, 2.0D),
                        MOON_TURF
                ),
                MOON_DIRT
        );
        context.register(SURFACE_MOON, moonSurface);
        MaterialRule moonUnderground = MaterialRules.registerAndWrap(
                context,
                UNDERGROUND_MOON,
                MaterialRules.sequence(
                        MaterialRules.ifTrue(
                                MaterialRules.verticalGradient(
                                        "moon_rock",
                                        VerticalAnchor.belowTop(3),
                                        VerticalAnchor.belowTop(5)
                                ),
                                MOON_ROCK
                        )
                )
        );
        context.register(MOON, createMoon(rules, moonSurface, moonUnderground));

    }

    private static void bootstrapOreVeins(BootstrapContext<MaterialRule> context) {
        HolderGetter<DensityFunction> functions = context.lookup(Registries.DENSITY_FUNCTION);
        DensityFunction richness = NoiseRouterData.getFunction(functions, NoiseRouterData.ORE_VEIN_RICHNESS);
        DensityFunction gap = NoiseRouterData.getFunction(functions, NoiseRouterData.ORE_VEIN_GAP);

        context.register(
                OVERWORLD_ALUMINUM_ORE_VEIN,
                new OreVeinRule(
                        GalacticraftBlocks.ALUMINUM_ORE.get().defaultBlockState(),
                        GalacticraftBlocks.RAW_ALUMINUM_BLOCK.get().defaultBlockState(),
                        Blocks.DIORITE.defaultBlockState(),
                        0.02F,
                        NoiseRouterData.getFunction(functions, GalacticraftNoiseRouterData.OVERWORLD_ORE_VEIN_ALUMINUM_DENSITY),
                        richness,
                        gap
                )
        );
        context.register(
                OVERWORLD_TIN_ORE_VEIN,
                new OreVeinRule(
                        GalacticraftBlocks.TIN_ORE.get().defaultBlockState(),
                        GalacticraftBlocks.RAW_TIN_BLOCK.get().defaultBlockState(),
                        Blocks.ANDESITE.defaultBlockState(),
                        0.02F,
                        NoiseRouterData.getFunction(functions, GalacticraftNoiseRouterData.OVERWORLD_ORE_VEIN_TIN_DENSITY),
                        richness,
                        gap
                )
        );
    }

    public static MaterialRule createMoon(HolderGetter<MaterialRule> rules, MaterialRule surface, MaterialRule underground) {
        return MaterialRules.sequence(
                MaterialRules.getRule(rules, VanillaMaterialRules.BEDROCK_FLOOR),
                MaterialRules.ifTrue(MaterialRules.abovePreliminarySurface(), surface),
                underground
        );
    }

    public static Holder<MaterialRule> empty() {
        return Holder.direct(MaterialRules.state(Blocks.AIR.defaultBlockState()));
    }

    private static ResourceKey<MaterialRule> key(String name) {
        return Constants.key(Registries.MATERIAL_RULE, name);
    }
}
