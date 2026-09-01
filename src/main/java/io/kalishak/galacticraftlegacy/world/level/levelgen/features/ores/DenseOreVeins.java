package io.kalishak.galacticraftlegacy.world.level.levelgen.features.ores;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;

public class DenseOreVeins {
    public static final ResourceKey<DenseOreVeinifier.VeinType> IRON = key("iron");
    public static final ResourceKey<DenseOreVeinifier.VeinType> COPPER = key("copper");

    public static final ResourceKey<DenseOreVeinifier.VeinType> ALUMINUM = key("aluminum");
    public static final ResourceKey<DenseOreVeinifier.VeinType> TIN = key("tin");

    public static void bootstrap(BootstrapContext<DenseOreVeinifier.VeinType> cxt) {
        cxt.register(IRON, DenseOreVeinifier.VeinType.IRON);
        cxt.register(COPPER, DenseOreVeinifier.VeinType.COPPER);

        cxt.register(ALUMINUM, new DenseOreVeinifier.VeinType(
                GalacticraftBlocks.ALUMINUM_ORE.get().defaultBlockState(),
                GalacticraftBlocks.RAW_ALUMINUM_BLOCK.get().defaultBlockState(),
                Blocks.DIORITE.defaultBlockState(),
                -20,
                35
        ));
        cxt.register(TIN, new DenseOreVeinifier.VeinType(
                GalacticraftBlocks.TIN_ORE.get().defaultBlockState(),
                GalacticraftBlocks.RAW_TIN_BLOCK.get().defaultBlockState(),
                Blocks.ANDESITE.defaultBlockState(),
                -9,
                20
        ));
    }

    private static ResourceKey<DenseOreVeinifier.VeinType> key(String name) {
        return Constants.key(GalacticraftRegistries.Keys.VEIN_TYPE, name);
    }
}
