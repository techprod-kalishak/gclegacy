package io.kalishak.galacticraftlegacy.mixin;

import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.level.OxygenHelper;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.block.WallUnlitTorchBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//@Mixin(StandingAndWallBlockItem.class)
public abstract class StandingAndWallBlockItemMixin {
    @Unique
    private static @Nullable BlockState galacticraftlegacy$createUnlitBlockState(BlockState state) {
        if (state.is(GalacticraftTags.Blocks.LIT_TORCHES_STANDING)) return GalacticraftBlocks.UNLIT_TORCH.get().defaultBlockState();
        else if (state.is(GalacticraftTags.Blocks.LIT_TORCHES_WALL)) {
            Direction facing = state.getValue(WallTorchBlock.FACING);

            return GalacticraftBlocks.UNLIT_WALL_TORCH.get().defaultBlockState().setValue(WallUnlitTorchBlock.FACING, facing);
        }

        return null;
    }

    //@Inject(method = "getPlacementState", at = @At("TAIL"), cancellable = true)
    private void galacticraftlegacy$getPlacementState(BlockPlaceContext context, CallbackInfoReturnable<@Nullable BlockState> cir) {
        BlockState ogState = cir.getReturnValue();

        if (ogState != null && ogState.is(GalacticraftTags.Blocks.LIT_TORCHES)) {
            Level level = context.getLevel();

            if (!OxygenHelper.isBreathableAir(level, context.getClickedPos())) {
                cir.setReturnValue(galacticraftlegacy$createUnlitBlockState(ogState));
            }
        }
    }
}
