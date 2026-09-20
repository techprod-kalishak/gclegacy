package io.kalishak.galacticraftlegacy.client.renderer.common;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.math.Transformation;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public interface CustomChestRenderer<S> extends CommonEntityBlockRenderer<S> {
    Map<Direction, Transformation> TRANSFORMATIONS = Util.makeEnumMap(Direction.class, CustomChestRenderer::createModelTransformation);

    private static Transformation createModelTransformation(Direction facing) {
        return new Transformation((new Matrix4f()).rotationAround(Axis.YP.rotationDegrees(-facing.toYRot()), 0.5F, 0.0F, 0.5F));
    }

    Model<Float> getChestModel();
    float getOpenness(S renderState);
    Direction getFacing(S renderState);

    @Override
    default void submitCommonModel(S renderState, SubmitNodeCollector submitNodeCollector, PoseStack poseStack, SpriteGetter sprites, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        poseStack.pushPose();
        poseStack.mulPose(CustomChestRenderer.TRANSFORMATIONS.get(getFacing(renderState)));
        poseStack.translate(-0.5F, -0.5F, -0.5F);

        Model<Float> chestModel = getChestModel();
        float openness = getOpenness(renderState);
        int lightCoords = getLightCoords(renderState);
        SpriteId spriteId = getSprite(sprites, renderState);
        submitNodeCollector.submitModel(chestModel, openness, poseStack, lightCoords, OverlayTexture.NO_OVERLAY, -1, spriteId, sprites, 0);

        if (crumblingOverlay != null) {
            submitNodeCollector.submitCrumblingOverlay(chestModel, openness, poseStack, spriteId.renderType(getChestModel().renderType()), lightCoords, OverlayTexture.NO_OVERLAY, -1, crumblingOverlay);
        }

        poseStack.popPose();
    }
}
