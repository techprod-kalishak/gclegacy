package io.kalishak.galacticraftlegacy.client.model.entity;

import io.kalishak.galacticraftlegacy.client.renderer.entity.state.monster.SkeletonBossRenderState;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SkeletonBossModel extends HumanoidModel<SkeletonBossRenderState> {
    public SkeletonBossModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        float halfPI = Mth.HALF_PI;

        PartDefinition head = partdefinition.addOrReplaceChild("head",
                CubeListBuilder.create().mirror().texOffs(0, 16)
                        .addBox(-4.0F, -8.0F, -6.0F, 8.0F, 8.0F, 8.0F),
                PartPose.offsetAndRotation(0F, -24F, 6F, 0.122173F, 0F, 0F)
        );
        head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

        partdefinition.addOrReplaceChild("pelvis",
                CubeListBuilder.create().mirror().texOffs(32, 19)
                        .addBox(-6.0F,  0.0F, -3.0F, 12.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(0F, -2F, 5F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("sternum",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox(-1.5F,  0.0F, -1.0F, 3.0F, 9.0F, 1.0F),
                PartPose.offsetAndRotation(0F, -21F, 2F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("right_leg",
                CubeListBuilder.create().mirror().texOffs(56, 33)
                        .addBox(-2.0F,  0.0F, -2.0F, 3.0F, 26.0F, 3.0F),
                PartPose.offsetAndRotation(-5F, 0F, 5F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("right_arm",
                CubeListBuilder.create().mirror().texOffs(56, 33)
                        .addBox(-2.0F, -2.0F, -1.5F, 3.0F, 24.0F, 3.0F),
                PartPose.offsetAndRotation(-8F, -20F, 5F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("body",
                CubeListBuilder.create().mirror().texOffs(32, 33)
                        .addBox(-1.5F,  0.0F, -1.0F, 3.0F, 22.0F, 2.0F),
                PartPose.offsetAndRotation(0F, -24F, 6F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("left_arm",
                CubeListBuilder.create().mirror().texOffs(56, 33)
                        .addBox(-1.0F, -2.0F, -1.5F, 3.0F, 24.0F, 3.0F),
                PartPose.offsetAndRotation(8F, -20F, 5F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("left_leg",
                CubeListBuilder.create().mirror().texOffs(56, 33)
                        .addBox(-2.0F,  0.0F, -2.0F, 3.0F, 26.0F, 3.0F),
                PartPose.offsetAndRotation(6F, 0F, 5F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("leftFrontBotRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox(-1.0F,  0.0F,  0.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(7F, -13F, 2F, 0F, -halfPI, 0F)
        );

        partdefinition.addOrReplaceChild("leftFrontTopRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox(-1.0F,  0.0F,  0.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(7F, -22F, 2F, 0F, -halfPI, 0F)
        );

        partdefinition.addOrReplaceChild("leftFront2ndRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox(-1.0F,  0.0F,  0.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(7F, -19F, 2F, 0F, -halfPI, 0F)
        );

        partdefinition.addOrReplaceChild("leftFront3rdRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox(-1.0F,  0.0F,  0.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(7F, -16F, 2F, 0F, -halfPI, 0F)
        );

        partdefinition.addOrReplaceChild("leftSideBotRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox(-1.0F,  0.0F, -6.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(7F, -13F, 7F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("leftSide3rdRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox(-1.0F,  0.0F, -6.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(7F, -16F, 7F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("leftSide2ndRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox(-1.0F,  0.0F, -6.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(7F, -19F, 7F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("leftSideTopRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox(-1.0F,  0.0F, -6.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(7F, -22F, 7F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("rightSideTopRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox( 0.0F,  0.0F, -6.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-7F, -22F, 7F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("rightSide2ndRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox( 0.0F,  0.0F, -6.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-7F, -19F, 7F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("rightSide3rdRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox( 0.0F,  0.0F, -6.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-7F, -16F, 7F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("rightSideBotRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox( 0.0F,  0.0F, -6.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-7F, -13F, 7F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("rightFrontBotRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox( 0.0F,  0.0F,  0.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-7F, -13F, 2F, 0F, halfPI, 0F)
        );

        partdefinition.addOrReplaceChild("rightFront3rdRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox( 0.0F,  0.0F,  0.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-7F, -16F, 2F, 0F, halfPI, 0F)
        );

        partdefinition.addOrReplaceChild("rightFront2ndRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox( 0.0F,  0.0F,  0.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-7F, -19F, 2F, 0F, halfPI, 0F)
        );

        partdefinition.addOrReplaceChild("rightFrontTopRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox( 0.0F,  0.0F,  0.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-7F, -22F, 2F, 0F, halfPI, 0F)
        );

        partdefinition.addOrReplaceChild("leftBackTopRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox(-1.0F,  0.0F,  0.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(7F, -22F, 7F, 0F, -halfPI, 0F)
        );

        partdefinition.addOrReplaceChild("leftBack2ndRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox(-1.0F,  0.0F,  0.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(7F, -19F, 7F, 0F, -halfPI, 0F)
        );

        partdefinition.addOrReplaceChild("leftBack3rdRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox(-1.0F,  0.0F,  0.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(7F, -16F, 7F, 0F, -halfPI, 0F)
        );

        partdefinition.addOrReplaceChild("leftBackBotRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox(-1.0F,  0.0F,  0.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(7F, -13F, 7F, 0F, -halfPI, 0F)
        );

        partdefinition.addOrReplaceChild("rightBackBotRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox( 0.0F,  0.0F,  0.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-7F, -13F, 7F, 0F, halfPI, 0F)
        );

        partdefinition.addOrReplaceChild("rightBack3rdRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox( 0.0F,  0.0F,  0.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-7F, -16F, 7F, 0F, halfPI, 0F)
        );

        partdefinition.addOrReplaceChild("rightBack2ndRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox( 0.0F,  0.0F,  0.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-7F, -19F, 7F, 0F, halfPI, 0F)
        );

        partdefinition.addOrReplaceChild("rightBackTopRib",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox( 0.0F,  0.0F,  0.0F, 1.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-7F, -22F, 7F, 0F, halfPI, 0F)
        );

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(SkeletonBossRenderState state) {
        resetPose();

        this.head.xRot = state.xRot * Mth.PI / 180.0F;
        this.head.yRot = state.yRot * Mth.PI / 180.0F;

        if (state.isFallFlying) {
            this.head.xRot = -Mth.PI / 4;
        }

        float animationPos = state.walkAnimationPos;
        float animationSpeed = state.walkAnimationSpeed;
        this.rightArm.xRot = Mth.cos(animationPos * 0.6662F + Mth.PI) * 2.0F * animationSpeed * 0.5F / state.speedValue;
        this.leftArm.xRot = Mth.cos(animationPos * 0.6662F) * 2.0F * animationSpeed * 0.5F / state.speedValue;
        this.rightLeg.xRot = Mth.cos(animationPos * 0.6662F) * 1.4F * animationSpeed / state.speedValue;
        this.rightLeg.yRot = 0.F;
        this.rightLeg.zRot = 0.0F;
        this.leftLeg.xRot = Mth.cos(animationPos * 0.6662F + Mth.PI) * 1.4F * animationSpeed / state.speedValue;
        this.leftLeg.yRot = 0.0F;
        this.leftLeg.zRot = 0.0F;

        if (state.isPassenger) {
            this.rightArm.xRot -= Mth.PI / 5.0F;
            this.leftArm.xRot -= Mth.PI / 5.0F;
            this.rightLeg.xRot = -Mth.PI * 2.0F / 5.0F;
            this.leftLeg.xRot = -Mth.PI * 2.0F / 5.0F;
            this.rightLeg.yRot = Mth.PI / 10.0F;
            this.leftLeg.yRot = -Mth.PI / 10.0F;
        }

        this.rightArm.yRot = state.yRot / Mth.RAD_TO_DEG;
        this.leftArm.yRot = state.yRot / Mth.RAD_TO_DEG;
        float swing;
        float swingPi;

        if (state.swingAnimation > -9990.0F) {
            swing = state.swingAnimation;
            this.body.yRot = Mth.sin(Mth.sqrt(swing) * Mth.TWO_PI) * 0.2F;

            this.rightArm.z = Mth.sin(this.body.yRot) * 5.0F;
            this.rightArm.x = -Mth.cos(this.body.yRot) * 5.0F;
            this.leftArm.z = -Mth.sin(this.body.yRot) * 5.0F;
            this.leftArm.x = Mth.cos(this.body.yRot) * 5.0F;
            this.rightArm.yRot += this.body.yRot;
            this.leftArm.yRot += this.body.yRot;
            this.leftArm.xRot += this.body.xRot;
            swing = 1.0F - state.swingAnimation;
            swing *= swing;
            swing *= swing;
            swing = 1.0F - swing;
            swingPi = Mth.sin(swing * Mth.PI);
            final float var9 = Mth.sin(state.swingAnimation * Mth.PI) * -(this.head.xRot - 0.7F) * 0.75F;
            this.rightArm.xRot = (float) (this.rightArm.xRot - (swingPi * 1.2D + var9));
            this.rightArm.yRot += this.body.yRot * 2.0F;
            this.rightArm.zRot = Mth.sin(state.swingAnimation * Mth.PI) * -0.4F;
        }

        final float f6 = Mth.sin(state.swingAnimation * Mth.PI);
        final float f7 = Mth.sin((1.0F - (1.0F - state.swingAnimation) * (1.0F - state.swingAnimation)) * Mth.PI);
        this.rightArm.zRot = 0.0F;
        this.leftArm.zRot = 0.0F;
        this.rightArm.yRot = 0.5F * (state.yRot / Mth.RAD_TO_DEG) + -(0.1F - f6 * 0.6F);
        this.leftArm.yRot = 0.5F * (state.yRot / Mth.RAD_TO_DEG) + 0.1F - f6 * 0.6F;
        this.rightArm.xRot = -(Mth.PI / 2F);
        this.leftArm.xRot = -(Mth.PI / 2F);
        this.rightArm.xRot -= f6 * 1.2F - f7 * 0.4F;
        this.leftArm.xRot -= f6 * 1.2F - f7 * 0.4F;
        this.rightArm.zRot += Mth.cos(state.bodyRot * 0.09F) * 0.05F + 0.05F;
        this.leftArm.zRot -= Mth.cos(state.bodyRot * 0.09F) * 0.05F + 0.05F;
        this.rightArm.xRot += Mth.sin(state.bodyRot * 0.067F) * 0.05F;
        this.leftArm.xRot -= Mth.sin(state.bodyRot * 0.067F) * 0.05F;

        if (state.deathTime > 0) {
            this.leftArm.xRot = -(Mth.PI / 2F) + (float) (Math.pow(state.deathTime, 2) / 5.0F) / 3.0F / Mth.RAD_TO_DEG;
            this.rightArm.xRot = -(Mth.PI / 2F) + (float) (Math.pow(state.deathTime, 2) / 5.0F) / 2.0F / Mth.RAD_TO_DEG;
        }

        if (state.throwTimer + state.postThrowDelay > 0) {
            this.rightArm.xRot -= Mth.cos((state.throwTimer + state.postThrowDelay) * 0.05F) * 1.2F + 0.05F;
            this.leftArm.xRot -= Mth.cos((state.throwTimer + state.postThrowDelay) * 0.05F) * 1.2F + 0.05F;
        }
    }

    @Override
    protected void setupAttackAnimation(SkeletonBossRenderState state) {
        super.setupAttackAnimation(state);
        animateArms(getArm(HumanoidArm.LEFT), getArm(HumanoidArm.RIGHT), state);
    }

    private static void animateArms(ModelPart leftArm, ModelPart rightArm, SkeletonBossRenderState state) {
        float armDrop = (float) -Math.PI / 2.0F;
        float attackYRotModifier = Mth.sin(state.swingAnimation * (float) Math.PI);
        float attackXRotModifier = Mth.sin((1.0F - (1.0F - state.swingAnimation) * (1.0F - state.swingAnimation)) * (float) Math.PI);
        float xRot = armDrop + attackYRotModifier * 1.2F - attackXRotModifier * 0.4F;
        float yRot = 0.1F - attackYRotModifier * 0.6F;

        if (state.rightArmPose != HumanoidModel.ArmPose.THROW_TRIDENT) {
            rightArm.xRot = xRot;
            rightArm.yRot = -yRot;
            rightArm.zRot = 0.0F;
        }

        if (state.leftArmPose != HumanoidModel.ArmPose.THROW_TRIDENT) {
            leftArm.xRot = xRot;
            leftArm.yRot = yRot;
            leftArm.zRot = 0.0F;
        }
    }
}
