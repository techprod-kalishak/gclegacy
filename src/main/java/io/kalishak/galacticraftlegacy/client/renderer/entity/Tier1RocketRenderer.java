package io.kalishak.galacticraftlegacy.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.kalishak.galacticraftlegacy.client.model.entity.Tier1RocketModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.RocketRenderState;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket.Tier1Rocket;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;

public class Tier1RocketRenderer extends EntityRenderer<Tier1Rocket, RocketRenderState> {
    private static final Identifier TEXTURES = Constants.texture("");
    private final Tier1RocketModel model;

    public Tier1RocketRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.9F;
        this.model = new Tier1RocketModel(context.bakeLayer(GalacticraftModelLayers.TIER_1_ROCKET));
    }

    @Override
    public RocketRenderState createRenderState() {
        return new RocketRenderState();
    }

    @Override
    public void extractRenderState(Tier1Rocket entity, RocketRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.launchPhase = entity.getLaunchPhase();
        state.roll = entity.getRollAmplitude();
        state.launched = entity.isLaunched();
        state.timeUntilLaunch = entity.getTimeUntilLaunch();
        state.previousXRot = entity.xRotO;
        state.previousYRot = entity.yRotO;
    }

    @Override
    public void submit(RocketRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();

        if (state.roll > 0.0F) {
            float i = state.launched ? (5 - Mth.floor(state.timeUntilLaunch / 85.0F)) / 10.0F : 0.3F;
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(state.roll) * state.roll * i));
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(state.roll) * state.roll * i));
        }

        poseStack.scale(-1.0F, -1.0F, -1.0F);
        submitNodeCollector.submitModel(
                this.model,
                state,
                poseStack,
                this.model.renderType(TEXTURES),
                state.lightCoords,
                -1,
                -1,
                null,
                state.outlineColor,
                null
        );
        poseStack.popPose();
    }

    @Override
    public boolean shouldRender(Tier1Rocket entity, Frustum culler, double camX, double camY, double camZ) {
        if (!entity.shouldRender(camX, camY, camZ)) {
            return false;
        }

        if (!affectedByCulling(entity)) {
            return true;
        }

        AABB boundingBox = getBoundingBoxForCulling(entity).inflate(0.6, 2, 0.6);
        if (boundingBox.hasNaN() || boundingBox.getSize() == 0.0) {
            boundingBox = new AABB(entity.getX() - 2.0, entity.getY() - 2.0, entity.getZ() - 2.0, entity.getX() + 2.0, entity.getY() + 2.0, entity.getZ() + 2.0);
        }

        return culler.isVisible(boundingBox);
    }
}
