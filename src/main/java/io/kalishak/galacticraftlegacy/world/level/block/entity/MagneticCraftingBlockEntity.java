/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.data.TriStateBoolean;
import io.kalishak.galacticraftlegacy.transfer.capability.item.MemorableResourceHandler;
import io.kalishak.galacticraftlegacy.world.inventory.container.memory.MemorableContainer;
import io.kalishak.galacticraftlegacy.world.inventory.magnetic_crafting.MagneticCraftingMenu;
import io.kalishak.galacticraftlegacy.world.inventory.container.CraftingStorage;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class MagneticCraftingBlockEntity extends BlockEntity implements MenuProvider, CraftingStorage, MemorableContainer {
    private final MemorableResourceHandler items;
    private NonNullList<ItemResource> memories = NonNullList.withSize(9, ItemResource.EMPTY);
    private @NonNull TriStateBoolean isMemoryOverridden = TriStateBoolean.NONE;
    private @NonNull ItemStack lastRecipeResult = ItemStack.EMPTY;
    private @Nullable RecipeHolder<?> recipeUsed;

    public MagneticCraftingBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(GalacticraftBlockEntityType.MAGNETIC_CRAFTING_TABLE.get(), worldPosition, blockState);
        this.items = new MemorableResourceHandler(this, 10) {
            @Override
            protected void onContentsChanged(int index, ItemStack previousContents) {
                super.onContentsChanged(index, previousContents);
                refreshRecipeResult(null, result -> setItem(0, result));
            }
        };
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                GalacticraftBlockEntityType.MAGNETIC_CRAFTING_TABLE.get(),
                (entity, side) -> {
                    if (side == Direction.UP) {
                        return RangedResourceHandler.of(() -> entity.items, 1, 10);
                    } else if (side == Direction.DOWN) {
                        return RangedResourceHandler.ofSingleIndex(() -> entity.items, 0);
                    }

                    return entity.items;
                }
        );
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 3;
    }

    @Override
    public int getMemorySize() {
        return 9;
    }

    @Override
    public int getOutputSlotIndex() {
        return 0;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.galacticraftlegacy.magnetic_crafting_table");
    }

    @Override
    public ResourceHandler<ItemResource> getResourceHandler() {
        return this.items;
    }

    public void refreshRecipeResult(@Nullable RecipeHolder<CraftingRecipe> recipeHint, Consumer<ItemStack> stackConsumer) {
        if (this.level instanceof ServerLevel serverLevel) {
            CraftingInput craftInput = asCraftInput();
            serverLevel.recipeAccess()
                    .getRecipeFor(RecipeType.CRAFTING, craftInput, serverLevel, recipeHint)
                    .ifPresent(recipe -> {
                        setRecipeUsed(recipe);
                        ItemStack result = recipe.value().assemble(craftInput);
                        stackConsumer.accept(result);
                    });
        }
    }

    @Override
    public boolean overrideMemory(@NonNull ItemResource newMemory, NonNullList<ItemResource> memories) {
        boolean allEmpty = true;

        for (int i = 0; i < getMemorySize(); i++) {
            if (!this.items.getResource(i).isEmpty()) {
                allEmpty = false;
                break;
            }
        }

        if (allEmpty) {
            return false;
        }

        Optional<RecipeHolder<CraftingRecipe>> recipe = Optional.empty();

        if (this.level instanceof ServerLevel serverLevel) {
            recipe = serverLevel.recipeAccess().getRecipeFor(RecipeType.CRAFTING, asCraftInput(), serverLevel);
        }

        if (recipe.isPresent()) {
            boolean fuzzyMatch = true;

            for (int i = 0; i < getMemorySize(); i++) {
                ItemResource resource = this.items.getResource(i);
                ItemResource memory = this.memories.get(i);

                if (!resource.matches(memory.toStack())) {
                    fuzzyMatch = false;
                    break;
                }
            }

            if (!fuzzyMatch) {
                for (int i = 0; i < getMemorySize(); i++) {
                    ItemResource resource = this.items.getResource(i);

                    if (newMemory.matches(resource.toStack())) {
                        for (int j = 0; j < getMemorySize(); j++) {
                            memories.set(j, this.items.getResource(j));
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void updateMemory(@NonNull ItemStack resultStack) {
        if (this.level instanceof ServerLevel serverLevel) {
            Optional<RecipeHolder<CraftingRecipe>> recipe = serverLevel.recipeAccess().getRecipeFor(RecipeType.CRAFTING, asCraftInput(), serverLevel);

            if (recipe.isPresent()) {
                setRecipeUsed(recipe.get());
                setLastResult(resultStack);

                for (int i = 0; i < getMemorySize(); i++) {
                    ItemResource resource = this.items.getResource(i);

                    if (!resource.isEmpty()) {
                        this.memories.set(i, resource);
                    }
                }
            }
        }
    }

    @Override
    public NonNullList<ItemResource> getMemories() {
        return this.memories;
    }

    @Override
    public void setMemories(List<ItemResource> memories) {
        this.memories = NonNullList.withSize(getMemorySize(), ItemResource.EMPTY);

        for (int i = 0; i < getMemorySize(); i++) {
            this.memories.set(i, memories.get(i));
        }
    }

    @Override
    public boolean isMemoryOverridden() {
        if (this.isMemoryOverridden.isBound()) {
            return this.isMemoryOverridden.value();
        }

        if (this.level instanceof ServerLevel serverLevel) {
            Optional<RecipeHolder<CraftingRecipe>> recipe = serverLevel.recipeAccess().getRecipeFor(RecipeType.CRAFTING, asCraftInput(), serverLevel);

            if (recipe.isPresent()) {
                for (int i = 0; i < getMemorySize(); i++) {
                    ItemResource resource = this.items.getResource(i);

                    if (!resource.isEmpty() && !resource.matches(this.memories.get(i).toStack())) {
                        this.isMemoryOverridden = TriStateBoolean.TRUE;
                        return true;
                    }
                }
            }
        }

        this.isMemoryOverridden = TriStateBoolean.FALSE;
        return false;
    }

    @Override
    public void setMemoryOverridden(boolean isMemoryOverridden) {
        this.isMemoryOverridden = TriStateBoolean.of(isMemoryOverridden);
    }

    @Override
    public void updateOverrideStatus() {
        this.isMemoryOverridden = TriStateBoolean.NONE;
    }

    @Override
    public void setLastResult(@NonNull ItemStack resultStack) {
        this.lastRecipeResult = resultStack;
    }

    @Override
    public @NonNull ItemStack getLastResult() {
        return this.lastRecipeResult;
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipeUsed) {
        this.recipeUsed = recipeUsed;
    }

    @Override
    public @Nullable RecipeHolder<?> getRecipeUsed() {
        return this.recipeUsed;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.items.set(slot, ItemResource.of(stack), stack.getCount());
    }

    public void setItem(int slot, ItemResource resource, int amount) {
        this.items.set(slot, resource, amount);
    }

    @Override
    public ItemStack getItem(int slot) {
        return ItemUtil.getStack(this.items, slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        try (Transaction transaction = Transaction.open(null)) {
            ItemResource resource = this.items.getResource(slot);
            int extracted = this.items.extract(slot, resource, count, transaction);

            if (extracted > 0) {
                return resource.toStack(extracted);
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        saveMemories(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        loadMemories(input);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);

        if (!this.memories.isEmpty()) {
            components.set(GalacticraftDataComponents.CRAFTING_MEMORY, saveAsComponent());
        }
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        readFromComponent(components);
    }

    @Override
    public void fillStackedContents(StackedItemContents stackedItemContents) {
        this.items.copyToList().forEach(stackedItemContents::accountStack);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new MagneticCraftingMenu(containerId, inventory, this);
    }
}
