/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.client.model.gear.ParachuteModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.GalacticraftSheets;
import io.kalishak.galacticraftlegacy.client.renderer.ParachuteRenderable;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenderState;
import io.kalishak.galacticraftlegacy.world.item.GearEquipmentAssets;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.equipment.EquipmentAsset;

public class ParachuteLayer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends GearEquipmentLayer<S, M> implements ParachuteRenderable<S> {
    private final SpriteGetter materials;

    public ParachuteLayer(RenderLayerParent<S, M> renderer, SpriteGetter spriteGetter, EntityModelSet entityModelSet, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssetManager) {
        super(renderer, ParachuteModel::new, GalacticraftModelLayers.PARACHUTE, entityModelSet, layerRenderer, equipmentAssetManager);
        this.materials = spriteGetter;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, S renderState, float yRot, float xRot) {
        if (getDataFromContext(renderState, GearRenderState.IS_PARACHUTE_VISIBLE, false)) {
            renderParachute(poseStack, nodeCollector, packedLight, renderState);
        }
    }

    @Override
    public Model<S> getParachuteModel() {
        return this.model;
    }

    @Override
    public SpriteId getParachuteMaterial(S renderState) {
        ResourceKey<EquipmentAsset> equipmentAsset = getDataFromContext(renderState, GearRenderState.PARACHUTE, GearEquipmentAssets.PARACHUTES.get(DyeColor.RED));
        return new SpriteId(GalacticraftSheets.PARACHUTE_SHEET, equipmentAsset.identifier());
    }

    @Override
    public SpriteGetter spriteGetter() {
        return this.materials;
    }
}
