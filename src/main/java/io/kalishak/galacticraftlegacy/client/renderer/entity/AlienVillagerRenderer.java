package io.kalishak.galacticraftlegacy.client.renderer.entity;

import io.kalishak.galacticraftlegacy.client.model.entity.AlienVillagerModel;
import io.kalishak.galacticraftlegacy.client.model.entity.BabyAlienVillagerModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.AlienVillagerRenderState;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.entity.npc.villager.AlienVillager;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
import net.minecraft.resources.Identifier;

@SuppressWarnings("deprecation")
public class AlienVillagerRenderer extends AgeableMobRenderer<AlienVillager, AlienVillagerRenderState, AlienVillagerModel> {
    private static final Identifier BASE_LOCATION = Constants.id("textures/entity/alien_villager/alien_villager.png");
    private static final Identifier BABY_LOCATION = Constants.id("textures/entity/alien_villager/alien_villager_baby.png");
    public static final CustomHeadLayer.Transforms CUSTOM_HEAD_TRANSFORMS = new CustomHeadLayer.Transforms(-0.1171875F, -0.07421875F, 1.0F);

    public AlienVillagerRenderer(EntityRendererProvider.Context context) {
        super(context, new AlienVillagerModel(context.bakeLayer(GalacticraftModelLayers.ALIEN_VILLAGER)), new BabyAlienVillagerModel(context.bakeLayer(GalacticraftModelLayers.ALIEN_VILLAGER_BABY)), 0.5F);
        addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getPlayerSkinRenderCache(), CUSTOM_HEAD_TRANSFORMS));
//        addLayer(new VillagerProfessionLayer<>(
//                this,
//                context.getResourceManager(),
//                "alien_villager",
//                new AlienVillagerModel(context.bakeLayer(GalacticraftModelLayers.ALIEN_VILLAGER_NO_HAT)),
//                new BabyAlienVillagerModel(context.bakeLayer(GalacticraftModelLayers.ALIEN_VILLAGER_BABY_NO_HAT))
//        ));
    }

    @Override
    public Identifier getTextureLocation(AlienVillagerRenderState state) {
        return state.isBaby ? BABY_LOCATION : BASE_LOCATION;
    }

    @Override
    protected float getShadowRadius(AlienVillagerRenderState state) {
        float r = super.getShadowRadius(state);

        return state.isBaby ? r * 0.5F : r;
    }

    @Override
    public AlienVillagerRenderState createRenderState() {
        return new AlienVillagerRenderState();
    }

    @Override
    public void extractRenderState(AlienVillager entity, AlienVillagerRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        HoldingEntityRenderState.extractHoldingEntityRenderState(entity, state, this.itemModelResolver);
        state.isUnhappy = entity.getUnhappyCounter() > 0;
        state.villagerData = entity.getVillagerData();
    }
}
