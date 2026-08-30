package io.kalishak.galacticraftlegacy.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.client.model.entity.Tier1RocketModel;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.entity.Tier1RocketRenderer;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.RocketRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import org.jspecify.annotations.Nullable;

public record CargoRocketSpecialRenderer(Tier1RocketModel model) implements SpecialVehicleRenderer<RocketRenderState, Tier1RocketModel> {
    @Override
    public void submit(@Nullable Tier1RocketModel argument, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.scale(0.25F, 0.25F, 0.25F);

        submitModel(submitNodeCollector, RocketRenderState.INSTANCE, poseStack, Tier1RocketRenderer.TEXTURES, lightCoords, overlayCoords, outlineColor);
        poseStack.popPose();
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<Tier1RocketModel> {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);

        @Override
        public CargoRocketSpecialRenderer bake(BakingContext bakingContext) {
            return new CargoRocketSpecialRenderer(new Tier1RocketModel(bakingContext.entityModelSet().bakeLayer(GalacticraftModelLayers.TIER_1_ROCKET)));
        }

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
