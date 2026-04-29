/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear;

import io.kalishak.galacticraftlegacy.EnumExtensions;
import io.kalishak.galacticraftlegacy.client.model.gear.*;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenderState;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.item.component.GearEquippable;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;

public abstract class GearEquipmentLayer<S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {
    protected final EquipmentLayerRenderer layerRenderer;
    protected final EquipmentAssetManager equipmentAssets;
    protected final Model<S> model;

    public GearEquipmentLayer(RenderLayerParent<S, M> renderer, GearEquipmentModel<S> model, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssetManager) {
        super(renderer);
        this.model = model;
        this.layerRenderer = layerRenderer;
        this.equipmentAssets = equipmentAssetManager;
    }

    public GearEquipmentLayer(RenderLayerParent<S, M> renderer, Function<ModelPart, ? extends Model<S>> modelGetter, ModelLayerLocation modelLayerLocation, EntityModelSet entityModels, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssetManager) {
        super(renderer);
        this.model = modelGetter.apply(entityModels.bakeLayer(modelLayerLocation));
        this.layerRenderer = layerRenderer;
        this.equipmentAssets = equipmentAssetManager;
    }

    @SubscribeEvent
    public static void registerAdditionalLayers(EntityRenderersEvent.AddLayers event) {
        EntityModelSet modelSet = event.getEntityModels();
        EquipmentLayerRenderer equipmentRenderer = event.getContext().getEquipmentRenderer();
        EquipmentAssetManager equipmentAssets = event.getContext().getEquipmentAssets();
        SpriteGetter spriteGetter = event.getContext().getSprites();

        AvatarRenderer<?> slimAvatarRenderer = event.getPlayerRenderer(PlayerModelType.SLIM);
        registerLayerForPlayerRenderer(slimAvatarRenderer, modelSet, equipmentRenderer, equipmentAssets, spriteGetter);

        AvatarRenderer<?> wideAvatarRenderer = event.getPlayerRenderer(PlayerModelType.WIDE);
        registerLayerForPlayerRenderer(wideAvatarRenderer, modelSet, equipmentRenderer, equipmentAssets, spriteGetter);

        CreeperRenderer creeperRenderer = event.getRenderer(EntityType.CREEPER);
        registerLayerForRenderer(creeperRenderer, modelSet, equipmentRenderer, equipmentAssets);

        SkeletonRenderer skeletonRenderer = event.getRenderer(EntityType.SKELETON);
        registerLayerForRenderer(skeletonRenderer, modelSet, equipmentRenderer, equipmentAssets);

        ZombieRenderer zombieRenderer = event.getRenderer(EntityType.ZOMBIE);
        registerLayerForRenderer(zombieRenderer, modelSet, equipmentRenderer, equipmentAssets);
    }

    private static <S extends HumanoidRenderState, M extends HumanoidModel<S>> void registerLayerForPlayerRenderer(@Nullable AvatarRenderer<?> renderer, EntityModelSet modelSet, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssets, SpriteGetter spriteGetter) {
        if (renderer != null) {
            renderer.addLayer(new OxygenMaskLayer<>(renderer, modelSet, layerRenderer, equipmentAssets));
            renderer.addLayer(new OxygenGearLayer<>(renderer, modelSet, layerRenderer, equipmentAssets));
            renderer.addLayer(new OxygenTankLayer<>(renderer, modelSet, layerRenderer, equipmentAssets));
            renderer.addLayer(new ThermalPaddingLayer(renderer, GalacticraftModelLayers.THERMAL_PADDING, modelSet, layerRenderer, equipmentAssets));
            renderer.addLayer(new TelemetryModuleLayer<>(renderer, layerRenderer, equipmentAssets));
            renderer.addLayer(new ParachuteLayer<>(renderer, spriteGetter, modelSet, layerRenderer, equipmentAssets));
        }
    }

    private static <S extends LivingEntityRenderState, M extends EntityModel<S>> void registerLayerForRenderer(@Nullable LivingEntityRenderer<?, S, M> renderer, EntityModelSet modelSet, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssets) {
        if (renderer == null) {
            return;
        }

        renderer.addLayer(new OxygenMaskLayer<>(renderer, modelSet, layerRenderer, equipmentAssets));
        renderer.addLayer(new OxygenGearLayer<>(renderer, modelSet, layerRenderer, equipmentAssets));
        renderer.addLayer(new OxygenTankLayer<>(renderer, modelSet, layerRenderer, equipmentAssets));

//        if (renderer instanceof WolfRenderer wolfRenderer) {
//            renderer.addLayer((RenderLayer<S, M>) new ThermalWolfJacketLayer(wolfRenderer, modelSet, layerRenderer, equipmentAssets));
//        }
    }

    protected ItemStack extractFromRenderState(S renderState, ContextKey<ItemStack> contextKey, Function<GearRenderState, ItemStack> callback) {
        ItemStack stack = renderState.getRenderDataOrDefault(contextKey, ItemStack.EMPTY);

        if (stack.isEmpty() && renderState instanceof GearRenderState gearRenderState) {
            stack = callback.apply(gearRenderState);
        }

        return stack;
    }

    public EquipmentAssetManager getEquipmentAssetManager() {
        return this.equipmentAssets;
    }

    protected EquipmentClientInfo.LayerType getLayerForSlot(GearEquipmentSlot gearEquipmentSlot) {
        return switch (gearEquipmentSlot) {
            case THERMAL_CAP, THERMAL_SHIRT, THERMAL_LEGGINGS, THERMAL_SOCKS, SHIELD -> EnumExtensions.LAYER_TYPE_THERMAL_PADDING.getValue();
            case TANK, ADDITIONAL_TANK -> EnumExtensions.LAYER_TYPE_TANK.getValue();
            case PARACHUTE -> EnumExtensions.LAYER_TYPE_PARACHUTE.getValue();
            default -> null;
        };
    }

    protected Identifier getGearTextures(@NonNull ResourceKey<EquipmentAsset> gearEquipmentAsset, EquipmentClientInfo.LayerType layerType) {
        return this.equipmentAssets.get(gearEquipmentAsset).getLayers(layerType).getFirst().getTextureLocation(layerType);
    }

    protected RenderType getRenderType(Model<S> model, @NonNull ResourceKey<EquipmentAsset> tankAsset, GearEquipmentSlot gearEquipmentSlot) {
        EquipmentClientInfo.LayerType layerType = getLayerForSlot(gearEquipmentSlot);

        return model.renderType(getGearTextures(tankAsset, layerType));
    }

    protected RenderType getRenderType(Model<S> model, GearEquippable gearEquippable) {
        if (gearEquippable.assetId().isEmpty()) {
            return RenderTypes.glint();
        }

        return getRenderType(model, gearEquippable.assetId().get(), gearEquippable.gearSlot());
    }

    protected Identifier getEquipment(ResourceKey<EquipmentAsset> equipmentAsset, EquipmentClientInfo.LayerType layerType) {
        return this.equipmentAssets.get(equipmentAsset).getLayers(layerType).getFirst().textureId();
    }

    protected @Nullable <C> C getDataFromContext(S renderState, ContextKey<C> cxtKey) {
        return renderState.getRenderData(cxtKey);
    }

    protected <C> C getDataFromContext(S renderState, ContextKey<C> cxtKey, C defaultValue) {
        return renderState.getRenderDataOrDefault(cxtKey, defaultValue);
    }
}
