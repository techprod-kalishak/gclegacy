/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.galaxies.environment.CelestialBodyInfo;
import io.kalishak.galacticraftlegacy.world.level.OxygenHelper;
import io.kalishak.galacticraftlegacy.world.level.block.OxygenDetectorBlock;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class OxygenDetectorBlockEntity extends BlockEntity {
    private int searchDelay = 49;
    private final AABB searchBounds;

    public OxygenDetectorBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.OXYGEN_DETECTOR.get(), pos, blockState);

        this.searchBounds = new AABB(
                getBlockPos().getX() - 0.6D,
                getBlockPos().getY() - 0.6D,
                getBlockPos().getZ() - 0.6D,
                getBlockPos().getX() + 1.6D,
                getBlockPos().getY() + 1.6D,
                getBlockPos().getZ() + 1.6D
        );
    }

    public static void serverTick(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, OxygenDetectorBlockEntity blockEntity) {
        if (++blockEntity.searchDelay == 50) {
            blockEntity.searchDelay = 0;
            boolean foundOxygen = false;

            if (!CelestialBodyInfo.canLivingBreath(serverLevel)) {
                if (OxygenHelper.hasOxygen(serverLevel, blockEntity.searchBounds, false)) {
                    blockState.setValue(OxygenDetectorBlock.ACTIVE, true);
                    foundOxygen = true;
                }
            } else {
                for (Direction direction : Direction.values()) {
                    BlockPos relative = blockPos.relative(direction);
                    BlockState relativeState = serverLevel.getBlockState(relative);

                    if (relativeState.is(GalacticraftTags.Blocks.BREATHABLE_AIR)) {
                        foundOxygen = true;
                        break;
                    }
                }
            }

            blockState = blockState.setValue(OxygenDetectorBlock.ACTIVE, foundOxygen);
            serverLevel.setBlock(blockPos, blockState, OxygenDetectorBlock.UPDATE_ALL_IMMEDIATE);
        }
    }
}
