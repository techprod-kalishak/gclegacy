package io.kalishak.galacticraftlegacy.client.renderer.blockentity.state;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidTankBlockRenderState extends BlockEntityRenderState {
    public BlockModelRenderState liquidBlock;
    public FluidStack fluid;
}
