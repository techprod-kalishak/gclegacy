package io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.client.model.gear.GearEquipmentModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

public class OxygenMaskLayer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends GearEquipmentLayer<S, M> {
    public static final Identifier TEXTURES = Constants.texture("entity/equipment/gear/oxygen_mask.png");

    public OxygenMaskLayer(RenderLayerParent<S, M> renderer, EntityModelSet modelSet, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssets) {
        super(renderer, GearEquipmentModel.simple(modelSet.bakeLayer(GalacticraftModelLayers.OXYGEN_MASK), RenderTypes::entityCutout), layerRenderer, equipmentAssets);
    }

    public static LayerDefinition createOxygenMaskLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F), PartPose.rotation(0.0F, 0.0F, 0.0F)
        );

        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, S renderState, float yRot, float xRot) {
        if (getDataFromContext(renderState, GearRenderState.HAS_OXYGEN_MASK, false)) {
            poseStack.pushPose();
            poseStack.translate(0.0F, renderState.entityType.equals(EntityType.CREEPER) ? 0.39F : 0.02F, 0.0F);
            poseStack.scale(0.57F, 0.57F, 0.57F);
            nodeCollector.order(1).submitModel(this.model, renderState, poseStack, this.model.renderType(TEXTURES), packedLight, OverlayTexture.NO_OVERLAY, renderState.outlineColor, null);
            poseStack.scale(1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
    }
}
