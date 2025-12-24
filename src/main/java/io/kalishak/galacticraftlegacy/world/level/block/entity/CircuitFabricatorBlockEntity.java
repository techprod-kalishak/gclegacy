package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class CircuitFabricatorBlockEntity extends AbstractMachineBlockEntity implements RecipeCraftingHolder {
    public CircuitFabricatorBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.CIRCUIT_FABRICATOR.get(), pos, blockState);
    }

    public static void serverTick(ServerLevel serverLevel, BlockPos tickerPos, BlockState tickerState, CircuitFabricatorBlockEntity circuitFabricator) {
        energyTransferTick(serverLevel, tickerPos, tickerState, circuitFabricator);
    }

    @Override
    protected int size() {
        return 7;
    }

    @Override
    protected int getBatterySlotIndex() {
        return 6;
    }

    @Override
    protected Component getDefaultName() {
        return GalacticraftBlocks.CIRCUIT_FABRICATOR.get().getName();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return null;
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {

    }

    @Override
    public @Nullable RecipeHolder<?> getRecipeUsed() {
        return null;
    }
}
