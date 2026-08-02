/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.workbench;

import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.ResourceHandlerInput;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingRecipe;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.Optional;

public interface VehicleCraftingMenuProvider {
    static void removed(ContainerLevelAccess levelAccess, ResourceHandler<ItemResource> resourceHandler, Player player) {
        levelAccess.execute((_, _) -> ResourcefulHelper.populatePlayerFromContainer(player, resourceHandler));
    }

    static boolean stillValid(ContainerLevelAccess levelAccess, Player player) {
        return levelAccess.evaluate((level, pos) -> level.getBlockState(pos).is(GalacticraftTags.Blocks.NASA_WORKBENCHES) && player.isWithinBlockInteractionRange(pos, 4.0F), true);
    }

    static void slotChangedCraftingGrid(AbstractContainerMenu menu, ServerLevel level, Player player, ResourceHandler<ItemResource> resourceHandler, IndexModifier<ItemResource> indexModifier, VehicleCraftingPage page) {
        ResourceHandlerInput input = new ResourceHandlerInput(resourceHandler);
        ServerPlayer serverPlayer = (ServerPlayer) player;
        ItemStack result = ItemStack.EMPTY;
        Optional<RecipeHolder<VehicleCraftingRecipe>> maybeRecipe = level.getServer().getRecipeManager().getRecipeFor(GalacticraftRecipeType.VEHICLE_CRAFTING.get(), input, level);

        if (maybeRecipe.isPresent()) {
            RecipeHolder<VehicleCraftingRecipe> recipeHolder = maybeRecipe.get();
            VehicleCraftingRecipe craftingRecipe = recipeHolder.value();

            result = craftingRecipe.assemble(input);
        }

        int resultSlotIndex = page.vehicleRecipe().value().outputSlot().slotIndex();
        indexModifier.set(resultSlotIndex, ItemResource.of(result), result.count());
        menu.setRemoteSlot(resultSlotIndex, result);
        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), resultSlotIndex, result));
    }
}
