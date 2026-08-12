/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.model.gear;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;

import java.util.Set;

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
        this.parachuteSideLeft = root.getChild("side_left");
        this.parachuteSideRight = root.getChild("side_right");
        this.leftString = root.getChild("left_string");
        this.rightString = root.getChild("right_string");
        this.leftRearString = root.getChild("left_rear_string");
        this.rightRearString = root.getChild("right_rear_string");
    }

    public static LayerDefinition createParachuteLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild(
                "parachute",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40),
                PartPose.offset(15.0F, 4.0F, 0.0F)
        );
        partDefinition.addOrReplaceChild(
                "side_left",
                CubeListBuilder.create().texOffs(0, 42)
                        .addBox(-20.0F, -45.0F, -20.0F, 40, 2, 40),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );
        partDefinition.addOrReplaceChild(
                "side_right",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-20.0F, -45.0F, -20.0F, 10, 2, 40),
                PartPose.offset(11.0F, -11.0F, 0.0F)
        );

        partDefinition.addOrReplaceChild(
                "left_string",
                CubeListBuilder.create().texOffs(100, 0)
                        .addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );
        partDefinition.addOrReplaceChild(
                "right_string",
                CubeListBuilder.create().texOffs(100, 0)
                        .addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );
        partDefinition.addOrReplaceChild(
                "left_rear_string",
                CubeListBuilder.create().texOffs(100, 0)
                        .addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );
        partDefinition.addOrReplaceChild(
                "right_rear_string",
                CubeListBuilder.create().texOffs(100, 0)
                        .addBox(-0.5F, 0.0F, -0.5F, 1, 40, 1),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        return LayerDefinition.create(meshDefinition, 256, 256);
    }

    public static LayerDefinition createParachuteLayerOLD() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition root = partDefinition.addOrReplaceChild("parachute", CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-14.0F, -22.0F, 0.0F, 12.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offset(8.0F, 24.0F, -8.0F)
        );
        root.addOrReplaceChild(
                "side_left",
                CubeListBuilder.create()
                        .texOffs(0, 17)
                        .addBox(-12.0F, -11.0F, -6.0F, 10.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-8.0F, -12.0F, 6.0F, 0.0F, 0.0F, -0.3491F)
        );
        root.addOrReplaceChild(
                "side_right",
                CubeListBuilder.create()
                        .texOffs(0, 17)
                        .addBox(9.0F, -3.0F, -6.0F, 10.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-12.0F, -22.0F, 6.0F, 0.0F, 0.0F, 0.3491F)
        );
        root.addOrReplaceChild(
                "left_string",
                CubeListBuilder.create()
                        .texOffs(0, 34)
                        .addBox(-1.0F, -25.0F, 0.0F, 1.0F, 25.0F, 1.0F, new CubeDeformation(-0.45F)),
                PartPose.offsetAndRotation(-8.0F, 0.0F, 8.0F, 0.3491F, 0.0F, 0.6981F)
        );
        root.addOrReplaceChild(
                "right_string",
                CubeListBuilder.create()
                        .texOffs(0, 34)
                        .addBox(-1.0F, -25.0F, 0.0F, 1.0F, 25.0F, 1.0F, new CubeDeformation(-0.45F)),
                PartPose.offsetAndRotation(-8.0F, 0.0F, 8.0F, 0.3491F, 0.0F, -0.6545F)
        );
        root.addOrReplaceChild(
                "left_rear_string",
                CubeListBuilder.create()
                        .texOffs(0, 34)
                        .addBox(-1.0F, -25.0F, 0.0F, 1.0F, 25.0F, 1.0F, new CubeDeformation(-0.45F)),
                PartPose.offsetAndRotation(-8.0F, 0.0F, 8.0F, -0.3054F, 0.0F, 0.6981F)
        );
        root.addOrReplaceChild(
                "right_rear_string",
                CubeListBuilder.create()
                        .texOffs(0, 34)
                        .addBox(-1.0F, -25.0F, 0.0F, 1.0F, 25.0F, 1.0F, new CubeDeformation(-0.45F)),
                PartPose.offsetAndRotation(-8.0F, 0.0F, 8.0F, -0.3054F, 0.0F, -0.6545F)
        );

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    private Set<ModelPart> getModelParts() {
        ImmutableSet.Builder<ModelPart> set = ImmutableSet.<ModelPart>builder()
                .add(this.parachute)
                .add(this.parachuteSideLeft)
                .add(this.parachuteSideRight)
                .add(this.leftString)
                .add(this.rightString)
                .add(this.leftRearString)
                .add(this.rightRearString);

        return set.build();
    }

    @Override
    public void setupAnim(S renderState) {
        resetPose();

        if (renderState instanceof LivingEntityRenderState livingEntityRenderState) {
            getModelParts().forEach(modelPart -> modelPart.zRot = livingEntityRenderState.bodyRot * (float) (Math.PI / 180.0F));
        }
    }
}
