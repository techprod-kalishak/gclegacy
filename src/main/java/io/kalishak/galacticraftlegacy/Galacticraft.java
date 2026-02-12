package io.kalishak.galacticraftlegacy;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.data.GalacticraftData;
import io.kalishak.galacticraftlegacy.network.GalacticraftNetworkHandler;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.space.CelestialBodyType;
import io.kalishak.galacticraftlegacy.world.attribute.GalacticraftAttributeTypes;
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
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftDispenserBehaviors;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluidType;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

@Mod(Galacticraft.MODID)
public class Galacticraft {
    public static final String MODID = "galacticraftlegacy";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Galacticraft(IEventBus modEventBus, ModContainer modContainer) {
        GalacticraftAttachments.init(modEventBus);
        GalacticraftAttributeTypes.init(modEventBus);
        GalacticraftBlocks.init(modEventBus);
        GalacticraftBlockEntityType.init(modEventBus);
        CelestialBodyType.init(modEventBus);
        GalacticraftCreativeModeTabs.init(modEventBus);
        GalacticraftDataComponents.init(modEventBus);
        GalacticraftEntityType.init(modEventBus);
        GalacticraftEnvironmentAttributes.init(modEventBus);
        GalacticraftFluidType.init(modEventBus);
        GalacticraftFluids.init(modEventBus);
        GalacticraftItems.init(modEventBus);
        GalacticraftMenuType.init(modEventBus);
        GalacticraftRecipeBookCategories.init(modEventBus);
        GalacticraftRecipeDisplay.init(modEventBus);
        GalacticraftRecipeSerializer.init(modEventBus);
        GalacticraftRecipeType.init(modEventBus);

        modEventBus.addListener(this::setup);

        modEventBus.addListener(GalacticraftData::gatherData);
        modEventBus.addListener(GalacticraftNetworkHandler::registerPackets);
        modEventBus.register(GalacticraftRegistries.class);

        NeoForge.EVENT_BUS.register(new NeoEventHandler());
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }

    private void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(GalacticraftDispenserBehaviors::registerDispenseBehaviors);
    }
}
