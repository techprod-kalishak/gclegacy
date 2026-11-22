package io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenderState;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenders;
import io.kalishak.galacticraftlegacy.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.component.ProtoShieldControllerData;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.item.ItemStack;

public class ShieldControllerLayer extends GearEquipmentLayer<AvatarRenderState, PlayerModel> {
    public static final Identifier TEXTURES = Constants.texture("entity/equipment/gear/shield.png");
    private final PlayerModel modelSlim;

    protected ShieldControllerLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, EntityModelSet entityModels, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssetManager) {
        super(renderer, new PlayerModel(entityModels.bakeLayer(GalacticraftModelLayers.SHIELD_CONTROLLER), false), layerRenderer, equipmentAssetManager, 0.0F, 0.0F);
        this.modelSlim = new PlayerModel(entityModels.bakeLayer(GalacticraftModelLayers.SHIELD_CONTROLLER_SLIM), true);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int lightCoords, AvatarRenderState renderState, float yRot, float xRot) {
        ItemStack stack = extractFromRenderState(renderState, GearRenderState.SHIELD_CONTROLLER, GearRenders::shieldController);
        ProtoShieldControllerData shieldControllerData = stack.get(GalacticraftDataComponents.SHIELD_CONTROLLER);

        if (stack.is(GalacticraftItems.SHIELD_CONTROLLER) || (shieldControllerData != null && shieldControllerData.getTicksBeforeFatalDamage() > 0)) {
            Model<AvatarRenderState> model = getEffectivePlayerModel(renderState);

            poseStack.pushPose();
            nodeCollector.submitModel(model, renderState, poseStack, model.renderType(TEXTURES), lightCoords, OverlayTexture.NO_OVERLAY, getColor(renderState), null, renderState.outlineColor);
            poseStack.popPose();
        }
    }

    public Model<AvatarRenderState> getEffectivePlayerModel(AvatarRenderState renderState) {
        return renderState.skin.model() == PlayerModelType.SLIM ? this.modelSlim : this.model;
    }

    private static int getColor(AvatarRenderState renderState) {
        float time = (float) (1.0F - Math.sin((renderState.ageInTicks + renderState.partialTick) / 10.0F) * 0.1F);
        float r = 0.9F * time;
        float g = 0.2F * time;
        float b = 0.9F * time;

        return ARGB.colorFromFloat(0.2F, r, g, b);
    }
}
