/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.model.block;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;

public class SolarPanelModel extends Model<Float> {
    private final ModelPart panel;
    private final ModelPart vertical0;
    private final ModelPart vertical1;
    private final ModelPart vertical2;
    private final ModelPart horizontal0;
    private final ModelPart horizontal1;
    private final ModelPart horizontal2;
    private final ModelPart horizontal3;

    public SolarPanelModel(ModelPart root) {
        super(root, RenderTypes::entitySolid);
        this.panel = root.getChild("panel");
        this.vertical0 = root.getChild("vertical0");
        this.vertical1 = root.getChild("vertical1");
        this.vertical2 = root.getChild("vertical2");
        this.horizontal0 = root.getChild("horizontal0");
        this.horizontal1 = root.getChild("horizontal1");
        this.horizontal2 = root.getChild("horizontal2");
        this.horizontal3 = root.getChild("horizontal3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("panel",
                CubeListBuilder.create().mirror().texOffs(0, 0)
                        .addBox(-23.0F, -0.5F, -23.0F, 46.0F, 1.0F, 46.0F),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("horizontal0",
                CubeListBuilder.create().mirror().texOffs(0, 48)
                        .addBox(-24.0F, -1.1F, -23.0F, 1.0F, 1.0F, 46.0F),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("vertical0",
                CubeListBuilder.create().mirror().texOffs(94, 48)
                        .addBox(-24.0F, -1.1F,  23.0F, 48.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("vertical2",
                CubeListBuilder.create().mirror().texOffs(94, 48)
                        .addBox(-24.0F, -1.1F, -24.0F, 48.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("vertical1",
                CubeListBuilder.create().mirror().texOffs(94, 48)
                        .addBox(-24.0F, -1.1F, -0.5F, 48.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("horizontal1",
                CubeListBuilder.create().mirror().texOffs(0, 48)
                        .addBox(-9.0F, -1.1F, -23.0F, 1.0F, 1.0F, 46.0F),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("horizontal3",
                CubeListBuilder.create().mirror().texOffs(0, 48)
                        .addBox( 23.0F, -1.1F, -23.0F, 1.0F, 1.0F, 46.0F),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("horizontal2",
                CubeListBuilder.create().mirror().texOffs(0, 48)
                        .addBox( 8.0F, -1.1F, -23.0F, 1.0F, 1.0F, 46.0F),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F)
        );

        partdefinition.addOrReplaceChild("pole",
                CubeListBuilder.create().mirror().texOffs(94, 50)
                        .addBox(-1.5F,  0.0F, -1.5F, 3.0F, 24.0F, 3.0F),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F)
        );

        return LayerDefinition.create(meshdefinition, 256, 128);
    }

    @Override
    public void setupAnim(Float state) {
        super.setupAnim(state);
        float rot = -Mth.sin(state / Math.PI);
        this.panel.zRot = rot;
        this.vertical0.zRot = rot;
        this.vertical1.zRot = rot;
        this.vertical2.zRot = rot;
        this.horizontal0.zRot = rot;
        this.horizontal1.zRot = rot;
        this.horizontal2.zRot = rot;
        this.horizontal3.zRot = rot;
    }
}
