package io.kalishak.galacticraftlegacy.client.renderer.entity.monster;

import io.kalishak.galacticraftlegacy.client.model.entity.EvolvedSpiderModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.EvolvedSpiderEyesLayer;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.entity.monster.EvolvedSpider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class EvolvedSpiderRenderer extends MobRenderer<EvolvedSpider, LivingEntityRenderState, EvolvedSpiderModel> {
    private static final Identifier SPIDER_LOCATION = Constants.id("textures/entity/spider/evolved_spider.png");

    public EvolvedSpiderRenderer(EntityRendererProvider.Context context) {
        super(context, new EvolvedSpiderModel(context.bakeLayer(GalacticraftModelLayers.EVOLVED_SPIDER)), 0.8F);
        addLayer(new EvolvedSpiderEyesLayer(this));
    }

    @Override
    protected float getFlipDegrees() {
        return 180.0F;
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return SPIDER_LOCATION;
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }
}
