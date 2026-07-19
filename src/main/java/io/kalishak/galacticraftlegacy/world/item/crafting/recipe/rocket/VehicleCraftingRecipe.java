package io.kalishak.galacticraftlegacy.world.item.crafting.recipe.rocket;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.crafting.GalacticraftRecipeBookCategories;
import io.kalishak.galacticraftlegacy.world.item.crafting.VehicleCraftingBookCategory;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeSerializer;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.ResourceHandlerInput;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class VehicleCraftingRecipe implements Recipe<ResourceHandlerInput> {
    public static final MapCodec<VehicleCraftingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CommonInfo.MAP_CODEC.forGetter(recipe -> recipe.commonInfo),
            VehicleCraftingDataRecipe.CODEC.fieldOf("recipe_holder").forGetter(recipe -> recipe.recipeHolder),
            VehicleCraftingBookInfo.MAP_CODEC.forGetter(recipe -> recipe.bookInfo)
    ).apply(instance, VehicleCraftingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, VehicleCraftingRecipe> STREAM_CODEC = StreamCodec.composite(
            CommonInfo.STREAM_CODEC, recipe -> recipe.commonInfo,
            VehicleCraftingDataRecipe.STREAM_CODEC, recipe -> recipe.recipeHolder,
            VehicleCraftingBookInfo.STREAM_CODEC, recipe -> recipe.bookInfo,
            VehicleCraftingRecipe::new
    );

    private final Recipe.CommonInfo commonInfo;
    private final Holder<VehicleCraftingDataRecipe> recipeHolder;
    private final VehicleCraftingBookInfo bookInfo;
    private @Nullable PlacementInfo placementInfo;

    public VehicleCraftingRecipe(Recipe.CommonInfo commonInfo, Holder<VehicleCraftingDataRecipe> recipeHolder, VehicleCraftingBookInfo bookInfo) {
        this.commonInfo = commonInfo;
        this.recipeHolder = recipeHolder;
        this.bookInfo = bookInfo;
    }

    @Override
    public boolean matches(ResourceHandlerInput resourceHandlerInput, Level level) {
        VehicleCraftingDataRecipe recipeData = this.recipeHolder.value();

        for (VehicleCraftingEntry entry : recipeData.inputSlots()) {
            ItemStack stackAtSlot = resourceHandlerInput.getItem(entry.slotIndex());

            Optional<Ingredient> ingredient = entry.input();

            if (ingredient.isPresent() && !ingredient.get().test(stackAtSlot)) {
                return false;
            } else if (!entry.isOutputSlot()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(ResourceHandlerInput resourceHandlerInput) {
        ItemStackTemplate template = this.recipeHolder.value().resultItem();
        DataComponentPatch.Builder patchBuilder = DataComponentPatch.builder();
        int size = getStorageSlotCount(resourceHandlerInput);

        if (size > 0) {
            patchBuilder.set(GalacticraftDataComponents.VEHICLE_STORAGE.get(), size);
        }

        return template.apply(patchBuilder.build());
    }

    private int getStorageSlotCount(ResourceHandlerInput resourceHandlerInput) {
        return this.recipeHolder.value().inputSlots().stream()
                .filter(entry -> entry.slotType().is(VehicleCraftingSlotTypes.STORAGE) && entry.input().isPresent())
                .filter(entry -> entry.input().get().test(ItemUtil.getStack(resourceHandlerInput.delegate(), entry.slotIndex())))
                .toArray().length;
    }

    @Override
    public boolean showNotification() {
        return this.commonInfo.showNotification();
    }

    @Override
    public String group() {
        return this.bookInfo.group;
    }

    @Override
    public RecipeSerializer<VehicleCraftingRecipe> getSerializer() {
        return GalacticraftRecipeSerializer.VEHICLE_CRAFTING.get();
    }

    @Override
    public RecipeType<VehicleCraftingRecipe> getType() {
        return GalacticraftRecipeType.VEHICLE_CRAFTING.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.createFromOptionals(this.recipeHolder.value().inputSlots().stream().map(VehicleCraftingEntry::input).toList());
        }

        return this.placementInfo;
    }

    @Override
    public List<RecipeDisplay> display() {
        return Recipe.super.display();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return switch (this.bookInfo.category) {
            case MISC -> GalacticraftRecipeBookCategories.CRAFTING_MISC_VEHICLES.get();
            case ROCKET -> GalacticraftRecipeBookCategories.CRAFTING_ROCKET.get();
            case LAND_VEHICLE -> GalacticraftRecipeBookCategories.CRAFTING_LAND_VEHICLE.get();
            case FLOATING_VEHICLE -> GalacticraftRecipeBookCategories.CRAFTING_FLOATING_VEHICLES.get();
        };
    }

    public record VehicleCraftingBookInfo(VehicleCraftingBookCategory category, String group) implements Recipe.BookInfo<VehicleCraftingBookCategory> {
        public static final MapCodec<VehicleCraftingBookInfo> MAP_CODEC = BookInfo.mapCodec(VehicleCraftingBookCategory.CODEC, VehicleCraftingBookCategory.MISC, VehicleCraftingBookInfo::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, VehicleCraftingBookInfo> STREAM_CODEC = BookInfo.streamCodec(VehicleCraftingBookCategory.STREAM_CODEC, VehicleCraftingBookInfo::new);
    }
}
