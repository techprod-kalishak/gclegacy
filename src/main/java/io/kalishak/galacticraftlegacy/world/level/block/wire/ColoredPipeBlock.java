package io.kalishak.galacticraftlegacy.world.level.block.wire;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.ColoredPipeBlockEntity;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.network.NetworkType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ColoredPipeBlock extends AbstractWireBlock {
    public static final MapCodec<ColoredPipeBlock> CODEC =  RecordCodecBuilder.mapCodec(instance -> instance.group(
            DyeColor.CODEC.fieldOf("color").forGetter(coloredPipeBlock -> coloredPipeBlock.color),
            propertiesCodec()
    ).apply(instance, ColoredPipeBlock::new));
    private static final Map<DyeColor, Supplier<ColoredPipeBlock>> COLORED_PIPES_BY_DYE = new HashMap<>();
    private final DyeColor color;

    public ColoredPipeBlock(DyeColor color, Properties properties) {
        super(NetworkType.FLUID, 0.4D, properties);
        this.color = color;
        COLORED_PIPES_BY_DYE.put(color, () -> this);
    }

    @Override
    protected MapCodec<ColoredPipeBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean canBeConnected(Level level, BlockPos pos, Direction direction) {
        BlockState state = level.getBlockState(pos.relative(direction));
        boolean doConnect = false;

        if (state.getBlock() instanceof ColoredPipeBlock coloredPipeBlock) {
            doConnect = coloredPipeBlock.color == this.color;
        }

        return doConnect && super.canBeConnected(level, pos, direction);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.getItem() instanceof DyeItem dye) {
            if (!level.isClientSide()) {
                BlockState newState = COLORED_PIPES_BY_DYE.get(dye.getDyeColor()).get().defaultBlockState();

                if (newState != state) {
                    level.setBlock(pos, newState, ColoredPipeBlock.UPDATE_ALL);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
                    stack.consume(1, player);

                    if (level.getBlockEntity(pos) instanceof ColoredPipeBlockEntity coloredPipeBlockEntity) {
                        coloredPipeBlockEntity.setColor(dye.getDyeColor());
                        coloredPipeBlockEntity.updateNetwork();
                    }
                }
            }

            return InteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;//return new ColoredPipeBlockEntity(blockPos, blockState, this.color);
    }

//    @Override
//    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
//        if (level instanceof ServerLevel serverLevel) {
//            return createTickerHelper(
//                    blockEntityType,
//                    GalacticraftBlockEntityType.COLORED_PIPE.get(),
//                    (l, pipePos, pipeState, pipe) -> ColoredPipeBlockEntity.serverTick(serverLevel, pipePos, pipeState, pipe)
//            );
//        }
//
//        return null;
//    }
}
