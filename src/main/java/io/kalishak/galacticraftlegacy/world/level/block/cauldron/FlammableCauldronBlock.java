/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.cauldron;

import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.CauldronFluidContent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jspecify.annotations.Nullable;

public class FlammableCauldronBlock extends FilledCauldronBlock {
    public FlammableCauldronBlock(CauldronInteraction.Dispatcher interactions, Properties properties) {
        super(interactions, properties);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier applier, boolean intersects) {
        if (entity.canInteractWithLevel() && entity.isOnFire()) {
            kaboom(entity, level, pos);
        }
    }

    @Override
    public void onBlockExploded(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion) {
        super.onBlockExploded(state, level, pos, explosion);
        kaboom(explosion.getDirectSourceEntity(), level, pos);
    }

    @Override
    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (hit.isInside() && projectile.canInteractWithLevel() && projectile.isOnFire()) {
            kaboom(projectile, level, hit.getBlockPos());
        }
    }

    private void kaboom(@Nullable Entity entity, Level level, BlockPos pos) {
        FluidStack fluidIn = FluidStack.EMPTY;
        CauldronFluidContent cauldronFluidContent = CauldronFluidContent.getForBlock(this);

        if (cauldronFluidContent != null && cauldronFluidContent.fluid != Fluids.EMPTY) {
            fluidIn = new FluidStack(cauldronFluidContent.fluid, FluidType.BUCKET_VOLUME);
        }

        if (fluidIn.is(GalacticraftTags.Fluids.FLAMMABLE_LIQUID)) {
            level.removeBlock(pos, false);
            level.explode(
                    entity,
                    pos.getX(),
                    pos.getY() + 0.5D,
                    pos.getZ(),
                    5,
                    Level.ExplosionInteraction.MOB
            );

            if (entity instanceof Projectile projectile && !projectile.isInvulnerable()) {
                projectile.setRemoved(Entity.RemovalReason.KILLED);
            }
        }
    }
}
