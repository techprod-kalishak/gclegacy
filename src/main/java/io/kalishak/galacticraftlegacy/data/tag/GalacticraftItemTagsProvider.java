package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class GalacticraftItemTagsProvider extends ItemTagsProvider {
    public GalacticraftItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ItemTags.AXES)
                .add(GalacticraftItems.DESH_AXE.get())
                .add(GalacticraftItems.STEEL_AXE.get())
                .add(GalacticraftItems.TITANIUM_AXE.get());
        tag(ItemTags.CHEST_ARMOR)
                .add(GalacticraftItems.DESH_CHESTPLATE.get())
                .add(GalacticraftItems.STEEL_CHESTPLATE.get())
                .add(GalacticraftItems.TITANIUM_CHESTPLATE.get());
        tag(ItemTags.FOOT_ARMOR)
                .add(GalacticraftItems.DESH_BOOTS.get())
                .add(GalacticraftItems.STEEL_BOOTS.get())
                .add(GalacticraftItems.TITANIUM_BOOTS.get());
        tag(ItemTags.HEAD_ARMOR)
                .add(GalacticraftItems.DESH_HELMET.get())
                .add(GalacticraftItems.STEEL_HELMET.get())
                .add(GalacticraftItems.TITANIUM_HELMET.get());
        tag(ItemTags.HOES)
                .add(GalacticraftItems.DESH_HOE.get())
                .add(GalacticraftItems.STEEL_HOE.get())
                .add(GalacticraftItems.TITANIUM_HOE.get());
        tag(ItemTags.LEG_ARMOR)
                .add(GalacticraftItems.DESH_LEGGINGS.get())
                .add(GalacticraftItems.STEEL_LEGGINGS.get())
                .add(GalacticraftItems.TITANIUM_LEGGINGS.get());
        tag(ItemTags.PICKAXES)
                .add(GalacticraftItems.DESH_PICKAXE.get())
                .add(GalacticraftItems.STEEL_PICKAXE.get())
                .add(GalacticraftItems.TITANIUM_PICKAXE.get());
        tag(ItemTags.SHOVELS)
                .add(GalacticraftItems.DESH_SHOVEL.get())
                .add(GalacticraftItems.STEEL_SHOVEL.get())
                .add(GalacticraftItems.TITANIUM_SHOVEL.get());
        tag(ItemTags.SPEARS)
                .add(GalacticraftItems.DESH_SPEAR.get())
                .add(GalacticraftItems.STEEL_SPEAR.get())
                .add(GalacticraftItems.TITANIUM_SPEAR.get());
        tag(ItemTags.SWORDS)
                .add(GalacticraftItems.DESH_SWORD.get())
                .add(GalacticraftItems.STEEL_SWORD.get())
                .add(GalacticraftItems.TITANIUM_SWORD.get());

        tag(Tags.Items.BUCKETS)
                .add(GalacticraftItems.OIL_BUCKET.get())
                .add(GalacticraftItems.FUEL_BUCKET.get());
        tag(Tags.Items.INGOTS)
                .addTag(GalacticraftTags.Items.INGOTS_DESH)
                .addTag(GalacticraftTags.Items.INGOTS_LEAD)
                .addTag(GalacticraftTags.Items.INGOTS_STEEL)
                .addTag(GalacticraftTags.Items.INGOTS_TITANIUM);
        tag(Tags.Items.NUGGETS)
                .addTag(GalacticraftTags.Items.NUGGETS_DESH)
                .addTag(GalacticraftTags.Items.NUGGETS_LEAD)
                .addTag(GalacticraftTags.Items.NUGGETS_STEEL)
                .addTag(GalacticraftTags.Items.NUGGETS_TITANIUM);
        tag(Tags.Items.RAW_MATERIALS)
                .addOptionalTag(GalacticraftTags.Items.RAW_MATERIALS_CHEESE)
                .addTag(GalacticraftTags.Items.RAW_MATERIALS_DESH)
                .addTag(GalacticraftTags.Items.RAW_MATERIALS_LEAD)
                .addTag(GalacticraftTags.Items.RAW_MATERIALS_SILICON)
                .addTag(GalacticraftTags.Items.RAW_MATERIALS_STEEL)
                .addTag(GalacticraftTags.Items.RAW_MATERIALS_TITANIUM);
        tag(Tags.Items.TOOLS)
                .addTag(GalacticraftTags.Items.WRENCH);

        tag(GalacticraftTags.Items.INGOTS_DESH)
                .add(GalacticraftItems.DESH_INGOT.get());
        tag(GalacticraftTags.Items.INGOTS_LEAD)
                .add(GalacticraftItems.LEAD_INGOT.get());
        tag(GalacticraftTags.Items.INGOTS_STEEL)
                .add(GalacticraftItems.STEEL_INGOT.get());
        tag(GalacticraftTags.Items.INGOTS_TITANIUM)
                .add(GalacticraftItems.TITANIUM_INGOT.get());
        tag(GalacticraftTags.Items.NUGGETS_DESH)
                .add(GalacticraftItems.DESH_NUGGET.get());
        tag(GalacticraftTags.Items.NUGGETS_LEAD)
                .add(GalacticraftItems.LEAD_NUGGET.get());
        tag(GalacticraftTags.Items.NUGGETS_STEEL)
                .add(GalacticraftItems.STEEL_NUGGET.get());
        tag(GalacticraftTags.Items.NUGGETS_TITANIUM)
                .add(GalacticraftItems.TITANIUM_NUGGET.get());
        tag(GalacticraftTags.Items.PARACHUTE)
                .add(GalacticraftItems.BLACK_PARACHUTE.get())
                .add(GalacticraftItems.BLUE_PARACHUTE.get())
                .add(GalacticraftItems.BROWN_PARACHUTE.get())
                .add(GalacticraftItems.CYAN_PARACHUTE.get())
                .add(GalacticraftItems.GRAY_PARACHUTE.get())
                .add(GalacticraftItems.LIGHT_BLUE_PARACHUTE.get())
                .add(GalacticraftItems.LIGHT_GRAY_PARACHUTE.get())
                .add(GalacticraftItems.LIME_PARACHUTE.get())
                .add(GalacticraftItems.MAGENTA_PARACHUTE.get())
                .add(GalacticraftItems.ORANGE_PARACHUTE.get())
                .add(GalacticraftItems.PINK_PARACHUTE.get())
                .add(GalacticraftItems.PURPLE_PARACHUTE.get())
                .add(GalacticraftItems.RED_PARACHUTE.get())
                .add(GalacticraftItems.WHITE_PARACHUTE.get())
                .add(GalacticraftItems.YELLOW_PARACHUTE.get());
        tag(GalacticraftTags.Items.RAW_MATERIALS_CHEESE)
                .add(GalacticraftItems.CHEESE_CHUNK.get());
        tag(GalacticraftTags.Items.RAW_MATERIALS_DESH)
                .add(GalacticraftItems.RAW_DESH.get());
        tag(GalacticraftTags.Items.RAW_MATERIALS_LEAD)
                .add(GalacticraftItems.RAW_LEAD.get());
        tag(GalacticraftTags.Items.RAW_MATERIALS_SILICON)
                .add(GalacticraftItems.RAW_SILICON.get());
        tag(GalacticraftTags.Items.RAW_MATERIALS_STEEL)
                .add(GalacticraftItems.RAW_STEEL.get());
        tag(GalacticraftTags.Items.RAW_MATERIALS_TITANIUM)
                .add(GalacticraftItems.RAW_TITANIUM.get());
        tag(GalacticraftTags.Items.REPAIRS_DESH_ARMOR)
                .addTag(GalacticraftTags.Items.INGOTS_DESH);
        tag(GalacticraftTags.Items.REPAIRS_STEEL_ARMOR)
                .addTag(GalacticraftTags.Items.INGOTS_STEEL);
        tag(GalacticraftTags.Items.REPAIRS_TITANIUM_ARMOR)
                .addTag(GalacticraftTags.Items.INGOTS_TITANIUM);
        tag(GalacticraftTags.Items.REPAIRS_DESH_TOOL)
                .addTag(GalacticraftTags.Items.INGOTS_DESH);
        tag(GalacticraftTags.Items.REPAIRS_STEEL_TOOL)
                .addTag(GalacticraftTags.Items.INGOTS_STEEL);
        tag(GalacticraftTags.Items.REPAIRS_TITANIUM_TOOL)
                .addTag(GalacticraftTags.Items.INGOTS_TITANIUM);
        tag(GalacticraftTags.Items.WRENCH)
                .add(GalacticraftItems.WRENCH.get());
    }
}
