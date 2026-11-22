/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.model.item;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class KeyModel extends Model<FeatureTier> {
    public KeyModel(ModelPart root) {
        super(root, RenderTypes::entitySolid);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("key", CubeListBuilder.create()
                .mirror().addBox(-4.5F, -1.0F, -0.5F, 3.0F, 3.0F, 1.0F)
                .addBox(-1.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F)
                .addBox(-0.5F,  0.0F, -0.5F, 11.0F, 1.0F, 1.0F)
                .addBox(6.0F,  1.0F, -0.5F, 4.0F, 1.0F, 1.0F)
                .addBox( 7.0F,  2.0F, -0.5F, 3.0F, 1.0F, 1.0F), PartPose.ZERO
        );

        return LayerDefinition.create(mesh, 16, 16);
    }

    public static Identifier getTexture(@Nullable FeatureTier tier) {
        String id = tier == null ? "moon" : tier.getCelestialBodyName();

        return Constants.texture("entity/dungeon_key/" + id + ".png");
    }
}
