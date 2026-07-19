package io.kalishak.galacticraftlegacy.world.level.block;

import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.world.inventory.workbench.NasaWorkbenchMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class NasaWorkbenchBlock extends Block {
    public NasaWorkbenchBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            player.openMenu(state.getMenuProvider(level, pos), pos);
        }

        return InteractionResult.SUCCESS;
    }

    protected Component getTitle() {
        return GalacticraftComponents.NASA_WORKBENCH_TITLE;
    }

    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
                (containerId, inventory, _) -> new NasaWorkbenchMenu(containerId, inventory, level, pos),
                getTitle()
        );
    }
}
