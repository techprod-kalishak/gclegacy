/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.model.gear;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.object.equipment.ElytraModel;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import org.joml.Quaternionf;

public class OxygenTankModel<S extends LivingEntityRenderState> extends GearEquipmentModel<S> {
    public OxygenTankModel(ModelPart root) {
        super(root, RenderTypes::entitySolid);
    }

    public static LayerDefinition createHeavyTankLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild(
                "heavy_tank",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.0F, -7.0F, -2.0F, 3.0F, 7.0F, 3.0F),
                PartPose.offset(0.0F, 7.0F, 0.0F)
        );
        return LayerDefinition.create(meshdefinition, 16, 16);
    }
    public static LayerDefinition createMediumTankLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild(
                "medium_tank",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.0F, -7.0F, -2.0F, 3.0F, 6.0F, 3.0F),
                PartPose.offset(0.0F, 6.0F, 0.0F)
        );
        return LayerDefinition.create(meshdefinition, 16, 16);
    }
    public static LayerDefinition createLightTankLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild(
                "light_tank",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.0F, -7.0F, -2.0F, 3.0F, 5.0F, 3.0F),
                PartPose.offset(0.0F, 7.0F, 0.0F)
        );
        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(S renderState) {
        resetPose();
    }
}
