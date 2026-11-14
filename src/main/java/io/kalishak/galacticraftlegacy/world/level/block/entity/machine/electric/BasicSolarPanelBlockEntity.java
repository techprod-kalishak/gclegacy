/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine.electric;

import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityTypes;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.MachineInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BasicSolarPanelBlockEntity extends AbstractSolarPanelBlockEntity {
    private static final MachineInstance.Properties PROPERTIES = Properties.of()
            .energyExtractionRate(200);

    public BasicSolarPanelBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityTypes.BASIC_SOLAR_PANEL.get(), pos, blockState);
    }

    @Override
    public Properties getProperties() {
        return PROPERTIES;
    }
}
