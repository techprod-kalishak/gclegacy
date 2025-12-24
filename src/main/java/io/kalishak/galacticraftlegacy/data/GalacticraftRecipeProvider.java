package io.kalishak.galacticraftlegacy.data;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.Nullable;

import java.awt.event.ItemListener;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class GalacticraftRecipeProvider extends RecipeProvider {
    GalacticraftRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        parachute(GalacticraftItems.BLACK_PARACHUTE, Items.BLACK_WOOL);
        parachute(GalacticraftItems.BLUE_PARACHUTE, Items.BLUE_WOOL);
        parachute(GalacticraftItems.BROWN_PARACHUTE, Items.BROWN_WOOL);
        parachute(GalacticraftItems.CYAN_PARACHUTE, Items.CYAN_WOOL);
        parachute(GalacticraftItems.GRAY_PARACHUTE, Items.GRAY_WOOL);
        parachute(GalacticraftItems.GREEN_PARACHUTE, Items.GREEN_WOOL);
        parachute(GalacticraftItems.LIGHT_BLUE_PARACHUTE, Items.LIGHT_BLUE_WOOL);
        parachute(GalacticraftItems.LIGHT_GRAY_PARACHUTE, Items.LIGHT_GRAY_WOOL);
        parachute(GalacticraftItems.LIME_PARACHUTE, Items.LIME_WOOL);
        parachute(GalacticraftItems.MAGENTA_PARACHUTE, Items.MAGENTA_WOOL);
        parachute(GalacticraftItems.ORANGE_PARACHUTE, Items.ORANGE_WOOL);
        parachute(GalacticraftItems.PINK_PARACHUTE, Items.PINK_WOOL);
        parachute(GalacticraftItems.PURPLE_PARACHUTE, Items.PURPLE_WOOL);
        parachute(GalacticraftItems.RED_PARACHUTE, Items.RED_WOOL);
        parachute(GalacticraftItems.WHITE_PARACHUTE, Items.WHITE_WOOL);
        parachute(GalacticraftItems.YELLOW_PARACHUTE, Items.YELLOW_WOOL);

        List<Item> dyes = List.of(
                Items.BLACK_DYE,
                Items.BLUE_DYE,
                Items.BROWN_DYE,
                Items.CYAN_DYE,
                Items.GRAY_DYE,
                Items.GREEN_DYE,
                Items.LIGHT_BLUE_DYE,
                Items.LIGHT_GRAY_DYE,
                Items.LIME_DYE,
                Items.MAGENTA_DYE,
                Items.ORANGE_DYE,
                Items.PINK_DYE,
                Items.PURPLE_DYE,
                Items.RED_DYE,
                Items.YELLOW_DYE,
                Items.WHITE_DYE
        );
        List<Item> parachutes = List.of(
                GalacticraftItems.BLACK_PARACHUTE.get(),
                GalacticraftItems.BLUE_PARACHUTE.get(),
                GalacticraftItems.BROWN_PARACHUTE.get(),
                GalacticraftItems.CYAN_PARACHUTE.get(),
                GalacticraftItems.GRAY_PARACHUTE.get(),
                GalacticraftItems.GREEN_PARACHUTE.get(),
                GalacticraftItems.LIGHT_BLUE_PARACHUTE.get(),
                GalacticraftItems.LIGHT_GRAY_PARACHUTE.get(),
                GalacticraftItems.LIME_PARACHUTE.get(),
                GalacticraftItems.MAGENTA_PARACHUTE.get(),
                GalacticraftItems.ORANGE_PARACHUTE.get(),
                GalacticraftItems.PINK_PARACHUTE.get(),
                GalacticraftItems.PURPLE_PARACHUTE.get(),
                GalacticraftItems.RED_PARACHUTE.get(),
                GalacticraftItems.YELLOW_PARACHUTE.get(),
                GalacticraftItems.WHITE_PARACHUTE.get()
        );
        colorItemWithDye(dyes, parachutes, "parachute_dye", RecipeCategory.TOOLS);
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_CLOTH)
                .define('W', ItemTags.WOOL)
                .define('R', Items.REDSTONE)
                .pattern(" W ")
                .pattern("WRW")
                .pattern(" W ")
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(this.output, Galacticraft.key(Registries.RECIPE, getItemName(GalacticraftItems.THERMAL_CLOTH)));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_CAP)
                .define('#', GalacticraftItems.THERMAL_CLOTH)
                .pattern("###")
                .pattern("# #")
                .unlockedBy(getHasName(GalacticraftItems.THERMAL_CLOTH), has(GalacticraftItems.THERMAL_CLOTH))
                .save(this.output, Galacticraft.key(Registries.RECIPE, getItemName(GalacticraftItems.THERMAL_CAP)));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_SHIRT)
                .define('#', GalacticraftItems.THERMAL_CLOTH)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(GalacticraftItems.THERMAL_CLOTH), has(GalacticraftItems.THERMAL_CLOTH))
                .save(this.output, Galacticraft.key(Registries.RECIPE, getItemName(GalacticraftItems.THERMAL_SHIRT)));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_LEGGINGS)
                .define('#', GalacticraftItems.THERMAL_CLOTH)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(GalacticraftItems.THERMAL_CLOTH), has(GalacticraftItems.THERMAL_CLOTH))
                .save(this.output, Galacticraft.key(Registries.RECIPE, getItemName(GalacticraftItems.THERMAL_LEGGINGS)));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_SOCKS)
                .define('#', GalacticraftItems.THERMAL_CLOTH)
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(GalacticraftItems.THERMAL_CLOTH), has(GalacticraftItems.THERMAL_CLOTH))
                .save(this.output, Galacticraft.key(Registries.RECIPE, getItemName(GalacticraftItems.THERMAL_SOCKS)));
    }

    @Override
    protected void colorWithDye(List<Item> dyes, List<Item> dyeableItems, @Nullable Item dye, String group, RecipeCategory category) {
        for (int i = 0; i < dyes.size(); i++) {
            Item item = dyes.get(i);
            Item item1 = dyeableItems.get(i);
            Stream<Item> stream = dyeableItems.stream().filter(p_288265_ -> !p_288265_.equals(item1));
            if (dye != null) {
                stream = Stream.concat(stream, Stream.of(dye));
            }

            this.shapeless(category, item1)
                    .requires(item)
                    .requires(Ingredient.of(stream))
                    .group(group)
                    .unlockedBy("has_needed_dye", this.has(item))
                    .save(this.output, Galacticraft.key(Registries.RECIPE, "dye_" + getItemName(item1)));
        }
    }

    private void parachute(ItemLike parachute, ItemLike woolItem) {
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.TOOLS, parachute)
                .group("parachute")
                .define('#', woolItem)
                .define('S', Items.STRING)
                .pattern("###")
                .pattern("S S")
                .pattern(" S ")
                .unlockedBy("discovered_gravity", has(Items.WHITE_WOOL))
                .save(this.output, Galacticraft.key(Registries.RECIPE, getItemName(parachute)));
    }

    public static class Runner extends RecipeProvider.Runner {
        Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new  GalacticraftRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Recipe Provider for Galacticraft Legacy";
        }
    }
}
