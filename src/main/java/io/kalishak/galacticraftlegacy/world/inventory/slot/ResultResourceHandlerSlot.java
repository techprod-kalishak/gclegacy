package io.kalishak.galacticraftlegacy.world.inventory.slot;

import io.kalishak.galacticraftlegacy.world.level.block.entity.ElectricFurnaceBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import java.util.Optional;

public class ResultResourceHandlerSlot extends ResourceHandlerSlot {
    private final Player player;
    private final ElectricFurnaceBlockEntity machine;
    private int removeCount;

    public ResultResourceHandlerSlot(Player player, ResourceHandler<ItemResource> handler, ElectricFurnaceBlockEntity machine, int index, int xPosition, int yPosition) {
        super(handler, machine::set, index, xPosition, yPosition);
        this.player = player;
        this.machine = machine;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public Optional<ItemStack> tryRemove(int count, int decrement, Player player) {
        Optional<ItemStack> stack = tryRemove(count, decrement, player);

        if (stack.isPresent()) {
            this.removeCount += decrement;
        }

        return stack;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        checkTakeAchievements(stack);
        super.onTake(player, stack);
    }

    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        this.removeCount += amount;
        super.onQuickCraft(stack, amount);
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        stack.onCraftedBy(this.player, this.removeCount);

        if (this.player instanceof ServerPlayer serverPlayer) {
            this.machine.awardUsedRecipes(serverPlayer);
        }
    }
}
