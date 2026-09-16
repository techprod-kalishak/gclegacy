package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.world.item.SpaceEmergencyKitItem;
import io.kalishak.galacticraftlegacy.world.level.block.entity.EmergencyPostBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class EmergencyPostBlock extends BaseEntityBlock {
    public static final MapCodec<EmergencyPostBlock> CODEC = simpleCodec(EmergencyPostBlock::new);
    public static final BooleanProperty WITH_KIT = BooleanProperty.create("with_kit");

    public EmergencyPostBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(WITH_KIT, true));
    }

    @Override
    protected MapCodec<EmergencyPostBlock> codec() {
        return CODEC;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
        ItemContainerContents containerContents = itemStack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        ItemStack emergencyKit = containerContents.copyOne();

        if (!emergencyKit.isEmpty() && emergencyKit.getItem() instanceof SpaceEmergencyKitItem) {
            BlockEntity entity = level.getBlockEntity(pos);

            if (entity instanceof EmergencyPostBlockEntity emergencyBox) {
                emergencyBox.putEmergencyKit(emergencyKit, null);
            }
        }

        super.setPlacedBy(level, pos, state, by, itemStack);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        ItemStack stack = context.getItemInHand();
        ItemContainerContents container = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);

        if (container.copyOne().isEmpty()) {
            return defaultBlockState().setValue(WITH_KIT, false);
        }

        return defaultBlockState();
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (itemStack.getItem() instanceof SpaceEmergencyKitItem) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof EmergencyPostBlockEntity emergencyBox) {
                if (emergencyBox.putEmergencyKit(itemStack, hitResult.getDirection().getOpposite())) {
                    state.setValue(WITH_KIT, true);
                    return InteractionResult.CONSUME;
                }
            }
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        ItemStack stack = player.getMainHandItem();

        if (stack.isEmpty()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof EmergencyPostBlockEntity emergencyBox) {
                if (player.isShiftKeyDown()) {
                    emergencyBox.toggleOpen(hitResult.getDirection().getOpposite());
                }

                if (emergencyBox.isOpened() && emergencyBox.consumeKit(player)) {
                    state.setValue(WITH_KIT, false);

                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EmergencyPostBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WITH_KIT);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level.isClientSide() ? createTickerHelper(type, GalacticraftBlockEntityType.EMERGENCY_POST.get(), EmergencyPostBlockEntity::clientTick) : null;
    }
}
