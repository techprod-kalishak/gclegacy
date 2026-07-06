/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.client.model.GalacticraftObjModelKeys;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.obj.ObjModel;

public class TelemetryModuleLayer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends GearEquipmentLayer<S, M> {
    public static final Identifier TEXTURES = Constants.texture("model/telemetry_module.png");

    public TelemetryModuleLayer(RenderLayerParent<S, M> renderer, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssetManager) {
        super(renderer, null, layerRenderer, equipmentAssetManager);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, S renderState, float yRot, float xRot) {
        ItemStack stack = extractFromRenderState(renderState, GearRenderState.TELEMETRY_MODULE, GearRenderState::telemetryModule);

        if (!stack.isEmpty()) {

        }
    }

    public ObjModel getObjModel(ModelManager modelManager) {
        return modelManager.getStandaloneModel(GalacticraftObjModelKeys.TELEMETRY_MODULE_KEY);
    }
}
