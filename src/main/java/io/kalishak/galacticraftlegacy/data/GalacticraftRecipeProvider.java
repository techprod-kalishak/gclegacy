package io.kalishak.galacticraftlegacy.data;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.data.recipes.builder.HeatingRecipeBuilder;
import io.kalishak.galacticraftlegacy.data.recipes.builder.SingleInputMachineRecipeBuilder;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class GalacticraftRecipeProvider extends RecipeProvider {
    GalacticraftRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        toolSet(GalacticraftTags.Items.INGOTS_STEEL,
                GalacticraftItems.STEEL_SWORD,
                GalacticraftItems.STEEL_SPEAR,
                GalacticraftItems.STEEL_SHOVEL,
                GalacticraftItems.STEEL_PICKAXE,
                GalacticraftItems.STEEL_AXE,
                GalacticraftItems.STEEL_HOE
        );
        armorSet(GalacticraftTags.Items.INGOTS_STEEL,
                GalacticraftItems.STEEL_HELMET,
                GalacticraftItems.STEEL_CHESTPLATE,
                GalacticraftItems.STEEL_LEGGINGS,
                GalacticraftItems.STEEL_BOOTS
        );
        nineBlockStorageRecipes(
                RecipeCategory.MISC,
                GalacticraftItems.STEEL_NUGGET,
                RecipeCategory.MISC,
                GalacticraftItems.STEEL_INGOT,
                getItemId(GalacticraftItems.STEEL_NUGGET) + "_from_steel_ingot",
                null,
                getItemId(GalacticraftItems.STEEL_INGOT) + "_from_steel_nugget",
                null
        );
        toolSet(GalacticraftTags.Items.INGOTS_DESH,
                GalacticraftItems.DESH_SWORD,
                GalacticraftItems.DESH_SPEAR,
                GalacticraftItems.DESH_SHOVEL,
                GalacticraftItems.DESH_PICKAXE,
                GalacticraftItems.DESH_AXE,
                GalacticraftItems.DESH_HOE
        );
        armorSet(GalacticraftTags.Items.INGOTS_DESH,
                GalacticraftItems.DESH_HELMET,
                GalacticraftItems.DESH_CHESTPLATE,
                GalacticraftItems.DESH_LEGGINGS,
                GalacticraftItems.DESH_BOOTS
        );
        nineBlockStorageRecipes(
                RecipeCategory.MISC,
                GalacticraftItems.DESH_NUGGET,
                RecipeCategory.MISC,
                GalacticraftItems.DESH_INGOT,
                getItemId(GalacticraftItems.DESH_NUGGET) + "_from_desh_ingot",
                null,
                getItemId(GalacticraftItems.DESH_INGOT) + "_from_desh_nugget",
                null
        );
        toolSet(GalacticraftTags.Items.INGOTS_TITANIUM,
                GalacticraftItems.TITANIUM_SWORD,
                GalacticraftItems.TITANIUM_SPEAR,
                GalacticraftItems.TITANIUM_SHOVEL,
                GalacticraftItems.TITANIUM_PICKAXE,
                GalacticraftItems.TITANIUM_AXE,
                GalacticraftItems.TITANIUM_HOE
        );
        armorSet(GalacticraftTags.Items.INGOTS_TITANIUM,
                GalacticraftItems.TITANIUM_HELMET,
                GalacticraftItems.TITANIUM_CHESTPLATE,
                GalacticraftItems.TITANIUM_LEGGINGS,
                GalacticraftItems.TITANIUM_BOOTS
        );
        nineBlockStorageRecipes(
                RecipeCategory.MISC,
                GalacticraftItems.TITANIUM_NUGGET,
                RecipeCategory.MISC,
                GalacticraftItems.TITANIUM_INGOT,
                getItemId(GalacticraftItems.TITANIUM_NUGGET) + "_from_titanium_ingot",
                null,
                getItemId(GalacticraftItems.TITANIUM_INGOT) + "_from_titanium_nugget",
                null
        );

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
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(GalacticraftItems.THERMAL_CLOTH)));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_PADDING_HELM)
                .define('#', GalacticraftItems.THERMAL_CLOTH)
                .pattern("###")
                .pattern("# #")
                .unlockedBy(getHasName(GalacticraftItems.THERMAL_CLOTH), has(GalacticraftItems.THERMAL_CLOTH))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(GalacticraftItems.THERMAL_PADDING_HELM)));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_PADDING_CHESTPIECE)
                .define('#', GalacticraftItems.THERMAL_CLOTH)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(GalacticraftItems.THERMAL_CLOTH), has(GalacticraftItems.THERMAL_CLOTH))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(GalacticraftItems.THERMAL_PADDING_CHESTPIECE)));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_PADDING_LEGGINGS)
                .define('#', GalacticraftItems.THERMAL_CLOTH)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(GalacticraftItems.THERMAL_CLOTH), has(GalacticraftItems.THERMAL_CLOTH))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(GalacticraftItems.THERMAL_PADDING_LEGGINGS)));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, GalacticraftItems.THERMAL_PADDING_BOOTS)
                .define('#', GalacticraftItems.THERMAL_CLOTH)
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(GalacticraftItems.THERMAL_CLOTH), has(GalacticraftItems.THERMAL_CLOTH))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(GalacticraftItems.THERMAL_PADDING_BOOTS)));

        SingleInputMachineRecipeBuilder.classic(GalacticraftItems.BASIC_WAFER.get(), 1, Ingredient.of(Items.REDSTONE_TORCH))
                .unlockedBy(getHasName(GalacticraftItems.RAW_SILICON), has(GalacticraftTags.Items.RAW_MATERIALS_SILICON))
                .save(output, Constants.key(Registries.RECIPE, "basic_wafer"));
        SingleInputMachineRecipeBuilder.classic(GalacticraftItems.ADVANCED_WAFER.get(), 1, Ingredient.of(Items.REPEATER))
                .unlockedBy(getHasName(GalacticraftItems.RAW_SILICON), has(GalacticraftTags.Items.RAW_MATERIALS_SILICON))
                .save(output, Constants.key(Registries.RECIPE, "advanced_wafer"));
        SingleInputMachineRecipeBuilder.classic(GalacticraftItems.SOLAR_WAFER.get(), 9, Ingredient.of(Items.LAPIS_LAZULI))
                .unlockedBy(getHasName(GalacticraftItems.RAW_SILICON), has(GalacticraftTags.Items.RAW_MATERIALS_SILICON))
                .save(output, Constants.key(Registries.RECIPE, "solar_panel"));
        electricFurnace("stone", RecipeCategory.BUILDING_BLOCKS, Items.STONE, Items.COBBLESTONE);
        electricFurnace("stone", RecipeCategory.BUILDING_BLOCKS, Items.SMOOTH_STONE, Items.STONE);

    }

    public void toolSet(TagKey<Item> ingredient, ItemLike sword, ItemLike spear, ItemLike shovel, ItemLike pickaxe, ItemLike axe, ItemLike hoe) {
        shaped(RecipeCategory.COMBAT, sword)
                .define('S', Items.STICK)
                .define('I', ingredient)
                .pattern("I")
                .pattern("I")
                .pattern("S")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(sword)));
        shaped(RecipeCategory.COMBAT, spear)
                .define('S', Items.STICK)
                .define('I', ingredient)
                .pattern("  I")
                .pattern(" I ")
                .pattern("S  ")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(spear)));
        shaped(RecipeCategory.TOOLS, shovel)
                .define('S', Items.STICK)
                .define('I', ingredient)
                .pattern("I")
                .pattern("S")
                .pattern("S")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(shovel)));
        shaped(RecipeCategory.TOOLS, pickaxe)
                .define('S', Items.STICK)
                .define('I', ingredient)
                .pattern("III")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(pickaxe)));
        shaped(RecipeCategory.TOOLS, axe)
                .define('S', Items.STICK)
                .define('I', ingredient)
                .pattern("II")
                .pattern("IS")
                .pattern(" S")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(axe)));
        shaped(RecipeCategory.TOOLS, hoe)
                .define('S', Items.STICK)
                .define('I', ingredient)
                .pattern("II")
                .pattern(" S")
                .pattern(" S")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(hoe)));
    }
    public void armorSet(TagKey<Item> ingredient, ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots) {
        shaped(RecipeCategory.COMBAT, helmet)
                .define('I', ingredient)
                .pattern("III")
                .pattern("I I")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(helmet)));
        shaped(RecipeCategory.COMBAT, chestplate)
                .define('I', ingredient)
                .pattern("I I")
                .pattern("III")
                .pattern("III")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(chestplate)));
        shaped(RecipeCategory.TOOLS, leggings)
                .define('I', ingredient)
                .pattern("III")
                .pattern("I I")
                .pattern("I I")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(leggings)));
        shaped(RecipeCategory.TOOLS, boots)
                .define('I', ingredient)
                .pattern("I I")
                .pattern("I I")
                .unlockedBy(getHasName(GalacticraftItems.STEEL_INGOT), has(ingredient))
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(boots)));
    }

    public void electricFurnace(@Nullable String group, RecipeCategory recipeCategory, ItemLike result, ItemLike ingredient) {
        Consumer<HeatingRecipeBuilder> common = builder -> builder.group(group).unlockedBy(getHasName(ingredient), has(ingredient));

        HeatingRecipeBuilder recipeBuilder = HeatingRecipeBuilder.heating(recipeCategory, result, Ingredient.of(ingredient));
        common.accept(recipeBuilder);
        recipeBuilder.save(output, Constants.key(Registries.RECIPE, getHeatingRecipeName(result, ingredient)));

        recipeBuilder = HeatingRecipeBuilder.arcHeating(recipeCategory, result, Ingredient.of(ingredient));
        common.accept(recipeBuilder);
        recipeBuilder.save(output, Constants.key(Registries.RECIPE, getArcHeatingRecipeName(result, ingredient)));
    }

    private String conversionName(ItemLike result, String conversionName, ItemLike ingredient) {
        return getItemName(result) + "_from_" + conversionName + "_" + getItemName(ingredient);
    }

    private static String getHeatingRecipeName(ItemLike result, ItemLike ingredient) {
        return getItemName(result) + "heating" + getItemName(ingredient);
    }

    private static String getArcHeatingRecipeName(ItemLike result, ItemLike ingredient) {
        return getItemName(result) + "arc_heating" + getItemName(ingredient);
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
                    .save(this.output, Constants.key(Registries.RECIPE, "dye_" + getItemName(item1)));
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
                .save(this.output, Constants.key(Registries.RECIPE, getItemName(parachute)));
    }

    @Override
    protected void nineBlockStorageRecipes(RecipeCategory unpackedCategory, ItemLike unpacked, RecipeCategory packedCategory, ItemLike packed) {
        nineBlockStorageRecipes(unpackedCategory, unpacked, packedCategory, packed, getItemName(packed), null, getSimpleRecipeName(unpacked), null);
    }

    private static String getItemId(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).toString();
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
