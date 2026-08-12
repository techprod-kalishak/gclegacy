/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.world.level.block.MultipartBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public abstract class MultipartBlockEntity extends BlockEntity {
    protected @Nullable BlockPos coreBlockPosition;

    public MultipartBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
        this.coreBlockPosition = worldPosition;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.storeNullable("CoreBlockPosition", BlockPos.CODEC, this.coreBlockPosition);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.read("CoreBlockPosition", BlockPos.CODEC).ifPresent(this::setCoreBlockPosition);
    }

    public void onBlockRemoval() {
        if (this.coreBlockPosition != null) {
            BlockEntity blockEntity = this.level.getBlockEntity(this.coreBlockPosition);

            if (blockEntity instanceof MultipartBlock multipartBlock) {
                multipartBlock.onRemoval(this);
            }
        }
    }

    public boolean onBlockActivation(BlockPos pos, Player caller) {
        if (this.coreBlockPosition != null) {
            BlockEntity blockEntity = this.level.getBlockEntity(this.coreBlockPosition);

            if (blockEntity instanceof MultipartBlock multipartBlock) {
                return multipartBlock.onActivation(caller);
            }
        }

        return false;
    }

    protected void setCoreBlockPosition(@Nullable BlockPos coreBlockPosition) {
        this.coreBlockPosition = coreBlockPosition;
    }

    protected @Nullable BlockPos getCoreBlockPosition() {
        return this.coreBlockPosition;
    }
}
