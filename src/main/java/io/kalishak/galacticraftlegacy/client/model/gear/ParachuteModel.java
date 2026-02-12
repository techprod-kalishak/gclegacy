package io.kalishak.galacticraftlegacy.client.model.gear;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;

import java.util.function.UnaryOperator;

public class ParachuteModel<S extends EntityRenderState> extends Model<S> {
    private final ModelPart parachute;
    private final ModelPart parachuteSideLeft;
    private final ModelPart parachuteSideRight;
    private final ModelPart leftString;
    private final ModelPart rightString;
    private final ModelPart leftRearString;
    private final ModelPart rightRearString;

    public ParachuteModel(ModelPart root) {
        super(root, RenderTypes::entitySolid);

        this.parachute = root.getChild("parachute");
        this.parachuteSideLeft = root.getChild("parachute_side_left");
        this.parachuteSideRight = root.getChild("parachute_side_right");
        this.leftString = root.getChild("left_string");
        this.rightString = root.getChild("right_string");
        this.leftRearString = root.getChild("left_rear_string");
        this.rightRearString = root.getChild("right_rear_string");
    }

    public static LayerDefinition createParachuteLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        root.addOrReplaceChild(
                "parachute",
                CubeListBuilder.create().texOffs(0, 42).addBox(-20.0F, -45.0F, -20.0F, 40.0F, 2.0F, 40.0F),
                PartPose.ZERO
        );
        root.addOrReplaceChild(
                "parachute_side_left",
                CubeListBuilder.create().texOffs(0, 0).addBox(-20.0F, -45.0F, -20.0F, 10.0F, 2.0F, 40.0F),
                PartPose.rotation(15.0F, 4.0F, 0.0F)
        );
        root.addOrReplaceChild(
                "parachute_side_right",
                CubeListBuilder.create().texOffs(0, 0).addBox(-20.0F, -45.0F, -20.0F, 10.0F, 2.0F, 40.0F),
                PartPose.rotation(11.0F, -11.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "left_string",
                CubeListBuilder.create().texOffs(100, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 40.0F, 1.0F),
                PartPose.rotation(9.0F, -7.0F, 2.0F)
        );
        root.addOrReplaceChild(
                "right_string",
                CubeListBuilder.create().texOffs(100, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 40.0F, 1.0F),
                PartPose.rotation(9.0F, -7.0F, 2.0F)
        );
        root.addOrReplaceChild(
                "left_rear_string",
                CubeListBuilder.create().texOffs(100, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 40.0F, 1.0F),
                PartPose.rotation(9.0F, -7.0F, 2.0F)
        );
        root.addOrReplaceChild(
                "right_rear_string",
                CubeListBuilder.create().texOffs(100, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 40.0F, 1.0F),
                PartPose.rotation(9.0F, -7.0F, 2.0F)
        );

        return LayerDefinition.create(meshDefinition, 256, 256);
    }

    protected UnaryOperator<Float> animationSupplier() {
        return rot -> rot * (float) (Math.PI / 180);
    }

    @Override
    public void setupAnim(S renderState) {
        resetPose();
        this.parachuteSideLeft.zRot = animationSupplier().apply(30.0F);
        this.parachuteSideRight.zRot = -animationSupplier().apply(30.0F);
        this.leftString.zRot = animationSupplier().apply(155.0F);
        this.leftString.xRot = animationSupplier().apply(23.0F);
        this.rightString.zRot = animationSupplier().apply(155.0F);
        this.rightString.xRot = -animationSupplier().apply(23.0F);
        this.leftRearString.zRot = -animationSupplier().apply(155.0F);
        this.leftRearString.xRot = animationSupplier().apply(23.0F);
        this.rightRearString.zRot = -animationSupplier().apply(155.0F);
        this.rightRearString.xRot = -animationSupplier().apply(23.0F);
    }
}
