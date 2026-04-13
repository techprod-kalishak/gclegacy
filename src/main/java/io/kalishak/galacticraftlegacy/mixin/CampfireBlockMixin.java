package io.kalishak.galacticraftlegacy.mixin;

import io.kalishak.galacticraftlegacy.world.level.OxygenHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin {
    //@Inject(method = "getStateForPlacement", at = @At(value = "TAIL"), cancellable = true)
    private void galacticraftlegacy$getStateForPlacement(BlockPlaceContext context, CallbackInfoReturnable<@Nullable BlockState> cir) {
        BlockState state = cir.getReturnValue();

        if (state != null && state.getValue(BlockStateProperties.LIT)) {
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();

            if (!OxygenHelper.hasOxygenNearby(level, pos, 1.0D, false)) {
                cir.setReturnValue(state.setValue(BlockStateProperties.LIT, false));
                return;
            }
        }

        cir.cancel();
    }
}
