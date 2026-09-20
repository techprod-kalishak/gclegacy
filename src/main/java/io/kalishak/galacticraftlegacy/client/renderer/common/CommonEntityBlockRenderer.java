package io.kalishak.galacticraftlegacy.client.renderer.common;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import org.jspecify.annotations.Nullable;

public interface CommonEntityBlockRenderer<S> {
    SpriteId getSprite(SpriteGetter spriteGetter, S renderState);

    void submitCommonModel(S renderState, SubmitNodeCollector submitNodeCollector, PoseStack poseStack, SpriteGetter sprites, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay);

    default int getLightCoords(S renderState) {
        return switch (renderState) {
            case BlockEntityRenderState blockEntityRenderState -> blockEntityRenderState.lightCoords;
            case EntityRenderState entityRenderState -> entityRenderState.lightCoords;
            default -> 0;
        };
    }
}
