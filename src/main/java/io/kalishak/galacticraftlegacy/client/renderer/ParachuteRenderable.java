package io.kalishak.galacticraftlegacy.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;

public interface ParachuteRenderable<S extends EntityRenderState> {
    Model<S> getParachuteModel();
    Material getMaterial(S renderState);
    MaterialSet materials();

    default void renderParachute(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, S renderState) {
        Material material = getMaterial(renderState);
        RenderType renderType = material.renderType(RenderTypes::entityCutout);
        TextureAtlasSprite sprite = materials().get(material);

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
