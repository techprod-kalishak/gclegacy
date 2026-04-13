package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.Nullable;

public class TerraformableBlock extends Block implements EarthTurnableBlock {
    public static final MapCodec<TerraformableBlock> CODEC = simpleCodec(TerraformableBlock::new);
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;

    public TerraformableBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(PERSISTENT, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(PERSISTENT, true);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PERSISTENT);
    }

    @Override
    public @Nullable BlockState getTerraformedBlock(BlockGetter level, BlockPos blockPos) {
        if (level.getBlockState(blockPos).getValue(PERSISTENT)) {
            return null;
        }

        BlockState stateAbove = level.getBlockState(blockPos.above());

        if (stateAbove.is(GalacticraftTags.Blocks.BREATHABLE_AIR)) {
            if (EarthTurnableBlock.adjacentToOrNearWater(level, blockPos, Tags.Blocks.GLASS_BLOCKS)) {
                return Blocks.GRASS_BLOCK.defaultBlockState();
            }

            return Blocks.DIRT.defaultBlockState();
        }

        return null;
    }
}
