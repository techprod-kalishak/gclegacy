package io.kalishak.galacticraftlegacy.world.level.material.fluid;

import io.kalishak.galacticraftlegacy.world.damagesource.GalacticraftDamageTypes;
import io.kalishak.galacticraftlegacy.world.level.GalacticraftParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import org.jspecify.annotations.Nullable;

public abstract class SulfuricAcidFluid extends BaseFlowingFluid {
    protected SulfuricAcidFluid(Properties properties) {
        super(properties);
    }

    @Override
    protected void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier) {
        if (level instanceof ServerLevel serverLevel) {
            if (entity.hurtServer(serverLevel, serverLevel.damageSources().source(GalacticraftDamageTypes.ACID_VICTIM), 2.0F) && !entity.isSilent()) {
                serverLevel.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SLIME_HURT_SMALL, SoundSource.BLOCKS, 0.4F, 2.0F + level.getRandom().nextFloat() * 0.4F);
            }
        }
    }

    @Override
    protected @Nullable ParticleOptions getDripParticle() {
        return GalacticraftParticleTypes.DRIPPING_SULFURIC_ACID.get();
    }

    public static class Flowing extends SulfuricAcidFluid {
        public Flowing(Properties properties) {
            super(properties);
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends SulfuricAcidFluid {
        public Source(Properties properties) {
            super(properties);
        }

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }
    }
}
