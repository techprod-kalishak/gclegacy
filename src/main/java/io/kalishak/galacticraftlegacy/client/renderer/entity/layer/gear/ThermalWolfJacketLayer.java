package io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenderState;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import net.minecraft.client.model.animal.wolf.WolfModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class ThermalWolfJacketLayer extends GearEquipmentLayer<WolfRenderState, WolfModel> implements ThermalEquipmentLayer<WolfRenderState, WolfModel> {
    private final WolfModel babyModel;

    public ThermalWolfJacketLayer(RenderLayerParent<WolfRenderState, WolfModel> renderer, EntityModelSet entityModels, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssetManager) {
        super(renderer, WolfModel::new, GalacticraftModelLayers.WOLF_THERMAL, entityModels, layerRenderer, equipmentAssetManager);
        this.babyModel = new WolfModel(entityModels.bakeLayer(GalacticraftModelLayers.WOLF_BABY_THERMAL));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, WolfRenderState renderState, float yRot, float xRot) {
        renderThermalPiece(poseStack, nodeCollector, renderState.getRenderDataOrDefault(GearRenderState.GEAR_BODY_ITEM, ItemStack.EMPTY), GearEquipmentSlot.BODY, packedLight, renderState, EquipmentClientInfo.LayerType.WOLF_BODY, PADDING_LAYER);
    }

    @Override
    public WolfModel getModel(EquipmentSlot slot, WolfRenderState renderState) {
        return renderState.isBaby ? this.babyModel : (WolfModel) this.model;
    }
}
