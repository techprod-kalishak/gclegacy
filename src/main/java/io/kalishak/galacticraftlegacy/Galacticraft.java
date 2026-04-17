/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy;

import io.kalishak.galacticraftlegacy.advancements.GalacticraftCriteriaTriggers;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.config.ClientConfig;
import io.kalishak.galacticraftlegacy.config.CommonConfig;
import io.kalishak.galacticraftlegacy.config.ServerConfig;
import io.kalishak.galacticraftlegacy.data.GalacticraftData;
import io.kalishak.galacticraftlegacy.data.datamap.GalacticraftDataMaps;
import io.kalishak.galacticraftlegacy.galaxies.GalacticraftGalaxies;
import io.kalishak.galacticraftlegacy.network.GalacticraftNetworkHandler;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.server.commands.GalacticraftCommands;
import io.kalishak.galacticraftlegacy.galaxies.CelestialBodyType;
import io.kalishak.galacticraftlegacy.sounds.GalacticraftSounds;
import io.kalishak.galacticraftlegacy.world.attribute.GalacticraftEnvironmentAttributes;
import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityType;
import io.kalishak.galacticraftlegacy.world.inventory.GalacticraftMenuType;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftCreativeModeTabs;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.crafting.GalacticraftRecipeBookCategories;
import io.kalishak.galacticraftlegacy.world.item.crafting.display.GalacticraftRecipeDisplay;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeSerializer;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import io.kalishak.galacticraftlegacy.world.level.GalacticraftParticleTypes;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftDispenserBehaviors;
import io.kalishak.galacticraftlegacy.world.level.block.cauldron.GalacticraftCauldronInteraction;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.levelgen.features.GalacticraftFeatures;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluidType;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import io.kalishak.galacticraftlegacy.world.score.race.SpaceRaceHooks;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.fluids.RegisterCauldronFluidContentEvent;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

@Mod(Galacticraft.MODID)
public class Galacticraft {
    public static final String MODID = "galacticraftlegacy";

    public Galacticraft(IEventBus modEventBus, ModContainer modContainer) {
        GalacticraftAttachments.init(modEventBus);
        GalacticraftBlocks.init(modEventBus);
        GalacticraftBlockEntityType.init(modEventBus);
        CelestialBodyType.init(modEventBus);
        GalacticraftCreativeModeTabs.init(modEventBus);
        GalacticraftCriteriaTriggers.init(modEventBus);
        GalacticraftDataComponents.init(modEventBus);
        GalacticraftEntityType.init(modEventBus);
        GalacticraftEnvironmentAttributes.init(modEventBus);
        GalacticraftFeatures.init(modEventBus);
        GalacticraftFluidType.init(modEventBus);
        GalacticraftFluids.init(modEventBus);
        GalacticraftItems.init(modEventBus);
        GalacticraftMenuType.init(modEventBus);
        GalacticraftParticleTypes.init(modEventBus);
        GalacticraftRecipeBookCategories.init(modEventBus);
        GalacticraftRecipeDisplay.init(modEventBus);
        GalacticraftRecipeSerializer.init(modEventBus);
        GalacticraftRecipeType.init(modEventBus);
        GalacticraftSounds.init(modEventBus);

        GalacticraftGalaxies.init(modEventBus);

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::registerCauldronFluids);

        GalacticraftCauldronInteraction.init(modEventBus);
        modEventBus.addListener(GalacticraftData::gatherData);
        modEventBus.addListener(GalacticraftNetworkHandler::registerPackets);
        modEventBus.addListener(GalacticraftDataMaps::registerDataMaps);
        modEventBus.register(GalacticraftRegistries.class);

        NeoForge.EVENT_BUS.register(new NeoEventHandler());
        NeoForge.EVENT_BUS.register(SpaceRaceHooks.class);
        NeoForge.EVENT_BUS.addListener(GalacticraftCommands::registerCommands);
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }

    private void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(GalacticraftDispenserBehaviors::registerDispenseBehaviors);
    }

    private void registerCauldronFluids(RegisterCauldronFluidContentEvent event) {
        event.register(GalacticraftBlocks.OIL_CAULDRON.get(), GalacticraftFluids.OIL.get(), 1000, null);
        event.register(GalacticraftBlocks.FUEL_CAULDRON.get(), GalacticraftFluids.FUEL.get(), 1000, null);
    }
}
