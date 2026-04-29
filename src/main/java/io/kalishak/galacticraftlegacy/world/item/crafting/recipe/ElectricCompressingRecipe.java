/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.crafting.GalacticraftRecipeBookCategories;
import io.kalishak.galacticraftlegacy.world.item.crafting.StaticRecipePattern;
import io.kalishak.galacticraftlegacy.world.item.crafting.display.BatterySlotDisplay;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public class ElectricCompressingRecipe extends CompressingRecipe {
    public static final MapCodec<ElectricCompressingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.optionalFieldOf("group", "").forGetter(ElectricCompressingRecipe::group),
            StaticRecipePattern.MAP_CODEC.fieldOf("pattern").forGetter(compressingRecipe -> compressingRecipe.pattern),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(ElectricCompressingRecipe::result),
            Codec.INT.optionalFieldOf("compressing_time", 200).forGetter(ElectricCompressingRecipe::compressingTime)
    ).apply(instance, ElectricCompressingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ElectricCompressingRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ElectricCompressingRecipe::group,
            StaticRecipePattern.STREAM_CODEC, compressingRecipe -> compressingRecipe.pattern,
            ItemStackTemplate.STREAM_CODEC, ElectricCompressingRecipe::result,
            ByteBufCodecs.INT, ElectricCompressingRecipe::compressingTime,
            ElectricCompressingRecipe::new
    );

    public ElectricCompressingRecipe(String group, StaticRecipePattern pattern, ItemStackTemplate result, int compressingTime) {
        super(group, pattern, result, compressingTime);
    }

    @Override
    public RecipeSerializer<ElectricCompressingRecipe> getSerializer() {
        return GalacticraftRecipeSerializer.ELECTRIC_COMPRESSING.get();
    }

    @Override
    public RecipeType<ElectricCompressingRecipe> getType() {
        return GalacticraftRecipeType.ELECTRIC_COMPRESSING.get();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return GalacticraftRecipeBookCategories.ELECTRIC_COMPRESSING.get();
    }

    @Override
    protected Holder<Item> icon() {
        return GalacticraftItems.ELECTRIC_COMPRESSOR;
    }

    @Override
    protected SlotDisplay energySource() {
        return new BatterySlotDisplay(
                new SlotDisplay.ItemSlotDisplay(GalacticraftItems.BATTERY),
                new SlotDisplay.OnlyWithComponent(
                        new SlotDisplay.ItemSlotDisplay(GalacticraftItems.BATTERY),
                        GalacticraftDataComponents.STORED_ENERGY.get()
                ),
                1000
        );
    }
}
