package io.kalishak.galacticraftlegacy.client.model.block;

import com.mojang.math.Axis;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.blockentity.state.EmergencyPostRenderState;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.Direction;

import java.util.function.Function;

public class EmergencyPostModel {
    public static final float MASK_SCALE = 3.0F;
    public final Flap northFlap;
    public final Flap eastFlap;
    public final Flap southFlap;
    public final Flap westFlap;
    public final Platform platform;
    public final Mask mask;
    public final Tank leftTank;
    public final Tank rightTank;
    public final Pack kit;

    public EmergencyPostModel(Function<ModelLayerLocation, ModelPart> partGetter) {
        ModelPart flap = partGetter.apply(GalacticraftModelLayers.EMERGENCY_POST.get(ModelType.FLAP));
        ModelPart tank = partGetter.apply(GalacticraftModelLayers.EMERGENCY_POST.get(ModelType.TANK));

        this.northFlap = new Flap(flap, Direction.NORTH);
        this.eastFlap = new Flap(flap, Direction.EAST);
        this.southFlap = new Flap(flap, Direction.SOUTH);
        this.westFlap = new Flap(flap, Direction.WEST);
        this.platform = new Platform(partGetter.apply(GalacticraftModelLayers.EMERGENCY_POST.get(ModelType.PLATFORM)));
        this.mask = new Mask(partGetter.apply(GalacticraftModelLayers.EMERGENCY_POST.get(ModelType.MASK)));
        this.leftTank = new Tank(tank);
        this.rightTank = new Tank(tank);
        this.kit = new Pack(partGetter.apply(GalacticraftModelLayers.EMERGENCY_POST.get(ModelType.KIT)));
    }

    public static LayerDefinition createEmergencyPostLayerFor(ModelType modelType) {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        CubeListBuilder builder = CubeListBuilder.create();
        PartPose partPose = PartPose.ZERO;

        switch (modelType) {
            case FLAP -> {
                root.addOrReplaceChild(
                        "down",
                        CubeListBuilder.create()
                                .addBox(-15.0F, -8.0F, 0.0F, 14.0F, 6.0F, 1.0F),
                        PartPose.ZERO
                );
                root.addOrReplaceChild(
                        "up",
                        CubeListBuilder.create()
                                .addBox(-15.0F, -8.0F, 0.0F, 14.0F, 6.0F, 1.0F),
                        PartPose.offset(0.0F, 12.0F, 0.0F)
                );

                return LayerDefinition.create(meshDefinition, modelType.xSize, modelType.ySize);
            }

            case PLATFORM -> {
                builder = builder
                        .addBox(-6.0F, -7.0F, 0.0F, 12.0F, 1.0F, 12.0F);
            }

            case MASK -> {
                builder = builder
                        .addBox(-8.0F, -4.0F, -8.0F, 16.0F, 16.0F, 16.0F);
            }

            case TANK -> {
                builder = builder
                        .texOffs(4, 0)
                        .addBox(-1.5F, 0.0F, -1.5F, 3.0F, 7.0F, 3.0F);
            }

            case KIT -> {
                builder = builder
                        .texOffs(50, 50)
                        .addBox(-6.0F, -11.0F, -10.0F, 12.0F, 1.0F, 20.0F);
            }

            case null -> throw new IllegalArgumentException("ModelType cannot be null!");
        }

        root.addOrReplaceChild(modelType.assetId, builder, partPose);

        return LayerDefinition.create(meshDefinition, modelType.xSize, modelType.ySize);
    }

    public void setVisibility(boolean isOpen) {
        this.platform.platform.visible = isOpen;
        this.mask.mask.visible = isOpen;
        this.leftTank.tank.visible = isOpen;
        this.rightTank.tank.visible = isOpen;
        this.kit.kit.visible = isOpen;
    }

    public static class Flap extends Model<EmergencyPostRenderState> {
        public final ModelPart down;
        public final ModelPart up;
        public final Direction side;

        public Flap(ModelPart root, Direction side) {
            super(root, RenderTypes::entityCutout);
            this.down = root.getChild("down");
            this.up = root.getChild("up");
            this.side = side;
        }

        @Override
        public void setupAnim(EmergencyPostRenderState state) {
            super.setupAnim(state);
            this.down.rotateBy(Axis.XN.rotationDegrees(state.getAngle(this.side)));
            this.up.rotateBy(Axis.XP.rotationDegrees(state.getAngle(this.side)));
        }
    }

    public static class Platform extends Model<EmergencyPostRenderState> {
        public final ModelPart platform;

        public Platform(ModelPart root) {
            super(root, RenderTypes::entityCutout);
            this.platform = root.getChild("platform");
        }
    }

    public static class Mask extends Model<EmergencyPostRenderState> {
        public final ModelPart mask;

        public Mask(ModelPart root) {
            super(root, RenderTypes::entityCutout);
            this.mask = root.getChild("mask");
        }
    }

    public static class Tank extends Model<EmergencyPostRenderState> {
        public final ModelPart tank;

        public Tank(ModelPart root) {
            super(root, RenderTypes::entityCutout);
            this.tank = root.getChild("tank");
        }
    }

    public static class Pack extends Model<EmergencyPostRenderState> {
        public final ModelPart kit;

        public Pack(ModelPart root) {
            super(root, RenderTypes::entityCutout);
            this.kit = root.getChild("kit");
        }
    }

    public enum ModelType {
        FLAP("flap", 32, 32),
        PLATFORM("platform", 16, 16),
        MASK("mask", 128, 64),
        TANK("tank", 128, 64),
        KIT("kit", 256, 256);

        final String assetId;
        final int xSize;
        final int ySize;

        ModelType(String assetId, int xSize, int ySize) {
            this.assetId = assetId;
            this.xSize = xSize;
            this.ySize = ySize;
        }

        @Override
        public String toString() {
            return this.assetId;
        }
    }
}
