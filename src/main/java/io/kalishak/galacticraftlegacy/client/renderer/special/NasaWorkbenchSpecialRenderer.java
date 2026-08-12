/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.model.object.NasaWorkbenchModel;
import io.kalishak.galacticraftlegacy.client.renderer.blockentity.NasaWorkbenchBlockRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import org.joml.Vector3fc;

import java.util.function.Consumer;

public class NasaWorkbenchSpecialRenderer implements NoDataSpecialModelRenderer {
    private final SpriteGetter sprites;
    private final NasaWorkbenchModel model;
    private final SpriteId sprite;

    public NasaWorkbenchSpecialRenderer(SpriteGetter sprites, NasaWorkbenchModel model, SpriteId sprite) {
        this.sprites = sprites;
        this.model = model;
        this.sprite = sprite;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 1.0F, 0.5F);
        submitNodeCollector.submitModel(this.model, null, poseStack, lightCoords, overlayCoords, -1, this.sprite, this.sprites, outlineColor, null);
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
            SpriteId spriteId = NasaWorkbenchBlockRenderer.NASA_WORKBENCH_TEXTURE;
            return new NasaWorkbenchSpecialRenderer(context.sprites(), model, spriteId);
        }
    }
}
