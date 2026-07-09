package io.kalishak.galacticraftlegacy.world.item.crafting;

import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipePropertySet;

public class GalacticraftRecipePropertySet {
    public static final ResourceKey<RecipePropertySet> ELECTRIC_FURNACE_INPUT = Constants.key(RecipePropertySet.TYPE_KEY, "electric_arc_furnace_input");
    public static final ResourceKey<RecipePropertySet> ELECTRIC_ARC_FURNACE_INPUT = Constants.key(RecipePropertySet.TYPE_KEY, "electric_furnace_input");
    public static final ResourceKey<RecipePropertySet> COMPRESSOR_INPUT = Constants.key(RecipePropertySet.TYPE_KEY, "compressor_input");
    public static final ResourceKey<RecipePropertySet> CIRCUIT_FABRICATOR_INPUT = Constants.key(RecipePropertySet.TYPE_KEY, "circuit_fabricator_input");
}
