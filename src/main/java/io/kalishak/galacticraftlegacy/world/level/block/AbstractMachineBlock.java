package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jspecify.annotations.Nullable;

public abstract class AbstractMachineBlock extends BaseEntityBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT; //Active
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    protected AbstractMachineBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    @Override
    protected abstract MapCodec<? extends AbstractMachineBlock> codec();

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            openContainer(level, pos, player);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(GalacticraftItems.WRENCH) && !stack.nextDamageWillBreak()) {
            state = rotate(state, player.isShiftKeyDown() ? Rotation.CLOCKWISE_90 : Rotation.COUNTERCLOCKWISE_90);
            level.setBlock(pos, state, Block.UPDATE_ALL_IMMEDIATE);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, new GameEvent.Context(player, state));

            stack.hurtAndBreak(1, player, hand);

            return InteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, ItemStack toolStack, boolean willHarvest, FluidState fluid) {
        ResourceHandler<ItemResource> itemResourceHandler = level.getCapability(Capabilities.Item.BLOCK, pos, state, level.getBlockEntity(pos), state.getValue(FACING));

        if (itemResourceHandler != null) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), ItemUtil.getStack(itemResourceHandler, 0));
        }

        return super.onDestroyedByPlayer(state, level, pos, player, toolStack, willHarvest, fluid);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        ItemStack returned =  super.getCloneItemStack(level, pos, state, includeData, player);
        EnergyHandler energyHandler = player.level().getCapability(Capabilities.Energy.BLOCK, pos, state, level.getBlockEntity(pos), state.getValue(FACING));

        if (includeData && energyHandler != null) {
            returned.set(GalacticraftDataComponents.STORED_ENERGY, energyHandler.getAmountAsInt());
        }

        return returned;
    }

    protected abstract void openContainer(Level level, BlockPos pos, Player player);

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        Containers.updateNeighboursAfterDestroy(state, level, pos);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return super.hasAnalogOutputSignal(state);
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        return ResourcefulHelper.getRedstoneSignalFromBlockEntity(level, pos, state, direction);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }
}
