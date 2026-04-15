/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;

public interface ParachuteRenderable<S extends EntityRenderState> {
    Model<S> getParachuteModel();
    SpriteId getParachuteMaterial(S renderState);
    SpriteGetter spriteGetter();

    default void renderParachute(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, S renderState) {
        SpriteId spriteId = getParachuteMaterial(renderState);
        RenderType renderType = spriteId.renderType(RenderTypes::entityCutout);
        TextureAtlasSprite sprite = spriteGetter().get(spriteId);

        nodeCollector.submitModel(
                getParachuteModel(),
                renderState,
                poseStack,
                renderType,
                renderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                packedLight,
                sprite,
                renderState.outlineColor,
                null
        );
    }
}
