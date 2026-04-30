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
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.EvolvedZombieRenderState;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.entity.monster.EvolvedZombie;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.zombie.BabyZombieModel;
import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class EvolvedZombieRenderer extends AbstractZombieRenderer<EvolvedZombie, EvolvedZombieRenderState, ZombieModel<EvolvedZombieRenderState>> {
    private static final Identifier ZOMBIE_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/zombie.png");

    public EvolvedZombieRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE)),
                new BabyZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE_BABY)),
                ArmorModelSet.bake(ModelLayers.ZOMBIE_ARMOR, context.getModelSet(), ZombieModel::new),
                ArmorModelSet.bake(ModelLayers.ZOMBIE_BABY_ARMOR, context.getModelSet(), BabyZombieModel::new)
        );
        addLayer(new OxygenMaskLayer<>(this, context.getModelSet(), context.getEquipmentRenderer(), context.getEquipmentAssets()));
        addLayer(new OxygenGearLayer<>(this, context.getModelSet(), context.getEquipmentRenderer(), context.getEquipmentAssets()));
        addLayer(new OxygenTankLayer<>(this, context.getModelSet(), context.getEquipmentRenderer(), context.getEquipmentAssets()));
    }

    @Override
    public Identifier getTextureLocation(EvolvedZombieRenderState state) {
        return ZOMBIE_LOCATION;
    }

    @Override
    public void extractRenderState(EvolvedZombie entity, EvolvedZombieRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        SpaceGearEquipment spaceGearEquipment = entity.getData(GalacticraftAttachments.ENTITY_GEAR_INVENTORY).getGearEquipment();
        state.oxygenMask = spaceGearEquipment.get(GearEquipmentSlot.MASK);
        state.oxygenGear = spaceGearEquipment.get(GearEquipmentSlot.GEAR);
        state.tank = spaceGearEquipment.get(GearEquipmentSlot.TANK);
        state.additionalTank = spaceGearEquipment.get(GearEquipmentSlot.ADDITIONAL_TANK);
    }

    @Override
    public EvolvedZombieRenderState createRenderState() {
        return new EvolvedZombieRenderState();
    }
}
