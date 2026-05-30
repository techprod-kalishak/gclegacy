/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.wire;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.WireBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class WireBlock extends AbstractWireBlock {
    public static final MapCodec<WireBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.DOUBLE.fieldOf("size").forGetter(WireBlock::getSize),
            propertiesCodec()
    ).apply(instance, WireBlock::new));

    public WireBlock(double size, Properties properties) {
        super(NetworkType.POWER, size, properties);
    }

    @Override
    protected MapCodec<WireBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WireBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState unlitState, BlockEntityType<T> blockEntityType) {
        if (level instanceof ServerLevel serverLevel) {
            return createTickerHelper(
                    blockEntityType,
                    GalacticraftBlockEntityType.WIRE.get(),
                    (l, wirePos, wireState, wire) -> WireBlockEntity.serverTick(serverLevel, wirePos, wireState, wire)
            );
        }

        return null;
    }
}
