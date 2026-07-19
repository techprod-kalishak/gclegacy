package io.kalishak.galacticraftlegacy.client.renderer.environment.sky;

import io.kalishak.galacticraftlegacy.client.renderer.environment.state.SpaceSkyRenderState;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public class BodyRocketView {
    public static void renderEarth(RenderLevelStageEvent.AfterSky event) {
        float scale = event.getLevelRenderState().getRenderDataOrDefault(SpaceSkyRenderState.DISTANCE_FROM_BODY, 1.0F);

    }

    public static void extractRenderState(ExtractLevelRenderStateEvent event) {
        event.getRenderState().setRenderData(SpaceSkyRenderState.DISTANCE_FROM_BODY, getScaledDistance(event.getCamera()));
    }

    private static float getScaledDistance(Camera camera) {
        BlockPos cameraPos = camera.blockPosition();
        int minimumDistance = 256;
        int maximumDistance = 1000;
        float distance = cameraPos.distManhattan(cameraPos.atY(minimumDistance));

        return (distance - minimumDistance) / (maximumDistance - minimumDistance);
    }
}
