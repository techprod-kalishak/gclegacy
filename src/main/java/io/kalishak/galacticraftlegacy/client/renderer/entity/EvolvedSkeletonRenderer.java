/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.entity;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear.OxygenGearLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear.OxygenMaskLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear.OxygenTankLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.EvolvedSkeletonRenderState;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.entity.monster.EvolvedSkeleton;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractSkeletonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class EvolvedSkeletonRenderer extends AbstractSkeletonRenderer<EvolvedSkeleton, EvolvedSkeletonRenderState> {
    private static final Identifier SKELETON_LOCATION = Identifier.withDefaultNamespace("textures/entity/skeleton/skeleton.png");

    public EvolvedSkeletonRenderer(EntityRendererProvider.Context context) {
        super(context, ModelLayers.SKELETON, ModelLayers.SKELETON_ARMOR);
        addLayer(new OxygenMaskLayer<>(this, context.getModelSet(), context.getEquipmentRenderer(), context.getEquipmentAssets()));
        addLayer(new OxygenGearLayer<>(this, context.getModelSet(), context.getEquipmentRenderer(), context.getEquipmentAssets()));
        addLayer(new OxygenTankLayer<>(this, context.getModelSet(), context.getEquipmentRenderer(), context.getEquipmentAssets()));
    }

    @Override
    public Identifier getTextureLocation(EvolvedSkeletonRenderState state) {
        return SKELETON_LOCATION;
    }

    @Override
    public void extractRenderState(EvolvedSkeleton entity, EvolvedSkeletonRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        SpaceGearEquipment spaceGearEquipment = entity.getData(GalacticraftAttachments.ENTITY_GEAR_INVENTORY).getGearEquipment();
        state.oxygenMask = spaceGearEquipment.get(GearEquipmentSlot.MASK);
        state.oxygenGear = spaceGearEquipment.get(GearEquipmentSlot.GEAR);
        state.tank = spaceGearEquipment.get(GearEquipmentSlot.TANK);
        state.additionalTank = spaceGearEquipment.get(GearEquipmentSlot.ADDITIONAL_TANK);
    }

    @Override
    public EvolvedSkeletonRenderState createRenderState() {
        return new EvolvedSkeletonRenderState();
    }
}
