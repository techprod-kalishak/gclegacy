package io.kalishak.galacticraftlegacy.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.client.renderer.entity.ObjModelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public record ItemObjSpecialRenderer(Identifier id) implements SpecialModelRenderer<Identifier> {
    @Override
    public void submit(@Nullable Identifier identifier, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int i1, boolean b, int i2) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        QuadCollection quadCollection = Minecraft.getInstance().getModelManager().getStandaloneModel(new StandaloneModelKey<>(this.id::toString));
        ObjModelHelper.renderQuads(quadCollection, poseStack, submitNodeCollector, RenderTypes.entitySolid(this.id.withPrefix("textures/")), _ -> {});
        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {

    }

    @Override
    public @Nullable Identifier extractArgument(ItemStack itemStack) {
        return this.id;
    }

    public record Unbaked(Identifier id) implements SpecialModelRenderer.Unbaked<Identifier> {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Identifier.CODEC.fieldOf("id").forGetter(Unbaked::id)
        ).apply(instance, Unbaked::new));

        @Override
        public SpecialModelRenderer<Identifier> bake(BakingContext bakingContext) {
            return new ItemObjSpecialRenderer(this.id);
        }

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
