package io.kalishak.galacticraftlegacy.world.item.crafting.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public record CircutFabricatorRecipeDisplay(SlotDisplay battery, SlotDisplay diamond, SlotDisplay siliconLeft, SlotDisplay siliconRight, SlotDisplay redstone, SlotDisplay ingredient, SlotDisplay result, SlotDisplay craftingStation) implements RecipeDisplay {
    public static final MapCodec<CircutFabricatorRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SlotDisplay.CODEC.fieldOf("battery").forGetter(CircutFabricatorRecipeDisplay::battery),
            SlotDisplay.CODEC.fieldOf("diamond").forGetter(CircutFabricatorRecipeDisplay::diamond),
            SlotDisplay.CODEC.fieldOf("silicon_left").forGetter(CircutFabricatorRecipeDisplay::siliconLeft),
            SlotDisplay.CODEC.fieldOf("silicon_right").forGetter(CircutFabricatorRecipeDisplay::siliconRight),
            SlotDisplay.CODEC.fieldOf("redstone").forGetter(CircutFabricatorRecipeDisplay::redstone),
            SlotDisplay.CODEC.fieldOf("ingredient").forGetter(CircutFabricatorRecipeDisplay::ingredient),
            SlotDisplay.CODEC.fieldOf("result").forGetter(CircutFabricatorRecipeDisplay::result),
            SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(CircutFabricatorRecipeDisplay::craftingStation)
    ).apply(instance, CircutFabricatorRecipeDisplay::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CircutFabricatorRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC, CircutFabricatorRecipeDisplay::battery,
            SlotDisplay.STREAM_CODEC, CircutFabricatorRecipeDisplay::diamond,
            SlotDisplay.STREAM_CODEC, CircutFabricatorRecipeDisplay::siliconLeft,
            SlotDisplay.STREAM_CODEC, CircutFabricatorRecipeDisplay::siliconRight,
            SlotDisplay.STREAM_CODEC, CircutFabricatorRecipeDisplay::redstone,
            SlotDisplay.STREAM_CODEC, CircutFabricatorRecipeDisplay::ingredient,
            SlotDisplay.STREAM_CODEC, CircutFabricatorRecipeDisplay::result,
            SlotDisplay.STREAM_CODEC, CircutFabricatorRecipeDisplay::craftingStation,
            CircutFabricatorRecipeDisplay::new
    );
    public static final RecipeDisplay.Type<CircutFabricatorRecipeDisplay> TYPE = new Type<>(MAP_CODEC, STREAM_CODEC);

    public static SlotDisplay ofIndex(CircutFabricatorRecipeDisplay display, int index) {
        return switch (index) {
            case 0 -> display.battery();
            case 1 -> display.diamond();
            case 2 -> display.siliconLeft();
            case 3 -> display.siliconRight();
            case 4 -> display.redstone();
            case 5 -> display.ingredient();
            case 6 -> display.result();
            case 7 -> display.craftingStation();
            default -> throw new IndexOutOfBoundsException();
        };
    }

    @Override
    public Type<CircutFabricatorRecipeDisplay> type() {
        return GalacticraftRecipeDisplay.CIRCUIT_FABRICATOR.get();
    }
}
