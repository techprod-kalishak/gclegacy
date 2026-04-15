/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.datamap;

import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.ClientNeoForgeMod;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import java.util.concurrent.CompletableFuture;

public class GalacticraftDataMaps {
    public static final DataMapType<Block, ExtinguishedWithoutOxygen> EXTINGUISHED_WITHOUT_OXYGEN = DataMapType.builder(
            Constants.id("extinguished_without_oxygen"),
            Registries.BLOCK,
            ExtinguishedWithoutOxygen.CODEC
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

        }
    }
}
