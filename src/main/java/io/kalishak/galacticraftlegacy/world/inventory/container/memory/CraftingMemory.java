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
import io.kalishak.galacticraftlegacy.data.TriStateBoolean;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public record CraftingMemory(List<ItemResource> memories, TriStateBoolean isOverridden, ItemStack lastResult, Optional<RecipeHolder<?>> lastRecipe) implements TooltipProvider {
    public static final MapCodec<CraftingMemory> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemResource.CODEC.listOf().fieldOf("memories").forGetter(CraftingMemory::memories),
            TriStateBoolean.CODEC.optionalFieldOf("overridden", TriStateBoolean.NONE).forGetter(CraftingMemory::isOverridden),
            ItemStack.OPTIONAL_CODEC.fieldOf("last_result").forGetter(CraftingMemory::lastResult),
            MemorableContainer.RECIPE_HOLDER_CODEC.optionalFieldOf("last_recipe").forGetter(CraftingMemory::lastRecipe)
    ).apply(instance, CraftingMemory::new));
    public static final Codec<CraftingMemory> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, CraftingMemory> STREAM_CODEC = StreamCodec.composite(
            ItemResource.STREAM_CODEC.apply(ByteBufCodecs.list()), CraftingMemory::memories,
            TriStateBoolean.STREAM_CODEC, CraftingMemory::isOverridden,
            ItemStack.OPTIONAL_STREAM_CODEC, CraftingMemory::lastResult,
            RecipeHolder.STREAM_CODEC.apply(ByteBufCodecs::optional), CraftingMemory::lastRecipe,
            CraftingMemory::new
    );

    @Override
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
        if (!this.lastResult.isEmpty()) {
            consumer.accept(Component.translatable("item.galacticraftlegacy.crafting_memory.tooltip", this.lastResult.getItemName()));
        }
    }
}
