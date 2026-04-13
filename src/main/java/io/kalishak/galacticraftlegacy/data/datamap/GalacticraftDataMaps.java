package io.kalishak.galacticraftlegacy.data.datamap;

import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

public class GalacticraftDataMaps {
    public static final DataMapType<Block, ExtinguishedWithoutOxygen> EXTINGUISHED_WITHOUT_OXYGEN = DataMapType.builder(
            Constants.id("extinguished_without_oxygen"),
            Registries.BLOCK,
            ExtinguishedWithoutOxygen.CODEC
    ).build();

    public static void registerDataMaps(RegisterDataMapTypesEvent event) {
        event.register(EXTINGUISHED_WITHOUT_OXYGEN);
    }
}
