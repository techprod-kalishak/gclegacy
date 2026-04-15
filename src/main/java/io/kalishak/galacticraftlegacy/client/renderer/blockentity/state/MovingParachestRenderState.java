/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.blockentity.state;

import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;

public class MovingParachestRenderState extends MovingBlockRenderState {
    public @Nullable BlockEntity blockEntity;

    @Override
    public @Nullable BlockEntity getBlockEntity(BlockPos pos) {
        return pos.equals(this.blockPos) ? this.blockEntity : null;
    }
}
