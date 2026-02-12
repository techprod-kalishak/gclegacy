package io.kalishak.galacticraftlegacy.client.renderer.entity.state;

import io.kalishak.galacticraftlegacy.client.renderer.blockentity.state.MovingParachestRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.item.DyeColor;

public class ParachestRenderState extends EntityRenderState {
    public MovingParachestRenderState movingParachestRenderState = new MovingParachestRenderState();
    public DyeColor parachuteColor = DyeColor.WHITE;
}
