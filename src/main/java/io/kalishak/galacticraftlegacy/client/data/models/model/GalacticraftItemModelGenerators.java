package io.kalishak.galacticraftlegacy.client.data.models.model;

import io.kalishak.galacticraftlegacy.aunified.data.model.ExtendedItemModelGenerator;
import io.kalishak.galacticraftlegacy.client.item.ColorByFluid;
import io.kalishak.galacticraftlegacy.client.renderer.item.properties.numeric.DungeonLocatorAngle;
import io.kalishak.galacticraftlegacy.client.renderer.item.properties.range.FluidAmountProperty;
import io.kalishak.galacticraftlegacy.client.renderer.item.properties.select.SchematicTierProperty;
import io.kalishak.galacticraftlegacy.client.renderer.special.KeySpecialRenderer;
import io.kalishak.galacticraftlegacy.client.renderer.special.SpecialVehicleRenderer;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.item.VehicleItem;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.equipment.trim.GalacticraftMaterialAssetGroup;
import io.kalishak.galacticraftlegacy.world.item.equipment.trim.GalacticraftTrimMaterials;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

import java.util.List;

public class GalacticraftItemModelGenerators extends ExtendedItemModelGenerator {
    public static final List<TrimMaterialData> MODDED_TRIMS = List.of(
            new TrimMaterialData(GalacticraftMaterialAssetGroup.STEEL, GalacticraftTrimMaterials.STEEL),
            new TrimMaterialData(GalacticraftMaterialAssetGroup.CHEESE, GalacticraftTrimMaterials.CHEESE),
            new TrimMaterialData(GalacticraftMaterialAssetGroup.DESH, GalacticraftTrimMaterials.DESH),
            new TrimMaterialData(GalacticraftMaterialAssetGroup.TITANIUM, GalacticraftTrimMaterials.TITANIUM),
            new TrimMaterialData(GalacticraftMaterialAssetGroup.LEAD, GalacticraftTrimMaterials.LEAD)
    );
    
    public GalacticraftItemModelGenerators(ItemModelGenerators gen) {
        super(gen.itemModelOutput, gen.modelOutput);
        addTrims(MODDED_TRIMS);
    }

    public void createDungeonLocator(Item item) {
        this.itemModelOutput
                .accept(
                        item,
                        ItemModelUtils.rangeSelect(new DungeonLocatorAngle(), 32.0F, createCompassModels(item))
                );
    }

    public void createFluidTank(Item item) {
        ItemModel.Unbaked emptyTank = ItemModelUtils.plainModel(createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked partialModel_1 = ItemModelUtils.tintedModel(createFlatItemModel(item, "_partial_1", ModelTemplates.FLAT_ITEM), new ColorByFluid());
        ItemModel.Unbaked partialModel_2 = ItemModelUtils.tintedModel(createFlatItemModel(item, "_partial_2", ModelTemplates.FLAT_ITEM), new ColorByFluid());
        ItemModel.Unbaked partialModel_3 = ItemModelUtils.tintedModel(createFlatItemModel(item, "_partial_3", ModelTemplates.FLAT_ITEM), new ColorByFluid());
        ItemModel.Unbaked partialModel_4 = ItemModelUtils.tintedModel(createFlatItemModel(item, "_partial_4", ModelTemplates.FLAT_ITEM), new ColorByFluid());
        ItemModel.Unbaked partialModel_5 = ItemModelUtils.tintedModel(createFlatItemModel(item, "_partial_5", ModelTemplates.FLAT_ITEM), new ColorByFluid());
        ItemModel.Unbaked partialModel_6 = ItemModelUtils.tintedModel(createFlatItemModel(item, "_partial_6", ModelTemplates.FLAT_ITEM), new ColorByFluid());

        this.itemModelOutput.accept(
                item,
                ItemModelUtils.composite(
                        emptyTank,
                        ItemModelUtils.rangeSelect(
                                new FluidAmountProperty(),
                                emptyTank,
                                ItemModelUtils.override(partialModel_1, 0.13F),
                                ItemModelUtils.override(partialModel_2, 0.28F),
                                ItemModelUtils.override(partialModel_3, 0.42F),
                                ItemModelUtils.override(partialModel_4, 0.57F),
                                ItemModelUtils.override(partialModel_5, 0.71F),
                                ItemModelUtils.override(partialModel_6, 0.85F)
                        )
                )
        );
    }

    public void createKey(Item key, FeatureTier featureTier) {
        Identifier model = createFlatItemModel(key, ExtendedModelTemplateBuilder.builder().parent(Constants.id("item/key_template")).build());
        ItemModel.Unbaked unbakedModel = ItemModelUtils.specialModel(model, new KeySpecialRenderer.Unbaked(featureTier));
        this.itemModelOutput.accept(key, unbakedModel);
    }

    private ItemModel.Unbaked getUnbakedSchematic(Item item, FeatureTier tier) {
        return ItemModelUtils.plainModel(createFlatItemModel(item, tier.getSuffix(), ModelTemplates.FLAT_ITEM));
    }

    public void createSchematic(Item item) {
        Identifier itemModel = ModelLocationUtils.getModelLocation(item);
        ItemModel.Unbaked fallback = getUnbakedSchematic(item, FeatureTier.TIER_1);

        ModelTemplates.FLAT_ITEM.create(itemModel, TextureMapping.layer0(item), this.modelOutput);

        this.itemModelOutput.accept(
                item,
                ItemModelUtils.conditional(
                        ItemModelUtils.hasComponent(GalacticraftDataComponents.SCHEMATIC.get()),
                        ItemModelUtils.select(
                                new SchematicTierProperty(),
                                ItemModelUtils.when(FeatureTier.TIER_1, fallback),
                                ItemModelUtils.when(FeatureTier.TIER_2, getUnbakedSchematic(item, FeatureTier.TIER_2)),
                                ItemModelUtils.when(FeatureTier.TIER_3, getUnbakedSchematic(item, FeatureTier.TIER_3))
                        ),
                        fallback
                )
        );
    }

    public void createVehicleLike(VehicleItem rocketItem, SpecialVehicleRenderer.Unbaked<?> unbaked) {
        Identifier model = GalacticraftModelTemplates.ROTATING_ITEM.create(rocketItem, TextureMapping.particle(GalacticraftBlocks.ASTEROID_ROCK.get()), this.modelOutput);
        ItemModel.Unbaked unbakedModel = ItemModelUtils.specialModel(model, unbaked);
        this.itemModelOutput.accept(rocketItem, unbakedModel);
    }
}
