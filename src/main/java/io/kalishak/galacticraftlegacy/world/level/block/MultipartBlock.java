/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Set;

public interface MultipartBlock {
    /**
     * Called when activated
     * @param player interaction owner
     * @return whether it should be activated
     */
    boolean onActivation(Player player);

    /**
     * Called when this multiblock is created
     * @param level where block was placed in
     * @param placedPosition where the block was placed at
     */
    void onCreation(Level level, BlockPos placedPosition);

    /**
     * Called when one of the multiblocks of this block is destroyed
     * @param caller BlockEntity who called onDestroy function
     */
    void onRemoval(BlockEntity caller);

    /**
     * Update other block entities
     * @param placedPosition main block position of multiblock
     * @param otherPositions children's block entities positions
     */
    void updatePositions(BlockPos placedPosition, Set<BlockPos> otherPositions);
}
