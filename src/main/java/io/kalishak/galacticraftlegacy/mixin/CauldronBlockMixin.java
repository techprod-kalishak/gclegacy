package io.kalishak.galacticraftlegacy.mixin;

import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.dimension.GalacticraftDimensionTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin {
    @Inject(method = "handlePrecipitation", at = @At("HEAD"), cancellable = true)
    private void galacticraftlegacy$handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation, CallbackInfo ci) {
        if (shouldHandlePrecipitation(level, precipitation)) {
            if (level.dimensionTypeRegistration().is(GalacticraftDimensionTypes.VENUS) && precipitation == Biome.Precipitation.RAIN) {
                level.setBlockAndUpdate(pos, GalacticraftBlocks.SULFURIC_ACID_CAULDRON.get().defaultBlockState());
                level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
            } else if (!level.dimensionTypeRegistration().is(GalacticraftDimensionTypes.VENUS)) {
                ci.cancel();
            }
        }
    }

    @Shadow
    protected static boolean shouldHandlePrecipitation(Level level, Biome.Precipitation precipitation) {
        return false;
    }
}
