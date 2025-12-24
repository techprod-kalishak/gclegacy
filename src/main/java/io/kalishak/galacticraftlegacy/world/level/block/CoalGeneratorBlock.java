package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.world.level.block.entity.CoalGeneratorBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class CoalGeneratorBlock extends AbstractMachineBlock {
    public static final MapCodec<CoalGeneratorBlock> CODEC = simpleCodec(CoalGeneratorBlock::new);

    public CoalGeneratorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<CoalGeneratorBlock> codec() {
        return CODEC;
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof MenuProvider menuProvider) {
            player.openMenu(menuProvider, pos);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CoalGeneratorBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level instanceof ServerLevel serverLevel) {
            return createTickerHelper(
                    blockEntityType,
                    GalacticraftBlockEntityType.COAL_GENERATOR.get(),
                    (tickerLevel, tickerPos, tickerState, ticker) -> CoalGeneratorBlockEntity.serverTick(serverLevel, tickerPos, tickerState, ticker)
            );
        }

        return null;
    }
}
