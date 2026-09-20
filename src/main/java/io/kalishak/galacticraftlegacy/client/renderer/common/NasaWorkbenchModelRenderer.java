package io.kalishak.galacticraftlegacy.client.renderer.common;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.client.model.block.NasaWorkbenchModel;
import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.util.Unit;
import org.jspecify.annotations.Nullable;

public interface NasaWorkbenchModelRenderer<S> extends CommonEntityBlockRenderer<S> {
    SpriteId NASA_WORKBENCH_TEXTURE = Sheets.BLOCK_ENTITIES_MAPPER.apply(Constants.id("nasa_workbench/workbench_arms"));

    NasaWorkbenchModel getWorkbenchModel();

    @Override
    default SpriteId getSprite(SpriteGetter spriteGetter, S renderState) {
        return NASA_WORKBENCH_TEXTURE;
    }

    @Override
    default void submitCommonModel(S renderState, SubmitNodeCollector submitNodeCollector, PoseStack poseStack, SpriteGetter sprites, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        int lightCoords = getLightCoords(renderState);
        NasaWorkbenchModel model = getWorkbenchModel();

        poseStack.pushPose();
        poseStack.translate(0.5F, 1.1F, 0.5F);

        submitNodeCollector.submitModel(model, Unit.INSTANCE, poseStack, lightCoords, OverlayTexture.NO_OVERLAY, -1, NASA_WORKBENCH_TEXTURE, sprites, 0);

        if (crumblingOverlay != null) {
            submitNodeCollector.submitCrumblingOverlay(model, Unit.INSTANCE, poseStack, NASA_WORKBENCH_TEXTURE.renderType(model.renderType()), lightCoords, OverlayTexture.NO_OVERLAY, -1, crumblingOverlay);
        }

        poseStack.popPose();
    }
}
