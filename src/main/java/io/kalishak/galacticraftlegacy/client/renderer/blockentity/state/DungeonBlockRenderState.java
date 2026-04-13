package io.kalishak.galacticraftlegacy.client.renderer.blockentity.state;

import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class DungeonBlockRenderState extends BlockEntityRenderState {
    public float open;
    public float angle;
    public boolean unlocked;
    public FeatureTier featureTier;
}
