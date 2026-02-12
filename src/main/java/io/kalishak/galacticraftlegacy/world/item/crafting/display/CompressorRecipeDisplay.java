package io.kalishak.galacticraftlegacy.world.item.crafting.display;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

public record CompressorRecipeDisplay(int width, int height, List<SlotDisplay> ingredients, SlotDisplay energySource, SlotDisplay result, SlotDisplay craftingStation, float experience, int compressingTime) implements RecipeDisplay {
    public static final MapCodec<CompressorRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("width").forGetter(CompressorRecipeDisplay::width),
            Codec.INT.fieldOf("height").forGetter(CompressorRecipeDisplay::height),
            SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(CompressorRecipeDisplay::ingredients),
            SlotDisplay.CODEC.fieldOf("energy_source").forGetter(CompressorRecipeDisplay::energySource),
            SlotDisplay.CODEC.fieldOf("result").forGetter(CompressorRecipeDisplay::result),
            SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(CompressorRecipeDisplay::craftingStation),
            Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(CompressorRecipeDisplay::experience),
            Codec.INT.fieldOf("compressing_time").forGetter(CompressorRecipeDisplay::compressingTime)
    ).apply(instance, CompressorRecipeDisplay::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CompressorRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, CompressorRecipeDisplay::width,
            ByteBufCodecs.VAR_INT, CompressorRecipeDisplay::height,
            SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()), CompressorRecipeDisplay::ingredients,
            SlotDisplay.STREAM_CODEC, CompressorRecipeDisplay::energySource,
            SlotDisplay.STREAM_CODEC, CompressorRecipeDisplay::result,
            SlotDisplay.STREAM_CODEC, CompressorRecipeDisplay::craftingStation,
            ByteBufCodecs.FLOAT, CompressorRecipeDisplay::experience,
            ByteBufCodecs.VAR_INT, CompressorRecipeDisplay::compressingTime,
            CompressorRecipeDisplay::new
    );

    @Override
    public Type<CompressorRecipeDisplay> type() {
        return GalacticraftRecipeDisplay.COMPRESSOR.get();
    }
}
