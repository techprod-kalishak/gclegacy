package io.kalishak.galacticraftlegacy.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.client.renderer.blockentity.state.PipeBlockEntityRenderState;
import io.kalishak.galacticraftlegacy.world.level.block.entity.wire.ColoredPipeBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jspecify.annotations.Nullable;

public class PipeBlockRenderer implements BlockEntityRenderer<ColoredPipeBlockEntity, PipeBlockEntityRenderState> {
    public PipeBlockRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public PipeBlockEntityRenderState createRenderState() {
        return new PipeBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(ColoredPipeBlockEntity blockEntity, PipeBlockEntityRenderState renderState, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);

        renderState.fluidColor = blockEntity.getExistingData(GalacticraftAttachments.SYNC_FLUID_STACK).map(fluidStack -> IClientFluidTypeExtensions.of(fluidStack.getFluidType()).getTintColor()).orElse(-1);
    }

    @Override
    public void submit(PipeBlockEntityRenderState pipeBlockEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {

    }
}
