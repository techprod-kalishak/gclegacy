package io.kalishak.galacticraftlegacy.client.renderer.entity.monster;

import io.kalishak.galacticraftlegacy.client.model.entity.EvolvedWitchModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.EvolvedWitchItemLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear.OxygenGearLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear.OxygenMaskLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear.OxygenTankLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.EvolvedWitchRenderState;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenders;
import io.kalishak.galacticraftlegacy.world.entity.monster.EvolvedWitch;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class EvolvedWitchRenderer extends MobRenderer<EvolvedWitch, EvolvedWitchRenderState, EvolvedWitchModel> {
    private static final Identifier WITCH_LOCATION = Identifier.withDefaultNamespace("textures/entity/witch/witch.png");

    public EvolvedWitchRenderer(EntityRendererProvider.Context context) {
        super(context, new EvolvedWitchModel(context.bakeLayer(GalacticraftModelLayers.EVOLVED_WITCH)), 0.5F);
        addLayer(new EvolvedWitchItemLayer(this));
        addLayer(new OxygenMaskLayer<>(this, context.getModelSet(), context.getEquipmentRenderer(), context.getEquipmentAssets(), 0.02F, 0.022F));
        addLayer(new OxygenGearLayer<>(this, context.getModelSet(), context.getEquipmentRenderer(), context.getEquipmentAssets(), 0.0F, 0.012F));
        addLayer(new OxygenTankLayer<>(this, context.getModelSet(), context.getEquipmentRenderer(), context.getEquipmentAssets(), 0.2F, 0.33F));
    }

    @Override
    public Identifier getTextureLocation(EvolvedWitchRenderState state) {
        return WITCH_LOCATION;
    }

    @Override
    public EvolvedWitchRenderState createRenderState() {
        return new EvolvedWitchRenderState();
    }

    @Override
    public void extractRenderState(EvolvedWitch entity, EvolvedWitchRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        HoldingEntityRenderState.extractHoldingEntityRenderState(entity, state, this.itemModelResolver);
        state.entityId = entity.getId();
        ItemStack mainHandItem = entity.getMainHandItem();
        state.isHoldingItem = !mainHandItem.isEmpty();
        state.isHoldingPotion = mainHandItem.is(Items.POTION);
        state.gearRenders = GearRenders.fixed();
    }
}
