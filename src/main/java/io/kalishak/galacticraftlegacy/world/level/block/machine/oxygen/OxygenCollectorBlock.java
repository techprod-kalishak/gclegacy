/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.machine.oxygen;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.OxygenCollectorBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import io.kalishak.galacticraftlegacy.world.level.block.machine.AbstractMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class OxygenCollectorBlock extends AbstractMachineBlock {
    public static final MapCodec<OxygenCollectorBlock> CODEC = simpleCodec(OxygenCollectorBlock::new);

    public OxygenCollectorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<OxygenCollectorBlock> codec() {
        return CODEC;
    }

    @Override
    public @NonNull NetworkType getNetworkType(@NonNull Direction direction) {
        return direction == Direction.WEST ? NetworkType.FLUID : super.getNetworkType(direction);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new OxygenCollectorBlockEntity(worldPosition, blockState);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof OxygenCollectorBlockEntity oxygenCollector) {
            if (oxygenCollector.lastOxygenCollected > 1) {
                for (int particleCount = 0; particleCount < 10; particleCount++) {
                    double x2 = pos.getX() + random.nextFloat();
                    double y2 = pos.getY() + random.nextFloat();
                    double z2 = pos.getZ() + random.nextFloat();
                    int dir = random.nextInt(2) * 2 - 1;
                    double mX = (random.nextFloat() - 0.5D) * 0.5D;
                    double mY = (random.nextFloat() - 0.5D) * 0.5D;
                    double mZ = (random.nextFloat() - 0.5D) * 0.5D;

                    Direction facing = state.getValue(FACING);
                    Direction.Axis axis = facing.getAxis();

                    if (axis == Direction.Axis.X) {
                        x2 = pos.getX() + 0.5D + 0.25D * dir;
                        mX = random.nextFloat() * 2.0F * dir;
                    } else {
                        z2 = pos.getZ() + 0.5D + 0.25D * dir;
                        mZ = random.nextFloat() * 2.0F * dir;
                    }

                    level.addParticle(ParticleTypes.BUBBLE, x2 + mX, y2 + mY, z2 + mZ, 0.7D, 0.7D, 1.0D);
                }
            }
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        if (level instanceof ServerLevel serverLevel) {
            return createTickerHelper(
                    type,
                    GalacticraftBlockEntityType.OXYGEN_COLLECTOR.get(),
                    (_, worldPosition, worldBlockState, blockEntity) -> OxygenCollectorBlockEntity.serverTick(serverLevel, worldPosition, worldBlockState, blockEntity)
            );
        }

        return null;
    }
}
