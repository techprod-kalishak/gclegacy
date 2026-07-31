/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NasaWorkbenchBlockEntity extends BlockEntity {
    public NasaWorkbenchBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(GalacticraftBlockEntityType.NASA_WORKBENCH.get(), worldPosition, blockState);
    }
}
