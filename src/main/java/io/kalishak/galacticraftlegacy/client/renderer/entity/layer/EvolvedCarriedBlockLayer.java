package io.kalishak.galacticraftlegacy.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.monster.EvolvedEndermanRenderState;
import net.minecraft.client.model.monster.enderman.EndermanModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class EvolvedCarriedBlockLayer extends RenderLayer<EvolvedEndermanRenderState, EndermanModel<EvolvedEndermanRenderState>> {
    public EvolvedCarriedBlockLayer(RenderLayerParent<EvolvedEndermanRenderState, EndermanModel<EvolvedEndermanRenderState>> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, EvolvedEndermanRenderState state, float yRot, float xRot) {
        BlockModelRenderState carriedBlock = state.carriedBlock;

        if (!carriedBlock.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 0.6875F, -0.75F);
            poseStack.rotateDegrees(Axis.XP, 20.0F);
            poseStack.rotateDegrees(Axis.YP, 45.0F);
            poseStack.translate(0.25F, 0.1875F, 0.25F);
            poseStack.scale(-0.5F, -0.5F, 0.5F);
            poseStack.rotateDegrees(Axis.YP, 90.0F);
            carriedBlock.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
        }
    }
}
