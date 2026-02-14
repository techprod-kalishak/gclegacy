package io.kalishak.galacticraftlegacy.client.renderer.entity.state;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;

public class ParachestRenderState extends EntityRenderState {
    public BlockState blockState;
    public float angle;
    public DyeColor parachuteColor = DyeColor.WHITE;
}
