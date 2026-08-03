/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.container.memory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;

import java.util.List;

public interface MemorableContainer extends RecipeCraftingHolder {
    Codec<RecipeHolder<?>> RECIPE_HOLDER_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.RECIPE).fieldOf("id").forGetter(RecipeHolder::id),
            Recipe.CODEC.fieldOf("value").forGetter(RecipeHolder::value)
    ).apply(instance, RecipeHolder::new));

    int getMemorySize();

    NonNullList<ItemStack> getMemories();
    void setMemories(List<ItemStack> memories);

    boolean overrideMemory(@NonNull ItemStack newMemory, NonNullList<ItemStack> memories);
    void updateMemory(@NonNull ItemStack resultStack);

    @NonNull ItemStack getLastResult();
    void setLastResult(@NonNull ItemStack resultStack);

    boolean isMemoryOverridden();
    void setMemoryOverridden(boolean isMemoryOverridden);

    static void save(ValueOutput output, MemorableContainer memorableContainer) {
        NonNullList<ItemStack> items = memorableContainer.getMemories();
        ValueOutput.TypedOutputList<ItemStackWithSlot> itemsOutput = output.list("Memories", ItemStackWithSlot.CODEC);

        for(int i = 0; i < items.size(); ++i) {
            ItemStack itemStack = items.get(i);

            if (!itemStack.isEmpty()) {
                itemsOutput.add(new ItemStackWithSlot(i, itemStack));
            }
        }

        if (itemsOutput.isEmpty()) {
            output.discard("Memories");
        }

        output.putBoolean("IsMemoryOverridden", memorableContainer.isMemoryOverridden());

        ItemStack memory = memorableContainer.getLastResult();

        if (!memory.isEmpty()) {
            output.store("LastResult", ItemStack.CODEC, memory);
        }

        RecipeHolder<?> lastRecipe = memorableContainer.getRecipeUsed();

        if (lastRecipe != null) {
            output.store("LastRecipe", RECIPE_HOLDER_CODEC, lastRecipe);
        }
    }

    static void load(ValueInput input, MemorableContainer memorableContainer) {
        ValueInput.TypedInputList<ItemStackWithSlot> items = input.listOrEmpty("Items", ItemStackWithSlot.CODEC);
        NonNullList<ItemStack> newMemories = NonNullList.withSize(memorableContainer.getMemorySize(), ItemStack.EMPTY);

        for (ItemStackWithSlot item : items) {
            if (item.isValidInContainer(memorableContainer.getMemorySize())) {
                newMemories.set(item.slot(), item.stack());
            }
        }

        memorableContainer.setMemories(newMemories);
        memorableContainer.setMemoryOverridden(input.getBooleanOr("IsMemoryOverridden", false));
        memorableContainer.setLastResult(input.read("LastResult", ItemStack.CODEC).orElse(ItemStack.EMPTY));
        input.read("LastRecipe", RECIPE_HOLDER_CODEC).ifPresent(memorableContainer::setRecipeUsed);
    }
}
