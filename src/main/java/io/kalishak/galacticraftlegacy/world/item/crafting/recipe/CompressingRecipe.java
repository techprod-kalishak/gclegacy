package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.item.crafting.StaticRecipePattern;
import io.kalishak.galacticraftlegacy.world.item.crafting.display.CompressorRecipeDisplay;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.CompressingRecipeInput;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public abstract class CompressingRecipe implements Recipe<CompressingRecipeInput> {
    public final StaticRecipePattern pattern;
    protected final String group;
    protected final ItemStack result;
    protected final float experience;
    protected final int compressingTime;
    protected @Nullable PlacementInfo placementInfo;

    protected CompressingRecipe(String group, StaticRecipePattern pattern, ItemStack result, float experience, int compressingTime) {
        this.group = group;
        this.pattern = pattern;
        this.result = result;
        this.experience = experience;
        this.compressingTime = compressingTime;
    }

    @Override
    public abstract RecipeSerializer<? extends CompressingRecipe> getSerializer();

    @Override
    public abstract RecipeType<? extends CompressingRecipe> getType();

    protected abstract Holder<Item> icon();

    protected abstract SlotDisplay energySource();

    public float experience() {
        return this.experience;
    }

    public int compressingTime() {
        return this.compressingTime;
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.createFromOptionals(ingredients());
        }

        return this.placementInfo;
    }

    public List<Optional<Ingredient>> ingredients() {
        return this.pattern.ingredients();
    }

    @Override
    public boolean matches(CompressingRecipeInput input, Level level) {
        return this.pattern.matches(input);
    }

    @Override
    public ItemStack assemble(CompressingRecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    public int width() {
        return this.pattern.width();
    }

    public int height() {
        return this.pattern.height();
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(
                new CompressorRecipeDisplay(
                        width(),
                        height(),
                        ingredients().stream().map(optionalIngredient -> optionalIngredient.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE)).toList(),
                        energySource(),
                        new SlotDisplay.ItemStackSlotDisplay(this.result),
                        new SlotDisplay.ItemSlotDisplay(icon()),
                        experience(),
                        compressingTime()
                )
        );
    }

    @FunctionalInterface
    public interface Factory<T extends CompressingRecipe> {
        T create(String group, StaticRecipePattern pattern, ItemStack result, float experience, int compressingTime);
    }

    public static class Serializer<T extends CompressingRecipe> implements RecipeSerializer<T> {
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        public Serializer(Factory<T> factory, int defaultCompressingTime) {
            this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(CompressingRecipe::group),
                    StaticRecipePattern.MAP_CODEC.forGetter(compressingRecipe -> compressingRecipe.pattern),
                    ItemStack.CODEC.fieldOf("result").forGetter(compressingRecipe -> compressingRecipe.result),
                    Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(CompressingRecipe::experience),
                    Codec.INT.optionalFieldOf("compressing_time", defaultCompressingTime).forGetter(CompressingRecipe::compressingTime)
            ).apply(instance, factory::create));
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, CompressingRecipe::group,
                    StaticRecipePattern.STREAM_CODEC, compressingRecipe -> compressingRecipe.pattern,
                    ItemStack.STREAM_CODEC, compressingRecipe -> compressingRecipe.result,
                    ByteBufCodecs.FLOAT, CompressingRecipe::experience,
                    ByteBufCodecs.VAR_INT, CompressingRecipe::compressingTime,
                    factory::create
            );
        }

        @Override
        public MapCodec<T> codec() {
            return this.codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
            return this.streamCodec;
        }
    }
}
