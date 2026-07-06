/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.datamap;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.deferred.DeferredWeatheringCopperBlocks;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable;

import java.util.concurrent.CompletableFuture;

public class GalacticraftDataMaps {
    public static final DataMapType<Block, Extinguishable> EXTINGUISHED_WITHOUT_OXYGEN = DataMapType.builder(
            Constants.id("extinguished_without_oxygen"),
            Registries.BLOCK,
            Extinguishable.CODEC
    ).build();

    public static void registerDataMaps(RegisterDataMapTypesEvent event) {
        event.register(EXTINGUISHED_WITHOUT_OXYGEN);
    }

    public static class Provider extends DataMapProvider {
        public Provider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(packOutput, lookupProvider);
        }

        @Override
        protected void gather(HolderLookup.Provider provider) {
            buildOxidizables(builder(NeoForgeDataMaps.OXIDIZABLES), GalacticraftBlocks.UNLIT_COPPER_LANTERN);
            Builder<Extinguishable, Block> extinguishableBuilder = builder(EXTINGUISHED_WITHOUT_OXYGEN);
            buildExtinguishable(extinguishableBuilder, Blocks.JACK_O_LANTERN, Blocks.CARVED_PUMPKIN);
            buildExtinguishable(extinguishableBuilder, Blocks.TORCH, GalacticraftBlocks.UNLIT_TORCH.get());
            buildExtinguishable(extinguishableBuilder, Blocks.WALL_TORCH, GalacticraftBlocks.UNLIT_WALL_TORCH.get());
            buildExtinguishable(extinguishableBuilder, Blocks.COPPER_TORCH, GalacticraftBlocks.UNLIT_COPPER_TORCH.get());
            buildExtinguishable(extinguishableBuilder, Blocks.COPPER_WALL_TORCH, GalacticraftBlocks.UNLIT_COPPER_WALL_TORCH.get());
            buildExtinguishable(extinguishableBuilder, Blocks.LANTERN, GalacticraftBlocks.UNLIT_LANTERN.get());
            buildExtinguishable(extinguishableBuilder, Blocks.COPPER_LANTERN.unaffected(), GalacticraftBlocks.UNLIT_COPPER_LANTERN.unaffected().get());
            buildExtinguishable(extinguishableBuilder, Blocks.COPPER_LANTERN.exposed(), GalacticraftBlocks.UNLIT_COPPER_LANTERN.exposed().get());
            buildExtinguishable(extinguishableBuilder, Blocks.COPPER_LANTERN.weathered(), GalacticraftBlocks.UNLIT_COPPER_LANTERN.weathered().get());
            buildExtinguishable(extinguishableBuilder, Blocks.COPPER_LANTERN.oxidized(), GalacticraftBlocks.UNLIT_COPPER_LANTERN.oxidized().get());
            buildExtinguishable(extinguishableBuilder, Blocks.COPPER_LANTERN.waxed(), GalacticraftBlocks.UNLIT_COPPER_LANTERN.waxed().get());
            buildExtinguishable(extinguishableBuilder, Blocks.COPPER_LANTERN.waxedExposed(), GalacticraftBlocks.UNLIT_COPPER_LANTERN.waxedExposed().get());
            buildExtinguishable(extinguishableBuilder, Blocks.COPPER_LANTERN.waxedWeathered(), GalacticraftBlocks.UNLIT_COPPER_LANTERN.waxedWeathered().get());
            buildExtinguishable(extinguishableBuilder, Blocks.COPPER_LANTERN.waxedOxidized(), GalacticraftBlocks.UNLIT_COPPER_LANTERN.waxedOxidized().get());
        }

        private static void buildOxidizables(Builder<Oxidizable, Block> dataMapBuilder, DeferredWeatheringCopperBlocks blocks) {
            blocks.weatheringMapping().forEach((previous, next) -> dataMapBuilder.add(previous, new Oxidizable(next.value()), false));
        }

        private static void buildExtinguishable(Builder<Extinguishable, Block> dataMapBuilder, Block litBlock, Block unlitBlock) {
            dataMapBuilder.add(getBlockId(litBlock), new Extinguishable(unlitBlock.defaultBlockState(), litBlock.defaultBlockState().hasProperty(BlockStateProperties.HORIZONTAL_FACING)), false);
        }

        private static Identifier getBlockId(Block block) {
            return BuiltInRegistries.BLOCK.getKey(block);
        }
    }
}
