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
    private final ModelPart fRoot;
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
    
    public NasaWorkbenchModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
        this.fRoot = root.getChild("f_root");

        this.base1a = this.fRoot.getChild("base1a");
        this.baseAux1 = this.fRoot.getChild("baseAux1");
        this.base1b = this.fRoot.getChild("base1b");
        this.base2a = this.fRoot.getChild("base2a");
        this.base2b = this.fRoot.getChild("base2b");
        this.baseAux3 = this.fRoot.getChild("baseAux3");
        this.baseAux2 = this.fRoot.getChild("baseAux2");
        this.clawL1 = this.fRoot.getChild("clawL1");
        this.clawL2 = this.fRoot.getChild("clawL2");
        this.clawR1 = this.fRoot.getChild("clawR1");
        this.clawR2 = this.fRoot.getChild("clawR2");
        this.clawRPR = this.fRoot.getChild("clawRPR");
        this.clawRPL = this.fRoot.getChild("clawRPL");
        this.armB1 = this.fRoot.getChild("armB1");
        this.manipulatorRotationPointB = this.fRoot.getChild("manipulatorRotationPointB");
        this.baseRotationPointB = this.fRoot.getChild("baseRotationPointB");
        this.armRotationPointB = this.fRoot.getChild("armRotationPointB");
        this.armB2 = this.fRoot.getChild("armB2");
        this.armR1 = this.fRoot.getChild("armR1");
        this.armR2 = this.fRoot.getChild("armR2");
        this.baseRotationPointR = this.fRoot.getChild("baseRotationPointR");
        this.armRotationPointR = this.fRoot.getChild("armRotationPointR");
        this.weldHead = this.fRoot.getChild("weldHead");
        this.weldBit = this.fRoot.getChild("weldBit");
        this.screenRotationPointF = this.fRoot.getChild("screenRotationPointF");
        this.armF1 = this.fRoot.getChild("armF1");
        this.baseRotationPointF = this.fRoot.getChild("baseRotationPointF");
        this.screen = this.fRoot.getChild("screen");
        this.armRotationPointL = this.fRoot.getChild("armRotationPointL");
        this.armL2 = this.fRoot.getChild("armL2");
        this.baseRotationPointL = this.fRoot.getChild("baseRotationPointL");
        this.armL1 = this.fRoot.getChild("armL1");
        this.sensorDish = this.fRoot.getChild("sensorDish");
        this.sensor = this.fRoot.getChild("sensor");
    }

    public static LayerDefinition createArmLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition root = partDefinition.addOrReplaceChild("f_root", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, (float) Math.PI, 0.0F, 0.0F));

        float halfPi = (float) Math.PI / 2;

        root.addOrReplaceChild(
                "base1a",
                CubeListBuilder.create().texOffs(113, 24)
                        .addBox(-2.5F, -2F, -7.5F, 5, 2, 15)
                        .mirror(),
                PartPose.offsetAndRotation(0, 24.0F, 0.0F, 0F, halfPi, 0F)
        );
        root.addOrReplaceChild(
                "baseAux1",
                CubeListBuilder.create().texOffs(35, 40)
                        .addBox(-3.5F, 0F, -3.5F, 7, 1, 7)
                        .mirror(),
                PartPose.offsetAndRotation(0, 20.0F, 0.0F, 0F, halfPi, 0F)
        );
        root.addOrReplaceChild(
                "base1b",
                CubeListBuilder.create().texOffs(113, 24)
                        .addBox(-2.5F, -2F, -7.5F, 5, 2, 15)
                        .mirror(),
                PartPose.rotation(0, 20.0F, 0.0F)
        );
        root.addOrReplaceChild(
                "base2a",
                CubeListBuilder.create().texOffs(65, 0)
                        .addBox(-3.5F, -2F, -7F, 7, 5, 14)
                        .mirror(),
                PartPose.offsetAndRotation(0, 23.0F, 0.0F, 0F, -halfPi, 0F)
        );
        root.addOrReplaceChild(
                "base2b",
                CubeListBuilder.create().texOffs(65, 0)
                        .addBox(-3.5F, -2F, -7F, 7, 3, 14)
                        .mirror(),
                PartPose.offsetAndRotation(0, 23.0F, 0.0F, 0F, halfPi, 0F)
        );
        root.addOrReplaceChild(
                "baseAux3",
                CubeListBuilder.create().texOffs(50, 62)
                        .addBox(-8.5F, 0F, -0.5F, 17, 4, 1)
                        .mirror(),
                PartPose.offsetAndRotation(0, 20.5F, 0.0F, 0F, -halfPi, 0F)
        );
        root.addOrReplaceChild(
                "baseAux2",
                CubeListBuilder.create().texOffs(50, 62)
                        .addBox(-8.5F, 0F, -0.5F, 17, 4, 1)
                        .mirror(),
                PartPose.offsetAndRotation(0, 20.5F, 0.0F, 0F, halfPi, 0F)
        );
        root.addOrReplaceChild(
                "clawL1",
                CubeListBuilder.create().texOffs(7, 57)
                        .addBox(-0.5F, -1F, -3F, 1, 2, 3)
                        .mirror(),
                PartPose.offsetAndRotation(-1F, -0.2F, 4F, 0F, 1.003826F, 0F)
        );
        root.addOrReplaceChild(
                "clawL2",
                CubeListBuilder.create().texOffs(7, 57)
                        .addBox(-2.7F, -1F, -5F, 1, 2, 3)
                        .mirror(),
                PartPose.offsetAndRotation(-1F, -0.2F, 4F, 0F, 0.1698892F, 0F)
        );
        root.addOrReplaceChild(
                "clawR1",
                CubeListBuilder.create().texOffs(7, 57)
                        .addBox(-0.5F, -1F, -3F, 1, 2, 3)
                        .mirror(),
                PartPose.offsetAndRotation(1F, -0.2F, 4F, 0F, -1.041005F, 0F)
        );
        root.addOrReplaceChild(
                "clawR2",
                CubeListBuilder.create().texOffs(7, 57)
                        .addBox(1.7F, -1F, -5F, 1, 2, 3)
                        .mirror(),
                PartPose.offsetAndRotation(1F, -0.2F, 4F, 0F, -0.1896157F, 0F)
        );
        root.addOrReplaceChild(
                "clawRPR",
                CubeListBuilder.create().texOffs(0, 45)
                        .addBox(-2.2F, -1.5F, -3F, 1, 3, 1)
                        .mirror(),
                PartPose.offsetAndRotation(1F, -0.2F, 4F, 0F, -1.63514F, 0F)
        );
        root.addOrReplaceChild(
                "clawRPL",
                CubeListBuilder.create().texOffs(0, 45)
                        .addBox(-2.2F, -1.5F, -3F, 1, 3, 1)
                        .mirror(),
                PartPose.offsetAndRotation(1F, -0.2F, 4F, 0F, 0.4096913F, 0F)
        );
        root.addOrReplaceChild(
                "armB1",
                CubeListBuilder.create().texOffs(46, 0)
                        .addBox(-1F, -12F, -1F, 2, 12, 2)
                        .mirror(),
                PartPose.offsetAndRotation(0F, 22F, 8F, 0F, -0.3005282F, 0F)
        );
        root.addOrReplaceChild(
                "manipulatorRotationPointB",
                CubeListBuilder.create().texOffs(0, 69)
                        .addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3)
                        .mirror(),
                PartPose.offsetAndRotation(0F, 0F, 4.5F, 0F, halfPi, 0F)
        );
        root.addOrReplaceChild(
                "baseRotationPointB",
                CubeListBuilder.create().texOffs(0, 69)
                        .addBox(-1.5F, -1.2F, -1.5F, 3, 3, 3)
                        .mirror(),
                PartPose.offsetAndRotation(0F, 22F, 7.5F, 0, 90 * Mth.DEG_TO_RAD, 15 * Mth.DEG_TO_RAD)
        );
        root.addOrReplaceChild(
                "armRotationPointB",
                CubeListBuilder.create().texOffs(0, 69)
                        .addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3)
                        .mirror(),
                PartPose.offsetAndRotation(0F, 10F, 11.5F, 90 * Mth.DEG_TO_RAD, 0, -90 * Mth.DEG_TO_RAD)
        );
        root.addOrReplaceChild(
                "armB2",
                CubeListBuilder.create().texOffs(46, 0)
                        .addBox(-1F, -12F, -1F, 2, 12, 2)
                        .mirror(),
                PartPose.offsetAndRotation(0F, 9F, 11F, 0.6289468F, 0F, 0F)
        );
        root.addOrReplaceChild(
                "armR1",
                CubeListBuilder.create().texOffs(46, 0)
                        .addBox(-1F, -12F, -1F, 2, 12, 2)
                        .mirror(),
                PartPose.offsetAndRotation(-8F, 22F, 0F, 0.6351428F, halfPi, 0F)
        );
        root.addOrReplaceChild(
                "armR2",
                CubeListBuilder.create().texOffs(55, 0)
                        .addBox(-1F, -8F, -1F, 2, 8, 2)
                        .mirror(),
                PartPose.offsetAndRotation(-15F, 11F, 0F, -0.9635439F, halfPi, 0F)
        );
        root.addOrReplaceChild(
                "baseRotationPointR",
                CubeListBuilder.create().texOffs(0, 69)
                        .addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3)
                        .mirror(),
                PartPose.offsetAndRotation(-7.9F, 22F, 0F, 0F, 0F, 0.5235988F)
        );
        root.addOrReplaceChild(
                "armRotationPointR",
                CubeListBuilder.create().texOffs(0, 69)
                        .addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3)
                        .mirror(),
                PartPose.offsetAndRotation(-15.76667F, 11.5F, 0F, 0F, 0F, -0.1745329F)
        );

        root.addOrReplaceChild("armRotationPointR",
                CubeListBuilder.create().mirror().texOffs(0, 69)
                        .addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-15.76667F, 11.5F, 0.0F, 0.0F, 0.0F, -0.1745329F)
        );

        root.addOrReplaceChild("weldHead",
                CubeListBuilder.create().mirror().texOffs(17, 0)
                        .addBox(-2.0F, -1.5F, -1.5F, 5.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(-9.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.5948578F)
        );

        root.addOrReplaceChild("weldBit",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox(0.0F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-9.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.5948606F)
        );

        root.addOrReplaceChild("screenRotationPointF",
                CubeListBuilder.create().mirror().texOffs(0, 77)
                        .addBox(-1.5F, -1.5F, -1.5F, 3.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, -9.0F, 30.0F * Mth.DEG_TO_RAD, 0.0F, 0.0F)
        );

        root.addOrReplaceChild("armF1",
                CubeListBuilder.create().mirror().texOffs(55, 0)
                        .addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, -7.5F, 0.2602503F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild("baseRotationPointF",
                CubeListBuilder.create().mirror().texOffs(0, 69)
                        .addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 21.6F, -6.8F, 30.0F * Mth.DEG_TO_RAD, 0.0F, 0.0F)
        );

        root.addOrReplaceChild("screen",
                CubeListBuilder.create().mirror().texOffs(10, 45)
                        .addBox(-3.5F, -2.5F, -1.0F, 7.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 13.0F, -10.0F, -1.047198F, 0.0F, 0.0F)
        );

        root.addOrReplaceChild("armRotationPointL",
                CubeListBuilder.create().mirror().texOffs(0, 69)
                        .addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(13.8F, 18.4F, 0.0F, 0.0F, -3.141593F, 0.5235988F)
        );

        root.addOrReplaceChild("armL2",
                CubeListBuilder.create().mirror().texOffs(55, 0)
                        .addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(13.7F, 18.0F, 0.0F, 0.6351428F, halfPi, 0.0F)
        );

        root.addOrReplaceChild("baseRotationPointL",
                CubeListBuilder.create().mirror().texOffs(0, 69)
                        .addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F),
                PartPose.offsetAndRotation(7.5F, 22.0F, 0.0F, 0.0F, -3.141593F, 0.5235988F)
        );

        root.addOrReplaceChild("armL1",
                CubeListBuilder.create().mirror().texOffs(55, 0)
                        .addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(8.0F, 22.0F, 0.0F, -1.037895F, halfPi, 0.0F)
        );

        root.addOrReplaceChild("sensorDish",
                CubeListBuilder.create().mirror().texOffs(68, 41)
                        .addBox(-1.0F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(6.0F, 12.0F, 0.0F, 0.0F, 0.0F, -0.3005282F)
        );

        root.addOrReplaceChild("sensor",
                CubeListBuilder.create().mirror().texOffs(60, 54)
                        .addBox(-3.0F, -2.0F, -1.0F, 5.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(9.0F, 12.0F, 0.0F, 0.0F, 0.0F, -0.3005353F)
        );

        return LayerDefinition.create(meshDefinition, 256, 128);
    }
}
