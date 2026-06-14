package io.kalishak.galacticraftlegacy.client.renderer.entity;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.ThrownMeteorChunkRenderState;
import io.kalishak.galacticraftlegacy.world.entity.projectile.ThrownMeteorChunk;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ThrownMeteorRenderer extends EntityRenderer<ThrownMeteorChunk, ThrownMeteorChunkRenderState> {
    public ThrownMeteorRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ThrownMeteorChunkRenderState createRenderState() {
        return new ThrownMeteorChunkRenderState();
    }

    @Override
    public void extractRenderState(ThrownMeteorChunk entity, ThrownMeteorChunkRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.scaledHeatLevel = entity.getData(GalacticraftAttachments.HOT_CONTENT).getScaledHeatLevel();
    }
}
