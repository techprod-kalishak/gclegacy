/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.client.model.block.NasaWorkbenchModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.common.NasaWorkbenchModelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.util.Unit;
import org.joml.Vector3fc;

import java.util.function.Consumer;

public class NasaWorkbenchSpecialRenderer implements NoDataSpecialModelRenderer {
    private final SpriteGetter sprites;
    private final NasaWorkbenchModel model;

    public NasaWorkbenchSpecialRenderer(SpriteGetter sprites, NasaWorkbenchModel model) {
        this.sprites = sprites;
        this.model = model;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 1.1F, 0.5F);
        submitNodeCollector.submitModel(this.model, Unit.INSTANCE, poseStack, lightCoords, OverlayTexture.NO_OVERLAY, -1, NasaWorkbenchModelRenderer.NASA_WORKBENCH_TEXTURE, this.sprites, 0);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {
        PoseStack poseStack = new PoseStack();
        this.model.root().getExtentsForGui(poseStack, consumer);
    }

    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public NasaWorkbenchSpecialRenderer bake(BakingContext context) {
            NasaWorkbenchModel model = new NasaWorkbenchModel(context.entityModelSet().bakeLayer(GalacticraftModelLayers.NASA_WORKBENCH));
            return new NasaWorkbenchSpecialRenderer(context.sprites(), model);
        }
    }
}
