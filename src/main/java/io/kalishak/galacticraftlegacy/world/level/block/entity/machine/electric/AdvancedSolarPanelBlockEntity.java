/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine.electric;

import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedSolarPanelBlockEntity extends AbstractSolarPanelBlockEntity {
    public AdvancedSolarPanelBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityTypes.ADVANCED_SOLAR_PANEL.get(), pos, blockState);
    }

    @Override
    public Properties getProperties() {
        return Properties.of();
    }
}
