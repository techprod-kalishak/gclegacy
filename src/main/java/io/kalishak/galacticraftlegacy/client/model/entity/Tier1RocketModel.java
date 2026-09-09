/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.model.entity;

import io.kalishak.galacticraftlegacy.client.renderer.entity.state.RocketRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

@SuppressWarnings("FieldCanBeLocal")
public class Tier1RocketModel extends EntityModel<RocketRenderState> {
    private final ModelPart insideRoof;
    private final ModelPart rocketBase1;
    private final ModelPart rocketBase2;
    private final ModelPart tip;
    private final ModelPart wing4d;
    private final ModelPart wing4c;
    private final ModelPart wing4e;
    private final ModelPart wing4b;
    private final ModelPart wing4a;
    private final ModelPart wing1a;
    private final ModelPart wing1b;
    private final ModelPart wing1c;
    private final ModelPart wing1e;
    private final ModelPart wing1d;
    private final ModelPart wing2e;
    private final ModelPart wing2d;
    private final ModelPart wing2c;
    private final ModelPart wing2b;
    private final ModelPart wing2a;
    private final ModelPart wing3e;
    private final ModelPart wing3d;
    private final ModelPart wing3c;
    private final ModelPart wing3b;
    private final ModelPart wing3a;
    private final ModelPart top1;
    private final ModelPart top2;
    private final ModelPart top3;
    private final ModelPart top4;
    private final ModelPart top5;
    private final ModelPart top6;
    private final ModelPart top7;
    private final ModelPart insideBottom;
    private final ModelPart insideLeft;
    private final ModelPart insidetop;
    private final ModelPart rocketBase3;
    private final ModelPart insideRight;
    private final ModelPart insideSideLeft;
    private final ModelPart insideSideRight;
    private final ModelPart insideSideBack;
    private final ModelPart insideFloor;
    
    public Tier1RocketModel(ModelPart root) {
        super(root);
        this.insideRoof = root.getChild("insideRoof");
        this.rocketBase1 = root.getChild("rocketBase1");
        this.rocketBase2 = root.getChild("rocketBase2");
        this.tip = root.getChild("tip");
        this.wing4d = root.getChild("wing4d");
        this.wing4c = root.getChild("wing4c");
        this.wing4e = root.getChild("wing4e");
        this.wing4b = root.getChild("wing4b");
        this.wing4a = root.getChild("wing4a");
        this.wing1a = root.getChild("wing1a");
        this.wing1b = root.getChild("wing1b");
        this.wing1c = root.getChild("wing1c");
        this.wing1e = root.getChild("wing1e");
        this.wing1d = root.getChild("wing1d");
        this.wing2e = root.getChild("wing2e");
        this.wing2d = root.getChild("wing2d");
        this.wing2c = root.getChild("wing2c");
        this.wing2b = root.getChild("wing2b");
        this.wing2a = root.getChild("wing2a");
        this.wing3e = root.getChild("wing3e");
        this.wing3d = root.getChild("wing3d");
        this.wing3c = root.getChild("wing3c");
        this.wing3b = root.getChild("wing3b");
        this.wing3a = root.getChild("wing3a");
        this.top1 = root.getChild("top1");
        this.top2 = root.getChild("top2");
        this.top3 = root.getChild("top3");
        this.top4 = root.getChild("top4");
        this.top5 = root.getChild("top5");
        this.top6 = root.getChild("top6");
        this.top7 = root.getChild("top7");
        this.insideBottom = root.getChild("insideBottom");
        this.insideLeft = root.getChild("insideLeft");
        this.insidetop = root.getChild("insidetop");
        this.rocketBase3 = root.getChild("rocketBase3");
        this.insideRight = root.getChild("insideRight");
        this.insideSideLeft = root.getChild("insideSideLeft");
        this.insideSideRight = root.getChild("insideSideRight");
        this.insideSideBack = root.getChild("insideSideBack");
        this.insideFloor = root.getChild("insideFloor");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        root.addOrReplaceChild("insideRoof",
                CubeListBuilder.create().mirror().texOffs(0, 59)
                        .addBox(-9.0F, -45.0F, -9.0F, 18.0F, 1.0F, 18.0F),
                PartPose.offsetAndRotation(0F, 23F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("rocketBase1",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox(-7.0F, -1.0F, -7.0F, 14.0F, 1.0F, 14.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("rocketBase2",
                CubeListBuilder.create().mirror().texOffs(0, 15)
                        .addBox(-6.0F, -2.0F, -6.0F, 12.0F, 1.0F, 12.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("tip",
                CubeListBuilder.create().mirror().texOffs(248, 144)
                        .addBox(-1.0F, -76.0F, -1.0F, 2.0F, 18.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing4d",
                CubeListBuilder.create().mirror().texOffs(66, 0)
                        .addBox( 11.0F, -14.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing4c",
                CubeListBuilder.create().mirror().texOffs(66, 0)
                        .addBox( 13.0F, -12.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing4e",
                CubeListBuilder.create().mirror().texOffs(66, 0)
                        .addBox( 9.1F, -15.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing4b",
                CubeListBuilder.create().mirror().texOffs(66, 0)
                        .addBox( 15.0F, -9.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing4a",
                CubeListBuilder.create().mirror().texOffs(74, 0)
                        .addBox( 17.0F, -14.0F, -1.0F, 1.0F, 15.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing1a",
                CubeListBuilder.create().mirror().texOffs(60, 0)
                        .addBox(-1.0F, -14.0F, -18.0F, 2.0F, 15.0F, 1.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing1b",
                CubeListBuilder.create().mirror().texOffs(66, 0)
                        .addBox(-1.0F, -9.0F, -17.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing1c",
                CubeListBuilder.create().mirror().texOffs(66, 0)
                        .addBox(-1.0F, -12.0F, -15.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing1e",
                CubeListBuilder.create().mirror().texOffs(66, 0)
                        .addBox(-1.0F, -15.0F, -11.1F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing1d",
                CubeListBuilder.create().mirror().texOffs(66, 0)
                        .addBox(-1.0F, -14.0F, -13.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing2e",
                CubeListBuilder.create().mirror().texOffs(66, 0)
                        .addBox(-11.1F, -15.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing2d",
                CubeListBuilder.create().mirror().texOffs(66, 0)
                        .addBox(-13.0F, -14.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing2c",
                CubeListBuilder.create().mirror().texOffs(66, 0)
                        .addBox(-15.0F, -12.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing2b",
                CubeListBuilder.create().mirror().texOffs(66, 0)
                        .addBox(-17.0F, -9.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing2a",
                CubeListBuilder.create().mirror().texOffs(74, 0)
                        .addBox(-18.0F, -14.0F, -1.0F, 1.0F, 15.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing3e",
                CubeListBuilder.create().mirror().texOffs(66, 0)
                        .addBox(-1.0F, -15.0F,  9.1F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing3d",
                CubeListBuilder.create().mirror().texOffs(66, 0)
                        .addBox(-1.0F, -14.0F,  11.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing3c",
                CubeListBuilder.create().mirror().texOffs(66, 0)
                        .addBox(-1.0F, -12.0F,  13.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing3b",
                CubeListBuilder.create().mirror().texOffs(66, 0)
                        .addBox(-1.0F, -9.0F,  15.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("wing3a",
                CubeListBuilder.create().mirror().texOffs(60, 0)
                        .addBox(-1.0F, -14.0F,  17.0F, 2.0F, 15.0F, 1.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("top1",
                CubeListBuilder.create().mirror().texOffs(192, 60)
                        .addBox(-8.0F, -48.0F, -8.0F, 16.0F, 2.0F, 16.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("top2",
                CubeListBuilder.create().mirror().texOffs(200, 78)
                        .addBox(-7.0F, -50.0F, -7.0F, 14.0F, 2.0F, 14.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("top3",
                CubeListBuilder.create().mirror().texOffs(208, 94)
                        .addBox(-6.0F, -52.0F, -6.0F, 12.0F, 2.0F, 12.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("top4",
                CubeListBuilder.create().mirror().texOffs(216, 108)
                        .addBox(-5.0F, -54.0F, -5.0F, 10.0F, 2.0F, 10.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("top5",
                CubeListBuilder.create().mirror().texOffs(224, 120)
                        .addBox(-4.0F, -56.0F, -4.0F, 8.0F, 2.0F, 8.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("top6",
                CubeListBuilder.create().mirror().texOffs(232, 130)
                        .addBox(-3.0F, -58.0F, -3.0F, 6.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("top7",
                CubeListBuilder.create().mirror().texOffs(240, 138)
                        .addBox(-2.0F, -60.0F, -2.0F, 4.0F, 2.0F, 4.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("insideBottom",
                CubeListBuilder.create().mirror().texOffs(85, 18)
                        .addBox(-3.9F, -22.0F, -8.9F, 8.0F, 17.0F, 1.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("insideLeft",
                CubeListBuilder.create().mirror().texOffs(103, 0)
                        .addBox( 3.9F, -46.0F, -8.9F, 5.0F, 41.0F, 1.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("insidetop",
                CubeListBuilder.create().mirror().texOffs(85, 0)
                        .addBox(-3.9F, -46.0F, -8.9F, 8.0F, 17.0F, 1.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("rocketBase3",
                CubeListBuilder.create().mirror().texOffs(0, 28)
                        .addBox(-5.0F, -4.0F, -5.0F, 10.0F, 2.0F, 10.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("insideRight",
                CubeListBuilder.create().mirror().texOffs(103, 42)
                        .addBox(-8.9F, -46.0F, -8.9F, 5.0F, 41.0F, 1.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("insideSideLeft",
                CubeListBuilder.create().mirror().texOffs(119, 57)
                        .addBox( 8.1F, -46.0F, -7.9F, 1.0F, 41.0F, 17.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("insideSideRight",
                CubeListBuilder.create().mirror().texOffs(120, 0)
                        .addBox(-8.9F, -46.0F, -7.9F, 1.0F, 41.0F, 16.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("insideSideBack",
                CubeListBuilder.create().mirror().texOffs(120, 114)
                        .addBox(-8.9F, -46.0F,  8.1F, 17.0F, 41.0F, 1.0F),
                PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F)
        );

        root.addOrReplaceChild("insideFloor",
                CubeListBuilder.create().mirror().texOffs(0, 40)
                        .addBox(-9.0F, -4.0F, -9.0F, 18.0F, 1.0F, 18.0F),
                PartPose.offsetAndRotation(0F, 23F, 0F, 0F, 0F, 0F)
        );

        return LayerDefinition.create(meshDefinition, 256, 256);
    }
}
