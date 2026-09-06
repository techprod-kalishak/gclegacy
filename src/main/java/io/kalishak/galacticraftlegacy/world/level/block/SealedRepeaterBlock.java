package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;

public class SealedRepeaterBlock extends FullDiodeBlock {
    public static final MapCodec<SealedRepeaterBlock> CODEC = simpleCodec(SealedRepeaterBlock::new);
    public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;
    public static final IntegerProperty DELAY = BlockStateProperties.DELAY;

    public SealedRepeaterBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false).setValue(LOCKED, Boolean.FALSE).setValue(DELAY, 1));
    }

    @Override
    protected int getDelay(BlockState state) {
        return state.getValue(DELAY) * 2;
    }

    @Override
    protected MapCodec<SealedRepeaterBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!itemStack.is(GalacticraftTags.Items.WRENCH)) {
            return InteractionResult.PASS;
        }

        level.setBlock(pos, state.cycle(DELAY), SealedRepeaterBlock.UPDATE_ALL);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NonNull BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);

        return state.setValue(LOCKED, isLocked(context.getLevel(), context.getClickedPos(), state));
    }

    @Override
    public boolean isLocked(LevelReader level, BlockPos pos, BlockState state) {
        return getAlternateSignal(level, pos, state) > 0;
    }

    @Override
    protected boolean sideInputDiodesOnly() {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, LOCKED, DELAY);
    }
}
