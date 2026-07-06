/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client;

import com.google.common.reflect.TypeToken;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.client.gui.SensorGlassesOverlay;
import io.kalishak.galacticraftlegacy.client.gui.TanksLayer;
import io.kalishak.galacticraftlegacy.client.gui.screens.inventory.*;
import io.kalishak.galacticraftlegacy.client.gui.screens.recipebook.GalacticraftClientRecipeBookCategories;
import io.kalishak.galacticraftlegacy.client.item.ColorByFluid;
import io.kalishak.galacticraftlegacy.client.model.FlagModel;
import io.kalishak.galacticraftlegacy.client.model.gear.OxygenGearModel;
import io.kalishak.galacticraftlegacy.client.model.gear.ParachuteModel;
import io.kalishak.galacticraftlegacy.client.renderer.blockentity.DungeonBlockRenderer;
import io.kalishak.galacticraftlegacy.client.renderer.blockentity.ParachestBlockRenderer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.*;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear.OxygenMaskLayer;
import io.kalishak.galacticraftlegacy.client.model.gear.OxygenTankModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.GalacticraftSheets;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear.GearEquipmentLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenderState;
import io.kalishak.galacticraftlegacy.client.renderer.environment.*;
import io.kalishak.galacticraftlegacy.client.renderer.item.KeyModel;
import io.kalishak.galacticraftlegacy.client.renderer.item.properties.numeric.DungeonLocatorAngle;
import io.kalishak.galacticraftlegacy.client.renderer.item.properties.range.FluidAmountProperty;
import io.kalishak.galacticraftlegacy.client.renderer.item.properties.select.SchematicTierProperty;
import io.kalishak.galacticraftlegacy.client.data.GalacticraftSpritesProvider;
import io.kalishak.galacticraftlegacy.client.renderer.special.KeySpecialRenderer;
import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityType;
import io.kalishak.galacticraftlegacy.world.inventory.GalacticraftMenuType;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluidType;
import io.kalishak.galacticraftlegacy.world.level.material.fluid.GalacticraftFluids;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
@Mod(value = Galacticraft.MODID, dist = Dist.CLIENT)
public class GalacticraftClient {
    public static final Logger LOGGER = LoggerFactory.getLogger(GalacticraftClient.class);

    public GalacticraftClient(IEventBus bus, ModContainer container) {
        bus.addListener(this::registerAtlases);
        bus.addListener(this::registerClientExtensions);
        bus.addListener(this::registerEntityRenderers);
        bus.addListener(this::registerEnvironmentEffects);
        bus.addListener(this::registerFluidModels);
        bus.addListener(this::registerOverlays);
        bus.addListener(this::registerLayerDefinitions);
        bus.addListener(this::registerScreens);
        bus.addListener(this::registerTintSources);
        bus.addListener(this::registerBlockTintSources);
        bus.addListener(this::registerRangedItemModelProperty);
        bus.addListener(this::addRenderStates);
        bus.addListener(this::registerSelectItemModelProperty);
        bus.addListener(this::registerSpecialRenderers);
        bus.addListener(this::registerDimensionTransitionScreen);

        bus.addListener(EntityRenderersEvent.AddLayers.class, GearEquipmentLayer::registerAdditionalLayers);
        bus.addListener(GalacticraftClientRecipeBookCategories::registerBookCategories);
        bus.addListener(GalacticraftKeys::registerKeyMappings);
        NeoForge.EVENT_BUS.addListener(SpaceSkyRenderer::extractLevelRenderState);
        NeoForge.EVENT_BUS.addListener(SpaceSkyRenderer::renderSky);

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        NeoForge.EVENT_BUS.register(new NeoForgeClientEventHandler());
    }

    private void registerAtlases(RegisterTextureAtlasesEvent event) {
        event.register(new AtlasManager.AtlasConfig(
                GalacticraftSheets.SCHEMATIC_SHEET,
                GalacticraftSpritesProvider.SCHEMATICS,
                false,
                Set.of()
        ));
        event.register(
                new AtlasManager.AtlasConfig(
                        GalacticraftSheets.PARACHUTE_SHEET,
                        GalacticraftSpritesProvider.PARACHUTES,
                        false,
                        Set.of()
                )
        );
        event.register(
                new AtlasManager.AtlasConfig(
                        GalacticraftSheets.CELESTIAL_BODY_SHEET,
                        GalacticraftSpritesProvider.CELESTIAL_BODIES,
                        false,
                        Set.of()
                )
        );
    }

    private void registerEnvironmentEffects(RegisterCustomEnvironmentEffectRendererEvent event) {
        MoonSkyRenderer.create(event::registerSkyboxRenderer);
        OrbitalSkyRenderer.create(event::registerSkyboxRenderer);
        SpaceCloudsRenderer.create(event::registerCloudRenderer);
        SpaceWeatherRenderer.create(event::registerWeatherEffectRenderer);
    }

    private void registerFluidModels(RegisterFluidModelsEvent event) {
        final Identifier still = Identifier.withDefaultNamespace("block/water_still");
        final Identifier flow = Identifier.withDefaultNamespace("block/water_flow");

        event.register(new FluidModel.Unbaked(
                new Material(still),
                new Material(flow),
                null,
                state -> 0xFFAFEEEE
        ), GalacticraftFluids.OXYGEN, GalacticraftFluids.OXYGEN_FLOWING);
        event.register(new FluidModel.Unbaked(
                new Material(still),
                new Material(flow),
                new Material(Identifier.withDefaultNamespace("block/water_overlay")),
                state -> 0xFF281E15
                ), GalacticraftFluids.OIL, GalacticraftFluids.OIL_FLOWING
        );
        event.register(new FluidModel.Unbaked(
                        new Material(Constants.id("block/fuel_still")),
                        new Material(Constants.id("block/fuel_flow")),
                        null,
                        state -> 0xFFECF542
                ), GalacticraftFluids.FUEL, GalacticraftFluids.FUEL_FLOWING
        );
    }

    private void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public Identifier getRenderOverlayTexture(Minecraft mc) {
                return Identifier.withDefaultNamespace("textures/misc/underwater.png");
            }
        }, GalacticraftFluidType.OIL);
    }

    private void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(GalacticraftEntityType.FLAG.get(), FlagRenderer::new);
        event.registerEntityRenderer(GalacticraftEntityType.FALLEN_METEOR.get(), FallingBlockRenderer::new);
        event.registerEntityRenderer(GalacticraftEntityType.SCHEMATIC.get(), SchematicRenderer::new);
        event.registerEntityRenderer(GalacticraftEntityType.THROWN_METEOR_CHUNK.get(), ThrownMeteorRenderer::new);
        event.registerEntityRenderer(GalacticraftEntityType.FALLING_PARACHEST.get(), FallingParachestRenderer::new);
        event.registerEntityRenderer(GalacticraftEntityType.EVOLVED_SKELETON.get(), EvolvedSkeletonRenderer::new);
        event.registerEntityRenderer(GalacticraftEntityType.EVOLVED_ZOMBIE.get(), EvolvedZombieRenderer::new);
        event.registerEntityRenderer(GalacticraftEntityType.NO_GRAVITY_MOVING_BLOCK.get(), NoGravityMovingBlockRenderer::new);

        event.registerBlockEntityRenderer(GalacticraftBlockEntityType.PARACHEST.get(), ParachestBlockRenderer::new);
        event.registerBlockEntityRenderer(GalacticraftBlockEntityType.DUNGEON_CHEST.get(), DungeonBlockRenderer::new);
    }

    private void registerOverlays(RegisterGuiLayersEvent event) {
        event.registerAboveAll(Constants.id("sensor_glasses"), new SensorGlassesOverlay());
        event.registerBelow(Constants.id("sensor_glasses"), Constants.id("tanks"), new TanksLayer());
    }

    private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GalacticraftModelLayers.KEY, KeyModel::createLayer);
        event.registerLayerDefinition(GalacticraftModelLayers.OXYGEN_MASK, OxygenMaskLayer::createOxygenMaskLayer);
        event.registerLayerDefinition(GalacticraftModelLayers.OXYGEN_GEAR, OxygenGearModel::createOxygenGearLayer);
        event.registerLayerDefinition(GalacticraftModelLayers.HEAVY_OXYGEN_TANK, OxygenTankModel::createHeavyTankLayer);
        event.registerLayerDefinition(GalacticraftModelLayers.MEDIUM_OXYGEN_TANK, OxygenTankModel::createMediumTankLayer);
        event.registerLayerDefinition(GalacticraftModelLayers.LIGHT_OXYGEN_TANK, OxygenTankModel::createLightTankLayer);
        event.registerLayerDefinition(GalacticraftModelLayers.PARACHUTE, ParachuteModel::createParachuteLayer);
        ArmorModelSet<LayerDefinition> thermalPadding = PlayerModel.createArmorMeshSet(new CubeDeformation(0.032F), new CubeDeformation(0.06F)).map(layer -> LayerDefinition.create(layer, 64, 32));
        event.registerLayerDefinition(GalacticraftModelLayers.THERMAL_PADDING.head(), thermalPadding::head);
        event.registerLayerDefinition(GalacticraftModelLayers.THERMAL_PADDING.chest(), thermalPadding::chest);
        event.registerLayerDefinition(GalacticraftModelLayers.THERMAL_PADDING.legs(), thermalPadding::legs);
        event.registerLayerDefinition(GalacticraftModelLayers.THERMAL_PADDING.feet(), thermalPadding::feet);

        event.registerLayerDefinition(GalacticraftModelLayers.FLAG, FlagModel::createLayer);
    }

    private void registerScreens(RegisterMenuScreensEvent event) {
        event.register(GalacticraftMenuType.ARC_FURNACE.get(), ElectricFurnaceScreen::new);
        event.register(GalacticraftMenuType.COAL_GENERATOR.get(), CoalGeneratorScreen::new);
        event.register(GalacticraftMenuType.CIRCUIT_FABRICATOR.get(), CircuitFabricatorScreen::new);
        event.register(GalacticraftMenuType.COMPRESSOR.get(), CompressorScreen::new);
        event.register(GalacticraftMenuType.DUNGEON_CHEST.get(), DungeonChestScreen::new);
        event.register(GalacticraftMenuType.ELECTRIC_COMPRESSOR.get(), ElectricCompressorScreen::new);
        event.register(GalacticraftMenuType.GEAR.get(), GearInventoryScreen::new);
        event.register(GalacticraftMenuType.MAGNETIC_CRAFTING.get(), MagneticCraftingTableScreen::new);
        event.register(GalacticraftMenuType.PARACHEST.get(), ParachestScreen::new);
        event.register(GalacticraftMenuType.OXYGEN_COLLECTOR.get(), OxygenCollectorScreen::new);
    }

    private void registerDimensionTransitionScreen(RegisterDimensionTransitionScreenEvent event) {
        //event.registerIncomingEffect(GalacticraftDimensions.MOON, SpaceTravelLoadingScreen::new);
    }

    private void registerTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(Constants.id("color_by_fluid"), ColorByFluid.CODEC);
    }

    private void registerBlockTintSources(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(List.of(GalacticraftBlockTintSources.meteor()), GalacticraftBlocks.FALLEN_METEOR.get());
    }

    private void registerRangedItemModelProperty(RegisterRangeSelectItemModelPropertyEvent event) {
        event.register(Constants.id("fluid_amount"), FluidAmountProperty.CODEC);
        event.register(Constants.id("dungeon_location"), DungeonLocatorAngle.MAP_CODEC);
    }

    private void registerSpecialRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(Constants.id("key_renderer"), KeySpecialRenderer.Unbaked.MAP_CODEC);
    }

    private void addRenderStates(RegisterRenderStateModifiersEvent event) {
        event.registerEntityModifier(
                new TypeToken<AvatarRenderer<?>>() {},
                GearRenderState::appendPlayerRenderStates
        );
        event.registerEntityModifier(
                CreeperRenderer.class,
                GearRenderState::appendCommonRenderStates
        );
        event.registerEntityModifier(
                ZombieRenderer.class,
                GearRenderState::appendCommonRenderStates
        );
        event.registerEntityModifier(
                SkeletonRenderer.class,
                GearRenderState::appendCommonRenderStates
        );
    }

    private void registerSelectItemModelProperty(RegisterSelectItemModelPropertyEvent event) {
        event.register(Constants.id("schematic_level"), SchematicTierProperty.TYPE);
    }
}
