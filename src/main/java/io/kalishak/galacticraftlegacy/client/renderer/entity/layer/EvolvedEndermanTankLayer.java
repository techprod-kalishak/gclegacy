package io.kalishak.galacticraftlegacy.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear.OxygenTankLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.EvolvedEndermanRenderState;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.monster.enderman.EndermanModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.resources.model.EquipmentAssetManager;

public class EvolvedEndermanTankLayer extends OxygenTankLayer<EvolvedEndermanRenderState, EndermanModel<EvolvedEndermanRenderState>> {
    public EvolvedEndermanTankLayer(RenderLayerParent<EvolvedEndermanRenderState, EndermanModel<EvolvedEndermanRenderState>> renderer, EntityModelSet modelSet, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssets) {
        super(renderer, modelSet, layerRenderer, equipmentAssets, -0.70F);
    }

    @Override
    protected void transformTanks(PoseStack poseStack, boolean isLeftTank) {
        if (isLeftTank) {
            poseStack.translate(0.12F, this.yOffset, 0.255F);
        } else {
            poseStack.translate(-0.175F, this.yOffset, 0.255F);
        }
    }
}
