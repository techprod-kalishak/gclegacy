package io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.client.model.gear.ParachuteModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenderState;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;

public class ParachuteLayer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends GearEquipmentLayer<S, M> {
    public ParachuteLayer(RenderLayerParent<S, M> renderer, EntityModelSet entityModelSet, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssetManager) {
        super(renderer, ParachuteModel::new, GalacticraftModelLayers.PARACHUTE, entityModelSet, layerRenderer, equipmentAssetManager);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, S renderState, float yRot, float xRot) {
        ResourceKey<EquipmentAsset> equipmentAsset = getDataFromContext(renderState, GearRenderState.PARACHUTE);

        if (equipmentAsset != null && getDataFromContext(renderState, GearRenderState.IS_PARACHUTE_VISIBLE, false)) {
            poseStack.pushPose();
            nodeCollector.order(1).submitModel(
                    this.model,
                    renderState,
                    poseStack,
                    getRenderType(this.model, equipmentAsset, GearEquipmentSlot.PARACHUTE),
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    renderState.outlineColor,
                    null
            );
        }
    }
}
