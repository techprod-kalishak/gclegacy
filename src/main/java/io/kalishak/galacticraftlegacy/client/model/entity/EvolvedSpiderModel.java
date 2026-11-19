package io.kalishak.galacticraftlegacy.client.model.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class EvolvedSpiderModel extends EntityModel<LivingEntityRenderState> {
    private final ModelPart head;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightMiddleHindLeg;
    private final ModelPart leftMiddleHindLeg;
    private final ModelPart rightMiddleFrontLeg;
    private final ModelPart leftMiddleFrontLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;

    public EvolvedSpiderModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.rightMiddleHindLeg = root.getChild("right_middle_hind_leg");
        this.leftMiddleHindLeg = root.getChild("left_middle_hind_leg");
        this.rightMiddleFrontLeg = root.getChild("right_middle_front_leg");
        this.leftMiddleFrontLeg = root.getChild("left_middle_front_leg");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild(
                "head", CubeListBuilder.create().texOffs(32, 4).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 15.0F, -3.0F)
        );
        PartDefinition mainBody = root.addOrReplaceChild(
                "body0", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.offset(0.0F, 15.0F, 0.0F)
        );
        root.addOrReplaceChild(
                "body1", CubeListBuilder.create().texOffs(0, 12).addBox(-5.0F, -4.0F, -6.0F, 10.0F, 8.0F, 12.0F), PartPose.offset(0.0F, 15.0F, 9.0F)
        );
        CubeListBuilder rightLeg = CubeListBuilder.create().texOffs(18, 0).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F);
        CubeListBuilder leftLeg = CubeListBuilder.create().texOffs(18, 0).mirror().addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F);
        root.addOrReplaceChild("right_hind_leg", rightLeg, PartPose.offsetAndRotation(-4.0F, 15.0F, 2.0F, 0.0F, (float) (Math.PI / 4), (float) (-Math.PI / 4)));
        root.addOrReplaceChild("left_hind_leg", leftLeg, PartPose.offsetAndRotation(4.0F, 15.0F, 2.0F, 0.0F, (float) (-Math.PI / 4), (float) (Math.PI / 4)));
        root.addOrReplaceChild("right_middle_hind_leg", rightLeg, PartPose.offsetAndRotation(-4.0F, 15.0F, 1.0F, 0.0F, (float) (Math.PI / 8), -0.58119464F));
        root.addOrReplaceChild("left_middle_hind_leg", leftLeg, PartPose.offsetAndRotation(4.0F, 15.0F, 1.0F, 0.0F, (float) (-Math.PI / 8), 0.58119464F));
        root.addOrReplaceChild("right_middle_front_leg", rightLeg, PartPose.offsetAndRotation(-4.0F, 15.0F, 0.0F, 0.0F, (float) (-Math.PI / 8), -0.58119464F));
        root.addOrReplaceChild("left_middle_front_leg", leftLeg, PartPose.offsetAndRotation(4.0F, 15.0F, 0.0F, 0.0F, (float) (Math.PI / 8), 0.58119464F));
        root.addOrReplaceChild(
                "right_front_leg", rightLeg, PartPose.offsetAndRotation(-4.0F, 15.0F, -1.0F, 0.0F, (float) (-Math.PI / 4), (float) (-Math.PI / 4))
        );
        root.addOrReplaceChild("left_front_leg", leftLeg, PartPose.offsetAndRotation(4.0F, 15.0F, -1.0F, 0.0F, (float) (Math.PI / 4), (float) (Math.PI / 4)));

        head.addOrReplaceChild("mask",
                CubeListBuilder.create().texOffs(0, 32)
                        .addBox(-5.0F, -5.0F, -9.0F, 10.0F, 10.0F, 10.0F),
                PartPose.ZERO
        );

        mainBody.addOrReplaceChild("left_tank",
                CubeListBuilder.create().texOffs(40, 34)
                        .addBox( 1.0F, -3.0F,  0.0F, 3.0F, 3.0F, 7.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("right_tank",
                CubeListBuilder.create().texOffs(40, 34)
                        .addBox(-4.0F, -3.0F,  0.0F, 3.0F, 3.0F, 7.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("tube1",
                CubeListBuilder.create().texOffs(40, 32)
                        .addBox( 2.0F,  0.0F, -6.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("tube2",
                CubeListBuilder.create().texOffs(40, 32)
                        .addBox( 2.0F, -1.0F, -5.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("tube3",
                CubeListBuilder.create().texOffs(40, 32)
                        .addBox( 2.0F, -1.0F, -4.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("tube4",
                CubeListBuilder.create().texOffs(40, 32)
                        .addBox( 2.0F, -2.0F, -3.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("tube5",
                CubeListBuilder.create().texOffs(40, 32)
                        .addBox( 2.0F, -3.0F, -2.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("tube6",
                CubeListBuilder.create().texOffs(40, 32)
                        .addBox( 2.0F, -4.0F, -2.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("tube7",
                CubeListBuilder.create().texOffs(40, 32)
                        .addBox( 2.0F, -5.0F, -1.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("tube8",
                CubeListBuilder.create().texOffs(40, 32)
                        .addBox( 2.0F, -5.0F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("tube9",
                CubeListBuilder.create().texOffs(40, 32)
                        .addBox( 2.0F, -4.0F,  0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("tube10",
                CubeListBuilder.create().texOffs(40, 32)
                        .addBox(-3.0F,  0.0F, -6.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("tube11",
                CubeListBuilder.create().texOffs(40, 32)
                        .addBox(-3.0F, -1.0F, -5.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("tube12",
                CubeListBuilder.create().texOffs(40, 32)
                        .addBox(-3.0F, -1.0F, -4.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("tube13",
                CubeListBuilder.create().texOffs(40, 32)
                        .addBox(-3.0F, -2.0F, -3.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("tube15",
                CubeListBuilder.create().texOffs(40, 32)
                        .addBox(-3.0F, -4.0F, -2.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("tube14",
                CubeListBuilder.create().texOffs(40, 32)
                        .addBox(-3.0F, -3.0F, -2.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("tube16",
                CubeListBuilder.create().texOffs(40, 32)
                        .addBox(-3.0F, -5.0F, -1.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("tube17",
                CubeListBuilder.create().texOffs(40, 32)
                        .addBox(-3.0F, -5.0F, -0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0F, -4F, 4F)
        );

        mainBody.addOrReplaceChild("tube18",
                CubeListBuilder.create().texOffs(40, 32)
                        .addBox(-3.0F, -4.0F,  0.5F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0F, -4F, 4F)
        );
        
        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        this.head.yRot = state.yRot * (float) (Math.PI / 180.0);
        this.head.xRot = state.xRot * (float) (Math.PI / 180.0);
        float animationPos = state.walkAnimationPos * 0.6662F;
        float animationSpeed = state.walkAnimationSpeed;
        float swingHind = -(Mth.cos(animationPos * 2.0F + 0.0F) * 0.4F) * animationSpeed;
        float swingMiddleHind = -(Mth.cos(animationPos * 2.0F + (float) Math.PI) * 0.4F) * animationSpeed;
        float swingMiddleFront = -(Mth.cos(animationPos * 2.0F + (float) (Math.PI / 2)) * 0.4F) * animationSpeed;
        float swingFront = -(Mth.cos(animationPos * 2.0F + (float) (Math.PI * 3.0 / 2.0)) * 0.4F) * animationSpeed;
        float stepHind = Math.abs(Mth.sin(animationPos + 0.0F) * 0.4F) * animationSpeed;
        float stepMiddleHind = Math.abs(Mth.sin(animationPos + (float) Math.PI) * 0.4F) * animationSpeed;
        float stepMiddleFrontHind = Math.abs(Mth.sin(animationPos + (float) (Math.PI / 2)) * 0.4F) * animationSpeed;
        float stepFront = Math.abs(Mth.sin(animationPos + (float) (Math.PI * 3.0 / 2.0)) * 0.4F) * animationSpeed;
        this.rightHindLeg.yRot += swingHind;
        this.leftHindLeg.yRot -= swingHind;
        this.rightMiddleHindLeg.yRot += swingMiddleHind;
        this.leftMiddleHindLeg.yRot -= swingMiddleHind;
        this.rightMiddleFrontLeg.yRot += swingMiddleFront;
        this.leftMiddleFrontLeg.yRot -= swingMiddleFront;
        this.rightFrontLeg.yRot += swingFront;
        this.leftFrontLeg.yRot -= swingFront;
        this.rightHindLeg.zRot += stepHind;
        this.leftHindLeg.zRot -= stepHind;
        this.rightMiddleHindLeg.zRot += stepMiddleHind;
        this.leftMiddleHindLeg.zRot -= stepMiddleHind;
        this.rightMiddleFrontLeg.zRot += stepMiddleFrontHind;
        this.leftMiddleFrontLeg.zRot -= stepMiddleFrontHind;
        this.rightFrontLeg.zRot += stepFront;
        this.leftFrontLeg.zRot -= stepFront;
    }
}
