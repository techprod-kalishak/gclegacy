package io.kalishak.galacticraftlegacy.world.level.block.cauldron;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.level.block.entity.FlammableCauldronBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.CauldronFluidContent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.jspecify.annotations.Nullable;

public class FlammableCauldronBlock extends AbstractCauldronBlock implements EntityBlock {
    public static final MapCodec<FlammableCauldronBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CauldronInteractions.CODEC.fieldOf("interactions").forGetter(flammableCauldronBlock -> flammableCauldronBlock.interactions),
            BuiltInRegistries.FLUID.holderByNameCodec().fieldOf("fuel_in").forGetter(flammableCauldronBlock -> flammableCauldronBlock.fuelIn),
            propertiesCodec()
    ).apply(instance, FlammableCauldronBlock::new));
    public static final BooleanProperty FULL = BooleanProperty.create("full");
    private final Holder<Fluid> fuelIn;

    public FlammableCauldronBlock(CauldronInteraction.Dispatcher interactions, Holder<Fluid> fuelIn, Properties properties) {
        super(properties, interactions);
        this.fuelIn = fuelIn;
        registerDefaultState(this.stateDefinition.any().setValue(FULL, true));
    }

    @Override
    protected MapCodec<? extends FlammableCauldronBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
        super.triggerEvent(state, level, pos, id, param);
        BlockEntity blockentity = level.getBlockEntity(pos);
        return blockentity != null && blockentity.triggerEvent(id, param);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier applier, boolean intersects) {
        if (!level.isClientSide()) {
            kaboom(entity, state, level, pos);
        }
    }

    @Override
    public void onBlockExploded(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion) {
        super.onBlockExploded(state, level, pos, explosion);
        kaboom(explosion.getDirectSourceEntity(), state, level, pos);
    }

    @Override
    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (projectile.isOnFire() && projectile.canInteractWithLevel()) {
            kaboom(projectile, state, level, hit.getBlockPos());
        }
    }

    private void kaboom(@Nullable Entity entity, BlockState state, Level level, BlockPos pos) {
        if (entity == null || entity.isOnFire()) {
            FluidStack fluidIn = FluidStack.EMPTY;

            ResourceHandler<FluidResource> fluidTank = level.getCapability(Capabilities.Fluid.BLOCK, pos, null);

            if (fluidTank != null) {
                fluidIn = FluidUtil.getStack(fluidTank, 0);
            } else if (state.getValue(FULL)) {
                CauldronFluidContent cauldronFluidContent = CauldronFluidContent.getForBlock(this);

                if (cauldronFluidContent != null && cauldronFluidContent.fluid != Fluids.EMPTY) {
                    fluidIn = new FluidStack(cauldronFluidContent.fluid, FluidType.BUCKET_VOLUME);
                }
            }

            if (fluidIn.is(GalacticraftTags.Fluids.FLAMMABLE_LIQUID)) {
                float fluidAmount = fluidIn.getAmount() / 1000.0F;
                level.removeBlock(pos, false);
                level.explode(
                        entity,
                        pos.getX() + 0.5,
                        pos.getY(),
                        pos.getZ(),
                        2 + 4 * fluidAmount,
                        Level.ExplosionInteraction.MOB
                );

                if (entity instanceof Projectile projectile && entity.isAlive()) {
                    projectile.setRemoved(Entity.RemovalReason.KILLED);
                }
            }
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FlammableCauldronBlockEntity(blockPos, blockState, new FluidStack(this.fuelIn, FluidType.BUCKET_VOLUME));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FULL);
    }

    @Override
    public boolean isFull(BlockState blockState) {
        return blockState.getValue(FULL);
    }
}
