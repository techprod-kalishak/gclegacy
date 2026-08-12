/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.container.memory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.data.TriStateBoolean;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface MemorableContainer extends RecipeCraftingHolder {
    Codec<RecipeHolder<?>> RECIPE_HOLDER_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.RECIPE).fieldOf("id").forGetter(RecipeHolder::id),
            Recipe.CODEC.fieldOf("value").forGetter(RecipeHolder::value)
    ).apply(instance, RecipeHolder::new));

    int getMemorySize();
    int getOutputSlotIndex();

    NonNullList<ItemResource> getMemories();
    void setMemories(List<ItemResource> memories);

    boolean overrideMemory(@NonNull ItemResource newMemory, NonNullList<ItemResource> memories);
    void updateMemory(@NonNull ItemStack resultStack);

    @NonNull ItemStack getLastResult();
    void setLastResult(@NonNull ItemStack resultStack);

    boolean isMemoryOverridden();
    void setMemoryOverridden(boolean isMemoryOverridden);
    void updateOverrideStatus();

    default void readFromComponent(DataComponentGetter components) {
        CraftingMemory craftingMemory = components.get(GalacticraftDataComponents.CRAFTING_MEMORY);

        if (craftingMemory != null && !craftingMemory.memories().isEmpty()) {
            setMemories(craftingMemory.memories());
            craftingMemory.isOverridden().asOptional().ifPresent(this::setMemoryOverridden);
            setLastResult(craftingMemory.lastResult());
            craftingMemory.lastRecipe().ifPresent(this::setRecipeUsed);
        }
        updateOverrideStatus();
    }

    default @Nullable CraftingMemory saveAsComponent() {
        var memories = getMemories();

        if (!memories.isEmpty()) {
            return new CraftingMemory(
                    memories,
                    TriStateBoolean.of(isMemoryOverridden()),
                    getLastResult(),
                    Optional.ofNullable(getRecipeUsed())
            );
        }

        return null;
    }

    default void saveMemories(ValueOutput output) {
        NonNullList<ItemResource> items = getMemories();
        ValueOutput.TypedOutputList<ItemStackWithSlot> itemsOutput = output.list("Memories", ItemStackWithSlot.CODEC);

        for(int i = 0; i < items.size(); ++i) {
            ItemStack itemStack = items.get(i).toStack();

            if (!itemStack.isEmpty()) {
                itemsOutput.add(new ItemStackWithSlot(i, itemStack));
            }
        }

        if (itemsOutput.isEmpty()) {
            output.discard("Memories");
        }

        ItemStack memory = getLastResult();

        if (!memory.isEmpty()) {
            output.store("LastResult", ItemStack.CODEC, memory);
        }

        RecipeHolder<?> lastRecipe = getRecipeUsed();

        if (lastRecipe != null) {
            output.store("LastRecipe", RECIPE_HOLDER_CODEC, lastRecipe);
        }
    }

    default void loadMemories(ValueInput input) {
        ValueInput.TypedInputList<ItemStackWithSlot> items = input.listOrEmpty("Items", ItemStackWithSlot.CODEC);
        NonNullList<ItemResource> newMemories = NonNullList.withSize(getMemorySize(), ItemResource.EMPTY);

        for (ItemStackWithSlot item : items) {
            if (item.isValidInContainer(getMemorySize())) {
                newMemories.set(item.slot(), ItemResource.of(item.stack()));
            }
        }

        setMemories(newMemories);
        setLastResult(input.read("LastResult", ItemStack.CODEC).orElse(ItemStack.EMPTY));
        input.read("LastRecipe", RECIPE_HOLDER_CODEC).ifPresent(this::setRecipeUsed);
        updateOverrideStatus();
    }
}
