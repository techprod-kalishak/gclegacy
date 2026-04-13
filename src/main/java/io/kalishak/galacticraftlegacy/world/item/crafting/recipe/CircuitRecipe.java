package io.kalishak.galacticraftlegacy.world.item.crafting.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.crafting.GalacticraftRecipeBookCategories;
import io.kalishak.galacticraftlegacy.world.item.crafting.display.CircutFabricatorRecipeDisplay;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.SimpleResourceInput;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.CircuitFabricatorBlockEntity;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class CircuitRecipe extends MachineRecipe<SimpleResourceInput> {
    private final Ingredient ingredient;
    private final boolean isClassicRecipe; //TODO make it usable

    public CircuitRecipe(String group, Ingredient ingredient, ItemStack result, boolean isClassicRecipe) {
        super(group, result);
        this.ingredient = ingredient;
        this.isClassicRecipe = isClassicRecipe;
    }

    @Override
    public RecipeSerializer<CircuitRecipe> getSerializer() {
        return GalacticraftRecipeSerializer.CIRCUIT.get();
    }

    @Override
    public RecipeType<CircuitRecipe> getType() {
        return GalacticraftRecipeType.CIRCUIT.get();
    }

    @Override
    public ItemStack disassembleIngredients(SimpleResourceInput simpleResourceInput, @Nullable Transaction tx, HolderLookup.Provider registries, boolean simulate) {
        if (!simulate) {
            try (Transaction childTx = Transaction.open(tx)) {
                for (int i = 0; i < simpleResourceInput.size() - 1; i++) {
                    ItemResource resource = simpleResourceInput.getResource(i);

                    if (simpleResourceInput.extract(i, resource, 1, childTx) != 1) {
                        return ItemStack.EMPTY;
                    }
                }

                childTx.commit();
            }
        } else {
            NonNullList<Ingredient> items = withBase(registries.lookupOrThrow(Registries.ITEM), this.ingredient);

            for (int i = 0; i < simpleResourceInput.size() - 1; i++) {
                if (!items.get(i).acceptsItem(simpleResourceInput.getResource(i).getHolder())) {
                    return ItemStack.EMPTY;
                }
            }
        }

        return assemble(simpleResourceInput, registries);
    }

    @Override
    public boolean matches(SimpleResourceInput input, Level level) {
        return this.ingredient.acceptsItem(input.getItem(CircuitFabricatorBlockEntity.SLOT_INGREDIENT).getItemHolder());
    }

    private static NonNullList<Ingredient> withBase(HolderGetter<Item> itemHolderGetter, Ingredient mainIngredient) {
        HolderSet<Item> siliconTag = itemHolderGetter.getOrThrow(GalacticraftTags.Items.RAW_MATERIALS_SILICON);
        NonNullList<Ingredient> list = NonNullList.withSize(5, Ingredient.of(Items.STONE));

        list.set(0, Ingredient.of(itemHolderGetter.getOrThrow(Tags.Items.GEMS_DIAMOND)));
        list.set(1, Ingredient.of(siliconTag));
        list.set(2, Ingredient.of(siliconTag));
        list.set(3, Ingredient.of(Items.REDSTONE));
        list.set(4, mainIngredient);

        return list;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(this.ingredient);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return GalacticraftRecipeBookCategories.FABRICATING.get();
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(
                new CircutFabricatorRecipeDisplay(
                        new SlotDisplay.ItemSlotDisplay(GalacticraftItems.BATTERY),
                        Ingredient.of(Items.DIAMOND).display(),
                        Ingredient.of(GalacticraftItems.RAW_SILICON).display(),
                        Ingredient.of(GalacticraftItems.RAW_SILICON).display(),
                        Ingredient.of(Items.REDSTONE).display(),
                        this.ingredient.display(),
                        new SlotDisplay.ItemStackSlotDisplay(result()),
                        new SlotDisplay.ItemSlotDisplay(GalacticraftItems.CIRCUIT_FABRICATOR)
                )
        );
    }

    private boolean isClassicRecipe() {
        return this.isClassicRecipe;
    }

    public static class Serializer implements RecipeSerializer<CircuitRecipe> {
        public static final MapCodec<CircuitRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(CircuitRecipe::group),
                Ingredient.CODEC.fieldOf("ingredients").forGetter(circuitRecipe -> circuitRecipe.ingredient),
                ItemStack.CODEC.fieldOf("result").forGetter(CircuitRecipe::result),
                Codec.BOOL.optionalFieldOf("is_classic_recipe", true).forGetter(CircuitRecipe::isClassicRecipe)
        ).apply(instance, CircuitRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, CircuitRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, CircuitRecipe::group,
                Ingredient.CONTENTS_STREAM_CODEC, circuitRecipe -> circuitRecipe.ingredient,
                ItemStack.STREAM_CODEC, CircuitRecipe::result,
                ByteBufCodecs.BOOL, CircuitRecipe::isClassicRecipe,
                CircuitRecipe::new
        );

        @Override
        public MapCodec<CircuitRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CircuitRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
