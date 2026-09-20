package io.kalishak.galacticraftlegacy.world.level.block.entity;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.SlotProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;

public interface LootContextProvider extends SlotProvider {
    LootContext getLootContext(ServerLevel level, ItemStack queriedStack);

    static int getFuelTime(ServerLevel level, LootContextProvider entity, ItemStack stack) {
        return ResolvableInt.getFromItem(stack, DataComponents.COOKING_FUEL, CookingFuel::burnTime, entity.getLootContext(level, stack), 0);
    }
}
