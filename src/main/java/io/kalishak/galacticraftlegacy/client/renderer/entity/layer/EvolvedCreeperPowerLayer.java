package io.kalishak.galacticraftlegacy.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.client.model.entity.EvolvedCreeperModel;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.monster.EvolvedCreeperRenderState;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.creeper.CreeperModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class EvolvedCreeperPowerLayer extends RenderLayer<EvolvedCreeperRenderState, EvolvedCreeperModel> {
    private static final Identifier POWER_LOCATION = Identifier.withDefaultNamespace("textures/entity/creeper/creeper_armor.png");
    private final CreeperModel model;

    public EvolvedCreeperPowerLayer(RenderLayerParent<EvolvedCreeperRenderState, EvolvedCreeperModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new CreeperModel(modelSet.bakeLayer(ModelLayers.CREEPER_ARMOR));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, EvolvedCreeperRenderState state, float yRot, float xRot) {
        if (isPowered(state)) {
            float t = state.ageInTicks;
            submitNodeCollector.order(1)
                    .submitModel(
                            this.model,
                            state,
                            poseStack,
                            RenderTypes.energySwirl(POWER_LOCATION, (state.ageInTicks * 0.01F) % 1.0F, t * 0.01F % 1.0F),
                            lightCoords,
                            OverlayTexture.NO_OVERLAY,
                            -8355712,
                            null,
                            state.outlineColor
                    );
        }
    }

    protected boolean isPowered(EvolvedCreeperRenderState state) {
        return !state.isBaby && state.isPowered;
    }
}
