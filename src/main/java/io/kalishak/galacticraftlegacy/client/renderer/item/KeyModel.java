package io.kalishak.galacticraftlegacy.client.renderer.item;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class KeyModel extends Model<FeatureTier> {
    public KeyModel(ModelPart root) {
        super(root, RenderTypes::entitySolid);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("bone", CubeListBuilder.create()
                .texOffs(1, 1).mirror().addBox(-3.0F, -9.0F, 7.5F, 3.0F, 3.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(1, 1).mirror().addBox(-4.0F, -8.5F, 7.5F, 1.0F, 2.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(1, 1).mirror().addBox(-15.0F, -8.0F, 7.5F, 11.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(1, 1).addBox(-14.5F, -7.0F, 7.5F, 4.0F, 1.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(1, 1).addBox(-14.5F, -6.0F, 7.5F, 3.0F, 1.0F, 1.0F, CubeDeformation.NONE), PartPose.offset(8.0F, 24.0F, -8.0F));

        return LayerDefinition.create(mesh, 16, 16);
    }

    public static Identifier getTexture(FeatureTier tier) {
        return Constants.texture("entity/" + tier.getCelestialBodyName() + "_dungeon_key.png");
    }
}
