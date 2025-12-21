package io.kalishak.galacticraftlegacy;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.data.GalacticraftLegacyData;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftDataComponents;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

@Mod(GalacticraftLegacy.MODID)
public class GalacticraftLegacy {
    public static final String MODID = "galacticraftlegacy";
    public static final Logger LOGGER = LogUtils.getLogger();

    public GalacticraftLegacy(IEventBus modEventBus, ModContainer modContainer) {
        GalacticraftAttachments.init(modEventBus);
        GalacticraftDataComponents.init(modEventBus);

        modEventBus.addListener(GalacticraftLegacyData::gatherData);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
