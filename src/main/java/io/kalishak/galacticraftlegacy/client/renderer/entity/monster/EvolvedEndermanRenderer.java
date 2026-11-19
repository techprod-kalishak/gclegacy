package io.kalishak.galacticraftlegacy.client.renderer.entity.monster;

import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.EvolvedCarriedBlockLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.EvolvedEnderEyesLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear.OxygenGearLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear.OxygenMaskLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear.OxygenTankLayer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.EvolvedEndermanRenderState;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenders;
import io.kalishak.galacticraftlegacy.world.entity.monster.EvolvedEnderman;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.enderman.EndermanModel;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EvolvedEndermanRenderer extends MobRenderer<EvolvedEnderman, EvolvedEndermanRenderState, EndermanModel<EvolvedEndermanRenderState>> {
    public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
    private static final Identifier ENDERMAN_LOCATION = Identifier.withDefaultNamespace("textures/entity/enderman/enderman.png");
    private final RandomSource random = RandomSource.create();
    private final BlockModelResolver blockModelResolver;

    public EvolvedEndermanRenderer(EntityRendererProvider.Context context) {
        super(context, new EndermanModel<>(context.bakeLayer(ModelLayers.ENDERMAN)), 0.5F);
        this.blockModelResolver = context.getBlockModelResolver();
        addLayer(new EvolvedEnderEyesLayer(this));
        addLayer(new EvolvedCarriedBlockLayer(this));
        addLayer(new OxygenMaskLayer<>(this, context.getModelSet(), context.getEquipmentRenderer(), context.getEquipmentAssets(), -0.78F, 0.0F));
        addLayer(new OxygenGearLayer<>(this, context.getModelSet(), context.getEquipmentRenderer(), context.getEquipmentAssets(), -0.15F, 0.0F));
        addLayer(new OxygenTankLayer<>(this, context.getModelSet(), context.getEquipmentRenderer(), context.getEquipmentAssets(), -0.70F, 0.255F));
    }

    @Override
    public Vec3 getRenderOffset(EvolvedEndermanRenderState state) {
        Vec3 offset = super.getRenderOffset(state);
        if (state.isCreepy) {
            double d = 0.02 * state.scale;
            return offset.add(this.random.nextGaussian() * d, 0.0, this.random.nextGaussian() * d);
        } else {
            return offset;
        }
    }

    @Override
    public Identifier getTextureLocation(EvolvedEndermanRenderState state) {
        return ENDERMAN_LOCATION;
    }

    @Override
    public EvolvedEndermanRenderState createRenderState() {
        return new EvolvedEndermanRenderState();
    }

    @Override
    public void extractRenderState(EvolvedEnderman entity, EvolvedEndermanRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        HumanoidMobRenderer.extractHumanoidRenderState(entity, state, partialTicks, this.itemModelResolver);
        state.isCreepy = entity.isCreepy();
        state.gearRenders = GearRenders.fixed();
        BlockState carriedBlock = entity.getCarriedBlock();

        if (carriedBlock != null) {
            this.blockModelResolver.update(state.carriedBlock, carriedBlock, BLOCK_DISPLAY_CONTEXT);
        } else {
            state.carriedBlock.clear();
        }
    }
}
