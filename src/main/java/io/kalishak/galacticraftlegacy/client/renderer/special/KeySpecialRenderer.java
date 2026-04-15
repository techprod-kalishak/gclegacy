package io.kalishak.galacticraftlegacy.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.client.model.geom.GalacticraftModelLayers;
import io.kalishak.galacticraftlegacy.client.renderer.item.KeyModel;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.item.KeyLock;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class KeySpecialRenderer implements SpecialModelRenderer<FeatureTier> {
    private final KeyModel keyModel;

    public KeySpecialRenderer(KeyModel keyModel) {
        this.keyModel = keyModel;
    }

    @Override
    public FeatureTier extractArgument(ItemStack itemStack) {
        KeyLock keyLock = itemStack.get(GalacticraftDataComponents.KEY_LOCK);

        return keyLock != null ? keyLock.featureTier() : FeatureTier.TIER_1;
    }

    @Override
    public void submit(@Nullable FeatureTier argument, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        poseStack.pushPose();
        poseStack.translate(1.0D, -1.0D, -1.0D);
        submitNodeCollector.submitModelPart(
                this.keyModel.root(),
                poseStack,
                this.keyModel.renderType(KeyModel.getTexture(argument)),
                lightCoords,
                overlayCoords,
                null,
                false,
                hasFoil,
                -1,
                null,
                outlineColor
        );

        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {
        PoseStack poseStack = new PoseStack();
        poseStack.scale(1.0F, -1.0F, -1.0F);
        this.keyModel.root().getExtentsForGui(poseStack, consumer);
    }

    public record Unbaked(FeatureTier featureTier) implements SpecialModelRenderer.Unbaked<FeatureTier> {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                FeatureTier.CODEC.fieldOf("feature_tier").forGetter(Unbaked::featureTier)
        ).apply(instance, Unbaked::new));

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<FeatureTier> bake(BakingContext bakingContext) {
            return new KeySpecialRenderer(new KeyModel(bakingContext.entityModelSet().bakeLayer(GalacticraftModelLayers.KEY)));
        }
    }
}
