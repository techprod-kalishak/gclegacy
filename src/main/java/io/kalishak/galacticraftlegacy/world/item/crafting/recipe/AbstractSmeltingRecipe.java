package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.item.crafting.GalacticraftRecipeBookCategories;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.SimpleResourceInput;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * A copy of {@link AbstractCookingRecipe} with {@link net.neoforged.neoforge.transfer.ResourceHandler} backed input
 */
public abstract class AbstractSmeltingRecipe extends MachineRecipe<SimpleResourceInput> {
    protected final CookingBookCategory category;
    protected final Ingredient ingredient;
    protected final int cookingTime;

    protected AbstractSmeltingRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, int cookingTime) {
        super(group, result);
        this.category = category;
        this.ingredient = ingredient;
        this.cookingTime = cookingTime;
    }

    @Override
    public abstract RecipeSerializer<? extends AbstractSmeltingRecipe> getSerializer();

    @Override
    public abstract RecipeType<? extends AbstractSmeltingRecipe> getType();

    @Override
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.create(this.ingredient);
        }

        return this.placementInfo;
    }

    @Override
    public boolean matches(SimpleResourceInput input, Level level) {
        return this.ingredient.acceptsItem(input.getResource(0).getHolder());
    }

    @Override
    public ItemStack disassembleIngredients(SimpleResourceInput resourceInput, @Nullable Transaction tx, HolderLookup.Provider registries, boolean simulate) {
        ItemResource itemResource = resourceInput.getResource(0);

        if (itemResource.isEmpty()) return ItemStack.EMPTY;

        try (Transaction childTx = Transaction.open(tx)) {
            if (this.ingredient.acceptsItem(itemResource.getHolder())) {
                if (resourceInput.extract(0, itemResource, 1, childTx) > 0) {

                    if (!simulate) {
                        childTx.commit();
                    }

                    return assemble(resourceInput, registries);
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return switch (category()) {
            case BLOCKS -> GalacticraftRecipeBookCategories.HEATING_BLOCKS.get();
            case FOOD -> GalacticraftRecipeBookCategories.HEATING_FOOD.get();
            case MISC -> GalacticraftRecipeBookCategories.HEATING_MISC.get();
        };
    }

    protected Ingredient ingredient() {
        return this.ingredient;
    }

    public int cookingTime() {
        return this.cookingTime;
    }

    public CookingBookCategory category() {
        return this.category;
    }

    protected abstract Holder<Item> icon();

    @Override
    public abstract List<RecipeDisplay> display();

    @FunctionalInterface
    public interface Factory<T extends AbstractSmeltingRecipe> {
        T create(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, int cookingTime);
    }

    public static class Serializer<T extends AbstractSmeltingRecipe> implements RecipeSerializer<T> {
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        public Serializer(Factory<T> factory, int defaultCookingTime) {
            this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(AbstractSmeltingRecipe::group),
                    CookingBookCategory.CODEC.fieldOf("category").orElse(CookingBookCategory.MISC).forGetter(AbstractSmeltingRecipe::category),
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(AbstractSmeltingRecipe::ingredient),
                    ItemStack.CODEC.fieldOf("result").forGetter(AbstractSmeltingRecipe::result),
                    Codec.INT.fieldOf("cookingtime").orElse(defaultCookingTime).forGetter(AbstractSmeltingRecipe::cookingTime)
            ).apply(instance, factory::create));
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, AbstractSmeltingRecipe::group,
                    CookingBookCategory.STREAM_CODEC, AbstractSmeltingRecipe::category,
                    Ingredient.CONTENTS_STREAM_CODEC, AbstractSmeltingRecipe::ingredient,
                    ItemStack.STREAM_CODEC, AbstractSmeltingRecipe::result,
                    ByteBufCodecs.INT, AbstractSmeltingRecipe::cookingTime,
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
