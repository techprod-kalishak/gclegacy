/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.inventory.container.memory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;
import java.util.Optional;

public record CraftingMemory(List<ItemStack> memories, boolean isOverridden, ItemStack lastResult, Optional<RecipeHolder<?>> lastRecipe) {
    public static final MapCodec<CraftingMemory> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.listOf().fieldOf("memories").forGetter(CraftingMemory::memories),
            Codec.BOOL.optionalFieldOf("overridden", false).forGetter(CraftingMemory::isOverridden),
            ItemStack.OPTIONAL_CODEC.fieldOf("last_result").forGetter(CraftingMemory::lastResult),
            MemorableContainer.RECIPE_HOLDER_CODEC.optionalFieldOf("last_recipe").forGetter(CraftingMemory::lastRecipe)
    ).apply(instance, CraftingMemory::new));
    public static final Codec<CraftingMemory> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, CraftingMemory> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_LIST_STREAM_CODEC, CraftingMemory::memories,
            ByteBufCodecs.BOOL, CraftingMemory::isOverridden,
            ItemStack.OPTIONAL_STREAM_CODEC, CraftingMemory::lastResult,
            RecipeHolder.STREAM_CODEC.apply(ByteBufCodecs::optional), CraftingMemory::lastRecipe,
            CraftingMemory::new
    );
    public static final CraftingMemory FORGOTTEN = new CraftingMemory(List.of(), false, ItemStack.EMPTY, Optional.empty());
}
