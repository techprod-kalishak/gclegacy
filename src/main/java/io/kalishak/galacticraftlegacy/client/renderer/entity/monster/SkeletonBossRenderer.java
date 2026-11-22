package io.kalishak.galacticraftlegacy.client.renderer.entity.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.kalishak.galacticraftlegacy.client.model.entity.SkeletonBossModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear.SkeletonBossItemInHandLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.monster.SkeletonBossRenderState;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.entity.monster.boss.SkeletonBoss;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Pose;
import org.jspecify.annotations.NonNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SkeletonBossRenderer extends MobRenderer<SkeletonBoss, SkeletonBossRenderState, SkeletonBossModel> {
    private static final Identifier TEXTURES = Constants.id("textures/entity/skeleton/skeleton_boss.png");

    public SkeletonBossRenderer(EntityRendererProvider.Context context) {
        super(context, new SkeletonBossModel(context.bakeLayer(GalacticraftModelLayers.SKELETON_BOSS)), 0.9F);
        addLayer(new SkeletonBossItemInHandLayer(this));
    }

    @Override
    public void extractRenderState(SkeletonBoss entity, SkeletonBossRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        HumanoidMobRenderer.extractHumanoidRenderState(entity, state, partialTicks, this.itemModelResolver);
        state.leftArmPose = HumanoidModel.ArmPose.CROSSBOW_HOLD;
        state.rightArmPose = HumanoidModel.ArmPose.CROSSBOW_HOLD;
        state.throwTimer = entity.getThrowTimer();
        state.postThrowDelay = entity.getPostThrowDelay();
    }

    @Override
    protected void setupRotations(SkeletonBossRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
        if (!state.hasPose(Pose.SLEEPING)) {
            poseStack.rotateDegrees(Axis.YP, 180.0F - bodyRot);
        }

        if (state.deathTime > 0) {
            float d = (float) (Math.pow(state.deathTime, 2) / 5.0F);

            poseStack.scale(1.2F, 1.2F, 1.2F);
            poseStack.rotateDegrees(Axis.YP, 2 * d - (float) (Math.pow(state.deathTime - 1, 2) / 5.0F));
        } else if (state.isAutoSpinAttack) {
            poseStack.rotateDegrees(Axis.XP, -90.0F - state.xRot);
            poseStack.rotateDegrees(Axis.YP, state.ageInTicks * -75.0F);
        } else if (state.isUpsideDown) {
            poseStack.translate(0.0F, (state.boundingBoxHeight + 0.1F) / entityScale, 0.0F);
            poseStack.rotateDegrees(Axis.ZP, 180.0F);
        }
    }

    @Override
    public @NonNull Identifier getTextureLocation(SkeletonBossRenderState state) {
        return TEXTURES;
    }

    @Override
    public @NonNull SkeletonBossRenderState createRenderState() {
        return new SkeletonBossRenderState();
    }
}
