package io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.EnumExtensions;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenderState;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.item.ItemStack;

public class ThermalPaddingLayer extends GearEquipmentLayer<AvatarRenderState, PlayerModel> implements ThermalEquipmentLayer<AvatarRenderState, PlayerModel> {
    private final ArmorModelSet<PlayerModel> slimModelSet;
    private final ArmorModelSet<PlayerModel> wideModelSet;

    public ThermalPaddingLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, ArmorModelSet<ModelLayerLocation> modelSet, EntityModelSet entityModels, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssetManager) {
        super(renderer, null, layerRenderer, equipmentAssetManager);
        this.slimModelSet = ArmorModelSet.bake(modelSet, entityModels, part -> new PlayerModel(part, false));
        this.wideModelSet = ArmorModelSet.bake(modelSet, entityModels, part -> new PlayerModel(part, true));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, AvatarRenderState renderState, float yRot, float xRot) {
        renderThermalPiece(poseStack, nodeCollector, renderState.getRenderDataOrDefault(GearRenderState.THERMAL_CAP, ItemStack.EMPTY), GearEquipmentSlot.THERMAL_CAP, packedLight, renderState);
        renderThermalPiece(poseStack, nodeCollector, renderState.getRenderDataOrDefault(GearRenderState.THERMAL_SHIRT, ItemStack.EMPTY), GearEquipmentSlot.THERMAL_SHIRT, packedLight, renderState);
        renderThermalPiece(poseStack, nodeCollector, renderState.getRenderDataOrDefault(GearRenderState.THERMAL_LEGGINGS, ItemStack.EMPTY), GearEquipmentSlot.THERMAL_LEGGINGS, packedLight, renderState, EnumExtensions.LAYER_TYPE_THERMAL_PADDING_LEGGINGS.getValue(), PADDING_LEGGINGS_LAYER);
        renderThermalPiece(poseStack, nodeCollector, renderState.getRenderDataOrDefault(GearRenderState.THERMAL_SOCKS, ItemStack.EMPTY), GearEquipmentSlot.THERMAL_SOCKS, packedLight, renderState);
    }

    @Override
    public PlayerModel getModel(EquipmentSlot slot, AvatarRenderState renderState) {
        return renderState.skin.model() == PlayerModelType.SLIM ? this.slimModelSet.get(slot) : this.wideModelSet.get(slot);
    }
}
