package io.kalishak.galacticraftlegacy.data.asset;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.data.models.model.GalacticraftTexturedModel;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class GalacticraftModelProvider extends ModelProvider {
    public GalacticraftModelProvider(PackOutput output) {
        super(output, Galacticraft.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        coalGenerator(blockModels, GalacticraftBlocks.COAL_GENERATOR.get());

        itemModels.generateFlatItem(GalacticraftItems.BATTERY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.INFINITE_BATTERY.get(), GalacticraftItems.BATTERY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.THERMAL_CLOTH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.THERMAL_CAP.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.THERMAL_SHIRT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.THERMAL_LEGGINGS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.THERMAL_SOCKS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.LIGHT_TANK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.MEDIUM_TANK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.DENSE_TANK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.OXYGEN_MASK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.OXYGEN_GEAR.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.WRENCH.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.BLACK_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.BLUE_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.BROWN_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.CYAN_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.GRAY_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.GREEN_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.LIGHT_BLUE_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.LIGHT_GRAY_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.LIME_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.MAGENTA_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.ORANGE_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.PINK_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.PURPLE_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.RED_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.WHITE_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GalacticraftItems.YELLOW_PARACHUTE.get(), ModelTemplates.FLAT_ITEM);
    }

    private void coalGenerator(BlockModelGenerators gen, Block block) {
        MultiVariant regularVariant = BlockModelGenerators.plainVariant(GalacticraftTexturedModel.COAL_GENERATOR.create(block, gen.modelOutput));
        Identifier litTexture = TextureMapping.getBlockTexture(block, "_front_on");
        MultiVariant litVariant = BlockModelGenerators.plainVariant(GalacticraftTexturedModel.COAL_GENERATOR.get(block).updateTextures(mapping -> mapping.put(TextureSlot.NORTH, litTexture)).createWithSuffix(block, "_on", gen.modelOutput));
        gen.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, regularVariant))
                        .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
        );
    }
}
