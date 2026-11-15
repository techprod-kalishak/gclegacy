package io.kalishak.galacticraftlegacy.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.client.model.entity.EvolvedCreeperModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.EvolvedCreeperPowerLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear.OxygenGearLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear.OxygenMaskLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear.OxygenTankLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.EvolvedCreeperRenderState;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenders;
import io.kalishak.galacticraftlegacy.world.entity.monster.EvolvedCreeper;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

@SuppressWarnings("deprecation")
public class EvolvedCreeperRenderer extends AgeableMobRenderer<EvolvedCreeper, EvolvedCreeperRenderState, EvolvedCreeperModel> {
    private static final Identifier CREEPER_LOCATION = Identifier.withDefaultNamespace("textures/entity/creeper/creeper.png");

    public EvolvedCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, new EvolvedCreeperModel(context.bakeLayer(GalacticraftModelLayers.EVOLVED_CREEPER)), new EvolvedCreeperModel(context.bakeLayer(GalacticraftModelLayers.EVOLVED_CREEPER)), 0.5F);
        addLayer(new EvolvedCreeperPowerLayer(this, context.getModelSet()));
        addLayer(new OxygenMaskLayer<>(this, context.getModelSet(), context.getEquipmentRenderer(), context.getEquipmentAssets()));
        addLayer(new OxygenGearLayer<>(this, context.getModelSet(), context.getEquipmentRenderer(), context.getEquipmentAssets()));
        addLayer(new OxygenTankLayer<>(this, context.getModelSet(), context.getEquipmentRenderer(), context.getEquipmentAssets()));
    }

    @Override
    public Identifier getTextureLocation(EvolvedCreeperRenderState state) {
        return CREEPER_LOCATION;
    }

    @Override
    public void extractRenderState(EvolvedCreeper entity, EvolvedCreeperRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.swelling = entity.getSwelling(partialTicks);
        state.isPowered = entity.isPowered();
        state.gearRenders = GearRenders.fixed();
    }

    @Override
    public EvolvedCreeperRenderState createRenderState() {
        return new EvolvedCreeperRenderState();
    }

    @Override
    protected void scale(EvolvedCreeperRenderState state, PoseStack poseStack) {
        float g = state.swelling;
        float wobble = 1.0F + Mth.sin(g * 100.0F) * g * 0.01F;
        g = Mth.clamp(g, 0.0F, 1.0F);
        g *= g;
        g *= g;
        float s = (1.0F + g * 0.4F) * wobble;
        float hs = (1.0F + g * 0.1F) / wobble;

        if (state.isBaby) {
            s *= 0.5F;
            hs *= 0.5F;
        }

        poseStack.scale(s, hs, s);
    }

    @Override
    protected float getWhiteOverlayProgress(EvolvedCreeperRenderState state) {
        float step = state.swelling;
        return (int)(step * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(step, 0.5F, 1.0F);
    }
}
