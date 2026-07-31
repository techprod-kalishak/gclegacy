/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.client.gui.screens.inventory.workbench.NasaWorkbenchScreenPage;
import io.kalishak.galacticraftlegacy.registry.SchematicVariants;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.NasaWorkbenchMenu;
import io.kalishak.galacticraftlegacy.world.level.block.entity.NasaWorkbenchBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class NasaWorkbenchBlock extends BaseEntityBlock {
    public static final MapCodec<NasaWorkbenchBlock> CODEC = simpleCodec(NasaWorkbenchBlock::new);

    public NasaWorkbenchBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<NasaWorkbenchBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            player.openMenu(state.getMenuProvider(level, pos), pos);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
                (containerId, inventory, _) -> new NasaWorkbenchMenu(containerId, inventory, level, pos),
                NasaWorkbenchScreenPage.createTitle(SchematicVariants.TIER_1_ROCKET)
        );
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new NasaWorkbenchBlockEntity(blockPos, blockState);
    }
}
