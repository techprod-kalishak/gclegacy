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
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.crafting.FabricatingBookCategory;
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
import net.minecraft.world.item.ItemStackTemplate;
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
    public static final MapCodec<CircuitRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CommonInfo.MAP_CODEC.forGetter(circuitRecipe -> circuitRecipe.commonInfo),
            FabricatingBookInfo.MAP_CODEC.forGetter(circuitRecipe -> circuitRecipe.bookInfo),
            Ingredient.CODEC.fieldOf("ingredients").forGetter(circuitRecipe -> circuitRecipe.ingredient),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(CircuitRecipe::result)
    ).apply(instance, CircuitRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CircuitRecipe> STREAM_CODEC = StreamCodec.composite(
            CommonInfo.STREAM_CODEC, circuitRecipe -> circuitRecipe.commonInfo,
            FabricatingBookInfo.STREAM_CODEC, circuitRecipe -> circuitRecipe.bookInfo,
            Ingredient.CONTENTS_STREAM_CODEC, circuitRecipe -> circuitRecipe.ingredient,
            ItemStackTemplate.STREAM_CODEC, CircuitRecipe::result,
            CircuitRecipe::new
    );

    private final FabricatingBookInfo bookInfo;
    private final Ingredient ingredient;

    public CircuitRecipe(CommonInfo commonInfo, FabricatingBookInfo bookInfo, Ingredient ingredient, ItemStackTemplate result) {
        super(commonInfo, result);
        this.bookInfo = bookInfo;
        this.ingredient = ingredient;
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
    public ItemStack disassembleIngredients(SimpleResourceInput simpleResourceInput, @Nullable Transaction tx, HolderGetter.Provider registries, boolean simulate) {
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
                if (!items.get(i).acceptsItem(simpleResourceInput.getResource(i).typeHolder())) {
                    return ItemStack.EMPTY;
                }
            }
        }

        return assemble(simpleResourceInput);
    }

    @Override
    public boolean matches(SimpleResourceInput input, Level level) {
        return this.ingredient.acceptsItem(input.getItem(CircuitFabricatorBlockEntity.SLOT_INGREDIENT).typeHolder());
    }

    @Override
    public String group() {
        return this.bookInfo.group;
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

    public record FabricatingBookInfo(FabricatingBookCategory category, String group) implements Recipe.BookInfo<FabricatingBookCategory> {
        public static final MapCodec<FabricatingBookInfo> MAP_CODEC = Recipe.BookInfo.mapCodec(FabricatingBookCategory.CODEC, FabricatingBookCategory.MISC, FabricatingBookInfo::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, FabricatingBookInfo> STREAM_CODEC = Recipe.BookInfo.streamCodec(FabricatingBookCategory.STREAM_CODEC, FabricatingBookInfo::new);
    }
}
