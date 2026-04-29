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
import io.kalishak.galacticraftlegacy.world.item.crafting.GalacticraftRecipeBookCategories;
import io.kalishak.galacticraftlegacy.world.item.crafting.StaticRecipePattern;
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

public class AnvilCompressingRecipe extends CompressingRecipe {
    public static final MapCodec<AnvilCompressingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.optionalFieldOf("group", "").forGetter(AnvilCompressingRecipe::group),
            StaticRecipePattern.MAP_CODEC.fieldOf("pattern").forGetter(compressingRecipe -> compressingRecipe.pattern),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(AnvilCompressingRecipe::result),
            Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(AnvilCompressingRecipe::experience),
            Codec.INT.optionalFieldOf("compressing_time", 200).forGetter(AnvilCompressingRecipe::compressingTime)
    ).apply(instance, AnvilCompressingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, AnvilCompressingRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, AnvilCompressingRecipe::group,
            StaticRecipePattern.STREAM_CODEC, compressingRecipe -> compressingRecipe.pattern,
            ItemStackTemplate.STREAM_CODEC, AnvilCompressingRecipe::result,
            ByteBufCodecs.FLOAT, AnvilCompressingRecipe::experience,
            ByteBufCodecs.INT, AnvilCompressingRecipe::compressingTime,
            AnvilCompressingRecipe::new
    );
    private final float experience;

    protected AnvilCompressingRecipe(String group, StaticRecipePattern pattern, ItemStackTemplate result, float experience, int compressingTime) {
        super(group, pattern, result, compressingTime);
        this.experience = experience;
    }

    @Override
    public RecipeSerializer<AnvilCompressingRecipe> getSerializer() {
        return GalacticraftRecipeSerializer.COMPRESSING.get();
    }

    @Override
    public RecipeType<AnvilCompressingRecipe> getType() {
        return GalacticraftRecipeType.COMPRESSING.get();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return GalacticraftRecipeBookCategories.COMPRESSING.get();
    }

    public float experience() {
        return this.experience;
    }

    @Override
    protected Holder<Item> icon() {
        return GalacticraftItems.COMPRESSOR;
    }

    @Override
    protected SlotDisplay energySource() {
        return SlotDisplay.AnyFuel.INSTANCE;
    }
}
