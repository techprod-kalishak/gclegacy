package io.kalishak.galacticraftlegacy.client.renderer.entity.layer;

import io.kalishak.galacticraftlegacy.client.renderer.entity.state.monster.EvolvedEndermanRenderState;
import net.minecraft.client.model.monster.enderman.EndermanModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class EvolvedEnderEyesLayer extends EyesLayer<EvolvedEndermanRenderState, EndermanModel<EvolvedEndermanRenderState>> {
    private static final RenderType ENDERMAN_EYES = RenderTypes.eyes(Identifier.withDefaultNamespace("textures/entity/enderman/enderman_eyes.png"));

    public EvolvedEnderEyesLayer(RenderLayerParent<EvolvedEndermanRenderState, EndermanModel<EvolvedEndermanRenderState>> renderer) {
        super(renderer);
    }

    @Override
    public RenderType renderType() {
        return ENDERMAN_EYES;
    }
}
