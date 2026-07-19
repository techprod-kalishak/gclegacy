package io.kalishak.galacticraftlegacy.world.inventory.workbench;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.entity.PlayerSpaceData;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.registry.SchematicVariant;
import io.kalishak.galacticraftlegacy.registry.SchematicVariants;
import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.inventory.container.ResourceHandlerCraftingResultInventory;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.ResourceHandlerInput;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket.VehicleCraftingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.CombinedResourceHandler;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public interface VehicleCraftingMenuProvider {
    static void removed(ContainerLevelAccess levelAccess, ResourceHandler<ItemResource> resourceHandler, Player player) {
        levelAccess.execute((_, _) -> ResourcefulHelper.populatePlayerFromContainer(player, resourceHandler));
    }

    static boolean stillValid(ContainerLevelAccess levelAccess, Player player) {
        return levelAccess.evaluate((level, pos) -> level.getBlockState(pos).is(GalacticraftTags.Blocks.NASA_WORKBENCHES) && player.isWithinBlockInteractionRange(pos, 4.0F), true);
    }

    static ResourceHandler<ItemResource> createCraftingHandler(VehicleCraftingPage page) {
        return new CombinedResourceHandler<>(
                new ItemStacksResourceHandler(page.getInputSlotSize()),
                new ResourceHandlerCraftingResultInventory()
        );
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

    static void openNextPage(Level level, BlockPos pos, Player player, @Nullable Holder<SchematicVariant> currentSchematic) {
        if (player.containerMenu instanceof NasaWorkbenchPageMenu) {
            player.closeContainer();
        }

        PlayerSpaceData data = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA);
        ResourceKey<SchematicVariant> nextSchematic = currentSchematic == null
                ? SchematicVariants.TIER_2_ROCKET
                : data.getSchematics().nextBy(currentSchematic.getKey());

        if (nextSchematic != null) {
            Optional<Holder.Reference<VehicleCraftingPage>> nextPage = level.registryAccess().get(Constants.castKey(nextSchematic, GalacticraftRegistries.Keys.VEHICLE_CRAFTING_PAGE));
            nextPage.ifPresent(
                    holder ->
                            player.openMenu(
                                    new SimpleMenuProvider(
                                            (containerId, inventory, _) -> new NasaWorkbenchPageMenu(containerId, inventory, level, pos, holder.value()),
                                            Component.empty()
                                    )
                            )
            );
        }
    }

    static Holder<VehicleCraftingPage> getPage(Level level, ResourceKey<VehicleCraftingPage> pageKey) {
        return level.registryAccess().getOrThrow(pageKey);
    }
}
