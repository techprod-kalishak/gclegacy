package io.kalishak.galacticraftlegacy.client.renderer.entity.layer;

import io.kalishak.galacticraftlegacy.client.model.entity.EvolvedSpiderModel;
import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class EvolvedSpiderEyesLayer extends EyesLayer<LivingEntityRenderState, EvolvedSpiderModel> {
    private static final RenderType SPIDER_EYES = RenderTypes.eyes(Constants.id("textures/entity/spider/evolved_spider_eyes.png"));

    public EvolvedSpiderEyesLayer(RenderLayerParent<LivingEntityRenderState, EvolvedSpiderModel> renderer) {
        super(renderer);
    }

    @Override
    public RenderType renderType() {
        return SPIDER_EYES;
    }
}
