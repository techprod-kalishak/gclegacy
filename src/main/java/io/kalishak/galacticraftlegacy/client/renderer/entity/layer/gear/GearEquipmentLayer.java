/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear;

import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenderState;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenders;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.context.ContextKey;
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
    protected final float yOffset;
    protected final float zOffset;

    protected GearEquipmentLayer(RenderLayerParent<S, M> renderer, Model<S> model, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssetManager, float yOffset, float zOffset) {
        super(renderer);
        this.model = model;
        this.layerRenderer = layerRenderer;
        this.equipmentAssets = equipmentAssetManager;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }

    protected GearEquipmentLayer(RenderLayerParent<S, M> renderer, Function<ModelPart, ? extends Model<S>> modelGetter, ModelLayerLocation modelLayerLocation, EntityModelSet entityModels, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssetManager, float yOffset, float zOffset) {
        super(renderer);
        this.model = modelGetter.apply(entityModels.bakeLayer(modelLayerLocation));
        this.layerRenderer = layerRenderer;
        this.equipmentAssets = equipmentAssetManager;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
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
    }

    private static void registerLayerForPlayerRenderer(@Nullable AvatarRenderer<?> renderer, EntityModelSet modelSet, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssets, SpriteGetter spriteGetter) {
        if (renderer != null) {
            renderer.addLayer(new OxygenMaskLayer<>(renderer, modelSet, layerRenderer, equipmentAssets));
            renderer.addLayer(new OxygenGearLayer<>(renderer, modelSet, layerRenderer, equipmentAssets));
            renderer.addLayer(new OxygenTankLayer<>(renderer, modelSet, layerRenderer, equipmentAssets));
            renderer.addLayer(new ThermalPaddingLayer(renderer, GalacticraftModelLayers.THERMAL_PADDING, modelSet, layerRenderer, equipmentAssets));
            renderer.addLayer(new FrequencyModuleLayer<>(renderer, layerRenderer, equipmentAssets));
            renderer.addLayer(new ParachuteLayer<>(renderer, spriteGetter, modelSet, layerRenderer, equipmentAssets));
            renderer.addLayer(new ShieldControllerLayer(renderer, modelSet, layerRenderer, equipmentAssets));
        }
    }

    protected @NonNull ItemStack extractFromRenderState(S renderState, ContextKey<ItemStack> contextKey, Function<GearRenders, ItemStack> callback) {
        ItemStack stack = renderState.getRenderDataOrDefault(contextKey, ItemStack.EMPTY);

        if (stack.isEmpty() && renderState instanceof GearRenderState gearRenderState) {
            stack = callback.apply(gearRenderState.gearRenders());
        }

        return stack;
    }

    public EquipmentAssetManager getEquipmentAssetManager() {
        return this.equipmentAssets;
    }

    protected Identifier getGearTextures(@NonNull ResourceKey<EquipmentAsset> gearEquipmentAsset, EquipmentClientInfo.LayerType layerType) {
        return this.equipmentAssets.get(gearEquipmentAsset).getLayers(layerType).getFirst().getTextureLocation(layerType);
    }
}
