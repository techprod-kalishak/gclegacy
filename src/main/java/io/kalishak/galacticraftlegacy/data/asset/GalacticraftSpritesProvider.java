package io.kalishak.galacticraftlegacy.data.asset;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;

import java.util.concurrent.CompletableFuture;

public class GalacticraftSpritesProvider extends SpriteSourceProvider {
    public GalacticraftSpritesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Galacticraft.MODID);
    }

    @Override
    protected void gather() {
        atlas(AtlasIds.GUI).addSource(new DirectoryLister(Galacticraft.MODID, ""));
    }
}
