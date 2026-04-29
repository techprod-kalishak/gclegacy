package io.kalishak.galacticraftlegacy.client.renderer.entity.state;

import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.world.item.ItemStack;

public class EvolvedZombieRenderState extends ZombieRenderState implements GearRenderState {
    public ItemStack oxygenMask;
    public ItemStack oxygenGear;
    public ItemStack tank;
    public ItemStack additionalTank;

    @Override
    public ItemStack oxygenMask() {
        return this.oxygenMask.copy();
    }

    @Override
    public ItemStack oxygenGear() {
        return this.oxygenGear;
    }

    @Override
    public ItemStack leftTank() {
        return this.tank.copy();
    }

    @Override
    public ItemStack rightTank() {
        return this.additionalTank.copy();
    }
}
