package io.kalishak.galacticraftlegacy.world.level.block.cauldron;

import io.kalishak.galacticraftlegacy.world.damagesource.GalacticraftDamageTypes;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

public class AcidCauldronBlock extends LayeredCauldronBlock {
    public AcidCauldronBlock(CauldronInteraction.Dispatcher interactions, Properties properties) {
        super(Biome.Precipitation.RAIN, interactions, properties);
    }

    @Override
    protected boolean canReceiveStalactiteDrip(Fluid fluid) {
        return fluid.isSame(GalacticraftFluids.SULFURIC_ACID.get());
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        if (level instanceof ServerLevel serverLevel) {
            if (entity.hurtServer(serverLevel, serverLevel.damageSources().source(GalacticraftDamageTypes.ACID_VICTIM), 1.0F) && !entity.isSilent()) {
                serverLevel.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SLIME_HURT_SMALL, SoundSource.BLOCKS, 0.4F, 2.0F + level.getRandom().nextFloat() * 0.4F);
            }
        }
    }
}
