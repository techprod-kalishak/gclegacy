package io.kalishak.galacticraftlegacy.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.resources.model.geometry.QuadCollection;

import java.util.function.Consumer;

public interface ObjModelHelper {
    static void renderQuads(QuadCollection quadCollection, PoseStack poseStack, SubmitNodeCollector nodeCollector, RenderType renderType, QuadInstance quadInstance) {
        nodeCollector.submitCustomGeometry(poseStack, renderType, (pose, vertexConsumer) -> {
            quadCollection.getQuads(null).forEach(quad -> vertexConsumer.putBakedQuad(pose, quad, quadInstance));
        });
    }

    static void renderQuads(QuadCollection quadCollection, PoseStack poseStack, SubmitNodeCollector nodeCollector, RenderType renderType, Consumer<QuadInstance> quadModifier) {
        QuadInstance instance = new QuadInstance();
        quadModifier.accept(instance);
        renderQuads(quadCollection, poseStack, nodeCollector, renderType, instance);
    }
}
