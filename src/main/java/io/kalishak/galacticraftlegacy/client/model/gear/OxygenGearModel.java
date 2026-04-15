/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.model.gear;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class OxygenGearModel<S extends LivingEntityRenderState> extends GearEquipmentModel<S> {
    public final ModelPart[][] tubes = new ModelPart[2][7];

    public OxygenGearModel(ModelPart root) {
        super(root, RenderTypes::entitySolid);

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                this.tubes[j][i] = root.getChild("tube_" + j + "_" + i);
            }
        }
    }

    public static LayerDefinition createOxygenGearLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        root.addOrReplaceChild(
                "tube_0_0",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F)
                        .mirror(),
                PartPose.rotation(2.0F, 3.0F, 5.8F)
        );
        root.addOrReplaceChild(
                "tube_0_1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F)
                        .mirror(),
                PartPose.rotation(2.0F, 2.0F, 6.8F)
        );
        root.addOrReplaceChild(
                "tube_0_2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F)
                        .mirror(),
                PartPose.rotation(2.0F, 1.0F, 6.8F)
        );
        root.addOrReplaceChild(
                "tube_0_3",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F)
                        .mirror(),
                PartPose.rotation(2.0F, 0.0F, 6.8F)
        );
        root.addOrReplaceChild(
                "tube_0_4",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F)
                        .mirror(),
                PartPose.rotation(2.0F, -1.0F, 6.8F)
        );
        root.addOrReplaceChild(
                "tube_0_5",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F)
                        .mirror(),
                PartPose.rotation(2.0F, -2.0F, 5.8F)
        );
        root.addOrReplaceChild(
                "tube_0_6",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F)
                        .mirror(),
                PartPose.rotation(2.0F, -3.0F, 4.8F)
        );

        root.addOrReplaceChild(
                "tube_1_0",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F)
                        .mirror(),
                PartPose.rotation(-2.0F, 3.0F, 5.8F)
        );
        root.addOrReplaceChild(
                "tube_1_1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F)
                        .mirror(),
                PartPose.rotation(-2.0F, 2.0F, 6.8F)
        );
        root.addOrReplaceChild(
                "tube_1_2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F)
                        .mirror(),
                PartPose.rotation(-2.0F, 1.0F, 6.8F)
        );
        root.addOrReplaceChild(
                "tube_1_3",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F)
                        .mirror(),
                PartPose.rotation(-2.0F, 0.0F, 6.8F)
        );
        root.addOrReplaceChild(
                "tube_1_4",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F)
                        .mirror(),
                PartPose.rotation(-2.0F, -1.0F, 6.8F)
        );
        root.addOrReplaceChild(
                "tube_1_5",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F)
                        .mirror(),
                PartPose.rotation(-2.0F, -2.0F, 5.8F)
        );
        root.addOrReplaceChild(
                "tube_1_6",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F)
                        .mirror(),
                PartPose.rotation(-2.0F, -3.0F, 4.8F)
        );

        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public void setupAnim(S renderState) {
        resetPose();

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                this.tubes[j][i].xRot = animationSupplier().apply(this.tubes[j][i].xRot);
                this.tubes[j][i].yRot = animationSupplier().apply(this.tubes[j][i].yRot);
            }
        }
    }
}
