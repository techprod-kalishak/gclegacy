/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.model.object;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class NasaWorkbenchModel extends Model<BlockEntityRenderState> {
    private final ModelPart base1a;
    private final ModelPart baseAux1;
    private final ModelPart base1b;
    private final ModelPart base2a;
    private final ModelPart base2b;
    private final ModelPart baseAux3;
    private final ModelPart baseAux2;
    private final ModelPart clawL1;
    private final ModelPart clawL2;
    private final ModelPart clawR1;
    private final ModelPart clawR2;
    private final ModelPart clawRPR;
    private final ModelPart clawRPL;
    private final ModelPart armB1;
    private final ModelPart manipulatorRotationPointB;
    private final ModelPart baseRotationPointB;
    private final ModelPart armRotationPointB;
    private final ModelPart armB2;
    private final ModelPart armR1;
    private final ModelPart armR2;
    private final ModelPart baseRotationPointR;
    private final ModelPart armRotationPointR;
    private final ModelPart weldHead;
    private final ModelPart weldBit;
    private final ModelPart screenRotationPointF;
    private final ModelPart armF1;
    private final ModelPart baseRotationPointF;
    private final ModelPart screen;
    private final ModelPart armRotationPointL;
    private final ModelPart armL2;
    private final ModelPart baseRotationPointL;
    private final ModelPart armL1;
    private final ModelPart sensorDish;
    private final ModelPart sensor;
    
    public NasaWorkbenchModel(ModelPart partDefinition) {
        super(partDefinition, RenderTypes::entityCutout);

        this.base1a = partDefinition.getChild("base1a");
        this.baseAux1 = partDefinition.getChild("baseAux1");
        this.base1b = partDefinition.getChild("base1b");
        this.base2a = partDefinition.getChild("base2a");
        this.base2b = partDefinition.getChild("base2b");
        this.baseAux3 = partDefinition.getChild("baseAux3");
        this.baseAux2 = partDefinition.getChild("baseAux2");
        this.clawL1 = partDefinition.getChild("clawL1");
        this.clawL2 = partDefinition.getChild("clawL2");
        this.clawR1 = partDefinition.getChild("clawR1");
        this.clawR2 = partDefinition.getChild("clawR2");
        this.clawRPR = partDefinition.getChild("clawRPR");
        this.clawRPL = partDefinition.getChild("clawRPL");
        this.armB1 = partDefinition.getChild("armB1");
        this.manipulatorRotationPointB = partDefinition.getChild("manipulatorRotationPointB");
        this.baseRotationPointB = partDefinition.getChild("baseRotationPointB");
        this.armRotationPointB = partDefinition.getChild("armRotationPointB");
        this.armB2 = partDefinition.getChild("armB2");
        this.armR1 = partDefinition.getChild("armR1");
        this.armR2 = partDefinition.getChild("armR2");
        this.baseRotationPointR = partDefinition.getChild("baseRotationPointR");
        this.armRotationPointR = partDefinition.getChild("armRotationPointR");
        this.weldHead = partDefinition.getChild("weldHead");
        this.weldBit = partDefinition.getChild("weldBit");
        this.screenRotationPointF = partDefinition.getChild("screenRotationPointF");
        this.armF1 = partDefinition.getChild("armF1");
        this.baseRotationPointF = partDefinition.getChild("baseRotationPointF");
        this.screen = partDefinition.getChild("screen");
        this.armRotationPointL = partDefinition.getChild("armRotationPointL");
        this.armL2 = partDefinition.getChild("armL2");
        this.baseRotationPointL = partDefinition.getChild("baseRotationPointL");
        this.armL1 = partDefinition.getChild("armL1");
        this.sensorDish = partDefinition.getChild("sensorDish");
        this.sensor = partDefinition.getChild("sensor");
    }

    public static LayerDefinition createArmLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        float halfPi = (float) Math.PI / 2;

        partDefinition.addOrReplaceChild(
                "base1a",
                CubeListBuilder.create().texOffs(113, 24)
                        .addBox(-2.5F, -2F, -7.5F, 5, 2, 15)
                        .mirror(),
                PartPose.offsetAndRotation(0, 24.0F, 0.0F, 0F, halfPi, 0F)
        );
        partDefinition.addOrReplaceChild(
                "baseAux1",
                CubeListBuilder.create().texOffs(35, 40)
                        .addBox(-3.5F, 0F, -3.5F, 7, 1, 7)
                        .mirror(),
                PartPose.offsetAndRotation(0, 20.0F, 0.0F, 0F, halfPi, 0F)
        );
        partDefinition.addOrReplaceChild(
                "base1b",
                CubeListBuilder.create().texOffs(113, 24)
                        .addBox(-2.5F, -2F, -7.5F, 5, 2, 15)
                        .mirror(),
                PartPose.rotation(0, 20.0F, 0.0F)
        );
        partDefinition.addOrReplaceChild(
                "base2a",
                CubeListBuilder.create().texOffs(65, 0)
                        .addBox(-3.5F, -2F, -7F, 7, 5, 14)
                        .mirror(),
                PartPose.offsetAndRotation(0, 23.0F, 0.0F, 0F, -halfPi, 0F)
        );
        partDefinition.addOrReplaceChild(
                "base2b",
                CubeListBuilder.create().texOffs(65, 0)
                        .addBox(-3.5F, -2F, -7F, 7, 3, 14)
                        .mirror(),
                PartPose.offsetAndRotation(0, 23.0F, 0.0F, 0F, halfPi, 0F)
        );
        partDefinition.addOrReplaceChild(
                "baseAux3",
                CubeListBuilder.create().texOffs(50, 62)
                        .addBox(-8.5F, 0F, -0.5F, 17, 4, 1)
                        .mirror(),
                PartPose.offsetAndRotation(0, 20.5F, 0.0F, 0F, -halfPi, 0F)
        );
        partDefinition.addOrReplaceChild(
                "baseAux2",
                CubeListBuilder.create().texOffs(50, 62)
                        .addBox(-8.5F, 0F, -0.5F, 17, 4, 1)
                        .mirror(),
                PartPose.offsetAndRotation(0, 20.5F, 0.0F, 0F, halfPi, 0F)
        );
        partDefinition.addOrReplaceChild(
                "clawL1",
                CubeListBuilder.create().texOffs(7, 57)
                        .addBox(-0.5F, -1F, -3F, 1, 2, 3)
                        .mirror(),
                PartPose.offsetAndRotation(-1F, -0.2F, 4F, 0F, 1.003826F, 0F)
        );
        partDefinition.addOrReplaceChild(
                "clawL2",
                CubeListBuilder.create().texOffs(7, 57)
                        .addBox(-2.7F, -1F, -5F, 1, 2, 3)
                        .mirror(),
                PartPose.offsetAndRotation(-1F, -0.2F, 4F, 0F, 0.1698892F, 0F)
        );
        partDefinition.addOrReplaceChild(
                "clawR1",
                CubeListBuilder.create().texOffs(7, 57)
                        .addBox(-0.5F, -1F, -3F, 1, 2, 3)
                        .mirror(),
                PartPose.offsetAndRotation(1F, -0.2F, 4F, 0F, -1.041005F, 0F)
        );
        partDefinition.addOrReplaceChild(
                "clawR2",
                CubeListBuilder.create().texOffs(7, 57)
                        .addBox(1.7F, -1F, -5F, 1, 2, 3)
                        .mirror(),
                PartPose.offsetAndRotation(1F, -0.2F, 4F, 0F, -0.1896157F, 0F)
        );
        partDefinition.addOrReplaceChild(
                "clawRPR",
                CubeListBuilder.create().texOffs(0, 45)
                        .addBox(-2.2F, -1.5F, -3F, 1, 3, 1)
                        .mirror(),
                PartPose.offsetAndRotation(1F, -0.2F, 4F, 0F, -1.63514F, 0F)
        );
        partDefinition.addOrReplaceChild(
                "clawRPL",
                CubeListBuilder.create().texOffs(0, 45)
                        .addBox(-2.2F, -1.5F, -3F, 1, 3, 1)
                        .mirror(),
                PartPose.offsetAndRotation(1F, -0.2F, 4F, 0F, 0.4096913F, 0F)
        );
        partDefinition.addOrReplaceChild(
                "armB1",
                CubeListBuilder.create().texOffs(46, 0)
                        .addBox(-1F, -12F, -1F, 2, 12, 2)
                        .mirror(),
                PartPose.offsetAndRotation(0F, 22F, 8F, 0F, -0.3005282F, 0F)
        );
        partDefinition.addOrReplaceChild(
                "manipulatorRotationPointB",
                CubeListBuilder.create().texOffs(0, 69)
                        .addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3)
                        .mirror(),
                PartPose.offsetAndRotation(0F, 0F, 4.5F, 0F, halfPi, 0F)
        );
        partDefinition.addOrReplaceChild(
                "baseRotationPointB",
                CubeListBuilder.create().texOffs(0, 69)
                        .addBox(-1.5F, -1.2F, -1.5F, 3, 3, 3)
                        .mirror(),
                PartPose.offsetAndRotation(0F, 22F, 7.5F, 0, 90 * Mth.DEG_TO_RAD, 15 * Mth.DEG_TO_RAD)
        );
        partDefinition.addOrReplaceChild(
                "armRotationPointB",
                CubeListBuilder.create().texOffs(0, 69)
                        .addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3)
                        .mirror(),
                PartPose.offsetAndRotation(0F, 10F, 11.5F, 90 * Mth.DEG_TO_RAD, 0, -90 * Mth.DEG_TO_RAD)
        );
        partDefinition.addOrReplaceChild(
                "armB2",
                CubeListBuilder.create().texOffs(46, 0)
                        .addBox(-1F, -12F, -1F, 2, 12, 2)
                        .mirror(),
                PartPose.offsetAndRotation(0F, 9F, 11F, 0.6289468F, 0F, 0F)
        );
        partDefinition.addOrReplaceChild(
                "armR1",
                CubeListBuilder.create().texOffs(46, 0)
                        .addBox(-1F, -12F, -1F, 2, 12, 2)
                        .mirror(),
                PartPose.offsetAndRotation(-8F, 22F, 0F, 0.6351428F, halfPi, 0F)
        );
        partDefinition.addOrReplaceChild(
                "armR2",
                CubeListBuilder.create().texOffs(55, 0)
                        .addBox(-1F, -8F, -1F, 2, 8, 2)
                        .mirror(),
                PartPose.offsetAndRotation(-15F, 11F, 0F, -0.9635439F, halfPi, 0F)
        );
        partDefinition.addOrReplaceChild(
                "baseRotationPointR",
                CubeListBuilder.create().texOffs(0, 69)
                        .addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3)
                        .mirror(),
                PartPose.offsetAndRotation(-7.9F, 22F, 0F, 0F, 0F, 0.5235988F)
        );
        partDefinition.addOrReplaceChild(
                "armRotationPointR",
                CubeListBuilder.create().texOffs(0, 69)
                        .addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3)
                        .mirror(),
                PartPose.offsetAndRotation(-15.76667F, 11.5F, 0F, 0F, 0F, -0.1745329F)
        );

        partDefinition.addOrReplaceChild("armRotationPointR",
                CubeListBuilder.create().mirror().texOffs(0, 69)
                        .addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-15.76667F, 11.5F, 0.0F, 0.0F, 0.0F, -0.1745329F)
        );

        partDefinition.addOrReplaceChild("weldHead",
                CubeListBuilder.create().mirror().texOffs(17, 0)
                        .addBox(-2.0F, -1.5F, -1.5F, 5.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-9.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.5948578F)
        );

        partDefinition.addOrReplaceChild("weldBit",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox(0.0F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-9.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.5948606F)
        );

        partDefinition.addOrReplaceChild("screenRotationPointF",
                CubeListBuilder.create().mirror().texOffs(0, 77)
                        .addBox(-1.5F, -1.5F, -1.5F, 3.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, -9.0F, 30.0F * Mth.DEG_TO_RAD, 0.0F, 0.0F)
        );

        partDefinition.addOrReplaceChild("armF1",
                CubeListBuilder.create().mirror().texOffs(55, 0)
                        .addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, -7.5F, 0.2602503F, 0.0F, 0.0F)
        );

        partDefinition.addOrReplaceChild("baseRotationPointF",
                CubeListBuilder.create().mirror().texOffs(0, 69)
                        .addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 21.6F, -6.8F, 30.0F * Mth.DEG_TO_RAD, 0.0F, 0.0F)
        );

        partDefinition.addOrReplaceChild("screen",
                CubeListBuilder.create().mirror().texOffs(10, 45)
                        .addBox(-3.5F, -2.5F, -1.0F, 7.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 13.0F, -10.0F, -1.047198F, 0.0F, 0.0F)
        );

        partDefinition.addOrReplaceChild("armRotationPointL",
                CubeListBuilder.create().mirror().texOffs(0, 69)
                        .addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(13.8F, 18.4F, 0.0F, 0.0F, -3.141593F, 0.5235988F)
        );

        partDefinition.addOrReplaceChild("armL2",
                CubeListBuilder.create().mirror().texOffs(55, 0)
                        .addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(13.7F, 18.0F, 0.0F, 0.6351428F, halfPi, 0.0F)
        );

        partDefinition.addOrReplaceChild("baseRotationPointL",
                CubeListBuilder.create().mirror().texOffs(0, 69)
                        .addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(7.5F, 22.0F, 0.0F, 0.0F, -3.141593F, 0.5235988F)
        );

        partDefinition.addOrReplaceChild("armL1",
                CubeListBuilder.create().mirror().texOffs(55, 0)
                        .addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(8.0F, 22.0F, 0.0F, -1.037895F, halfPi, 0.0F)
        );

        partDefinition.addOrReplaceChild("sensorDish",
                CubeListBuilder.create().mirror().texOffs(68, 41)
                        .addBox(-1.0F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(6.0F, 12.0F, 0.0F, 0.0F, 0.0F, -0.3005282F)
        );

        partDefinition.addOrReplaceChild("sensor",
                CubeListBuilder.create().mirror().texOffs(60, 54)
                        .addBox(-3.0F, -2.0F, -1.0F, 5.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(9.0F, 12.0F, 0.0F, 0.0F, 0.0F, -0.3005353F)
        );

        return LayerDefinition.create(meshDefinition, 256, 128);
    }
}
