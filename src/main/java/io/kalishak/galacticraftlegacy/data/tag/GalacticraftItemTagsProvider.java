package io.kalishak.galacticraftlegacy.data.tag;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class GalacticraftItemTagsProvider extends ItemTagsProvider {
    public GalacticraftItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Galacticraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Tags.Items.TOOLS)
                .addTag(GalacticraftTags.Items.WRENCH);

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
        tag(GalacticraftTags.Items.WRENCH)
                .add(GalacticraftItems.WRENCH.get());
    }
}
