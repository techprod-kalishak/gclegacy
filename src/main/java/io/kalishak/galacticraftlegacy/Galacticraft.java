package io.kalishak.galacticraftlegacy;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.data.GalacticraftData;
import io.kalishak.galacticraftlegacy.network.GalacticraftNetworkHandler;
import io.kalishak.galacticraftlegacy.space.CelestialBodyType;
import io.kalishak.galacticraftlegacy.world.inventory.GalacticraftMenuType;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftCreativeModeTabs;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluidType;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
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
        GalacticraftBlocks.init(modEventBus);
        GalacticraftBlockEntityType.init(modEventBus);
        CelestialBodyType.init(modEventBus);
        GalacticraftCreativeModeTabs.init(modEventBus);
        GalacticraftDataComponents.init(modEventBus);
        GalacticraftFluidType.init(modEventBus);
        GalacticraftFluids.init(modEventBus);
        GalacticraftItems.init(modEventBus);
        GalacticraftMenuType.init(modEventBus);

        modEventBus.addListener(this::modifyDefaultComponents);
        modEventBus.addListener(GalacticraftData::gatherData);
        modEventBus.addListener(GalacticraftNetworkHandler::registerPackets);

        NeoForge.EVENT_BUS.register(new NeoEventHandler());
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static Identifier id(String assetName) {
        return Identifier.fromNamespaceAndPath(MODID, assetName);
    }

    public static <R> ResourceKey<R> key(ResourceKey<? extends Registry<R>> registryKey, String name) {
        return ResourceKey.create(registryKey, id(name));
    }

    public void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        event.modifyMatching(
                item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(MODID),
                builder -> builder.set(DataComponents.RARITY, EnumExtensions.RARITY_GALAXY.getValue())
        );
    }
}
