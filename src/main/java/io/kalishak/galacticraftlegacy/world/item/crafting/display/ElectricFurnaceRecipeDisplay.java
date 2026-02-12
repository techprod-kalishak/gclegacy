package io.kalishak.galacticraftlegacy.world.item.crafting.display;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public record ElectricFurnaceRecipeDisplay(SlotDisplay ingredient, SlotDisplay battery, SlotDisplay result, SlotDisplay craftingStation, int cookingTime) implements RecipeDisplay {
    public static final MapCodec<ElectricFurnaceRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SlotDisplay.CODEC.fieldOf("ingredient").forGetter(ElectricFurnaceRecipeDisplay::ingredient),
            SlotDisplay.CODEC.fieldOf("battery").forGetter(ElectricFurnaceRecipeDisplay::battery),
            SlotDisplay.CODEC.fieldOf("result").forGetter(ElectricFurnaceRecipeDisplay::result),
            SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(ElectricFurnaceRecipeDisplay::craftingStation),
            Codec.INT.fieldOf("cooking_time").forGetter(ElectricFurnaceRecipeDisplay::cookingTime)
    ).apply(instance, ElectricFurnaceRecipeDisplay::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ElectricFurnaceRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC, ElectricFurnaceRecipeDisplay::ingredient,
            SlotDisplay.STREAM_CODEC, ElectricFurnaceRecipeDisplay::battery,
            SlotDisplay.STREAM_CODEC, ElectricFurnaceRecipeDisplay::result,
            SlotDisplay.STREAM_CODEC, ElectricFurnaceRecipeDisplay::craftingStation,
            ByteBufCodecs.INT, ElectricFurnaceRecipeDisplay::cookingTime,
            ElectricFurnaceRecipeDisplay::new
    );

    @Override
    public Type<ElectricFurnaceRecipeDisplay> type() {
        return GalacticraftRecipeDisplay.ELECTRIC_FURNACE.get();
    }
}
