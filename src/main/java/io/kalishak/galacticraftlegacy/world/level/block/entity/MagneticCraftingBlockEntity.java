/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.world.inventory.container.memory.MemorableContainer;
import io.kalishak.galacticraftlegacy.world.inventory.container.memory.CraftingMemory;
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class MagneticCraftingBlockEntity extends BaseItemStorageBlockEntity implements CraftingStorage, MemorableContainer {
    private NonNullList<ItemStack> memories = NonNullList.withSize(9, ItemStack.EMPTY);
    private boolean isMemoryOverridden;
    private @NonNull ItemStack lastRecipeResult = ItemStack.EMPTY;
    private @Nullable RecipeHolder<?> recipeUsed;

    public MagneticCraftingBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(GalacticraftBlockEntityType.MAGNETIC_CRAFTING.get(), worldPosition, blockState);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        BaseItemStorageBlockEntity.registerDirectionalSlots(event, GalacticraftBlockEntityType.MAGNETIC_CRAFTING.get(), Direction.UP, 1, 10, Direction.DOWN, 0);
    }

    @Override
    public Component getDefaultName() {
        return getBlockState().getBlock().getName();
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
    public int getSize() {
        return 10;
    }

    @Override
    public int getMemorySize() {
        return 9;
    }

    @Override
    public ResourceHandler<ItemResource> getResourceHandler() {
        return this.items;
    }

    @Override
    public void fillStackedContents(StackedItemContents contents) {
        forEachResource((resource, count) -> contents.accountStack(resource.toStack(count)));
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
    public boolean overrideMemory(@NonNull ItemStack newMemory, NonNullList<ItemStack> memories) {
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
                ItemStack stack = ItemUtil.getStack(this.items, i);
                ItemStack memory = this.memories.get(i);

                if (!ItemStack.isSameItemSameComponents(stack, memory)) {
                    fuzzyMatch = false;
                    break;
                }
            }

            if (!fuzzyMatch) {
                for (int i = 0; i < getMemorySize(); i++) {
                    ItemStack stack = ItemUtil.getStack(this.items, i);

                    if (ItemStack.isSameItemSameComponents(newMemory, stack)) {
                        for (int j = 0; j < getMemorySize(); j++) {
                            memories.set(j, ItemUtil.getStack(this.items, j));
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
                    ItemStack currentStack = ItemUtil.getStack(this.items, i);

                    if (!currentStack.isEmpty()) {
                        this.memories.set(i, currentStack);
                    }
                }
            }
        }
    }

    @Override
    public NonNullList<ItemStack> getMemories() {
        return this.memories;
    }

    @Override
    public void setMemories(List<ItemStack> memories) {
        this.memories = NonNullList.withSize(getMemorySize(), ItemStack.EMPTY);

        for (int i = 0; i < getMemorySize(); i++) {
            this.memories.set(i, memories.get(i));
        }
    }

    @Override
    public boolean isMemoryOverridden() {
        if (this.level instanceof ServerLevel serverLevel) {
            Optional<RecipeHolder<CraftingRecipe>> recipe = serverLevel.recipeAccess().getRecipeFor(RecipeType.CRAFTING, asCraftInput(), serverLevel);

            if (recipe.isPresent()) {
                for (int i = 0; i < getMemorySize(); i++) {
                    if (!this.items.getResource(i).isEmpty() && !ItemStack.isSameItemSameComponents(ItemUtil.getStack(this.items, i), this.memories.get(i))) {
                        this.isMemoryOverridden = true;
                    }
                }
            }
        }

        return this.isMemoryOverridden;
    }

    @Override
    public void setMemoryOverridden(boolean isMemoryOverridden) {
        this.isMemoryOverridden = isMemoryOverridden;
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
    protected void onItemChange(int slot, ItemStack previousStack) {
        super.onItemChange(slot, previousStack);
        refreshRecipeResult(null, result -> setItem(0, result));
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        MemorableContainer.save(output, this);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        MemorableContainer.load(input, this);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);

        if (!this.memories.isEmpty()) {
            CraftingMemory craftingMemory = new CraftingMemory(
                    this.memories,
                    this.isMemoryOverridden,
                    this.lastRecipeResult,
                    Optional.ofNullable(this.recipeUsed)
            );
            components.set(GalacticraftDataComponents.CRAFTING_MEMORY, craftingMemory);
        }
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        CraftingMemory craftingMemory = components.getOrDefault(GalacticraftDataComponents.CRAFTING_MEMORY, CraftingMemory.FORGOTTEN);

        if (!craftingMemory.memories().isEmpty()) {
            setMemories(craftingMemory.memories());
            setMemoryOverridden(craftingMemory.isOverridden());
            setLastResult(craftingMemory.lastResult());
            craftingMemory.lastRecipe().ifPresent(this::setRecipeUsed);
        }
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new MagneticCraftingMenu(containerId, inventory, this);
    }
}
