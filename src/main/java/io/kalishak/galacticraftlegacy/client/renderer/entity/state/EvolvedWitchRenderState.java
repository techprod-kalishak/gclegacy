package io.kalishak.galacticraftlegacy.client.renderer.entity.state;

import net.minecraft.client.renderer.entity.state.WitchRenderState;

public class EvolvedWitchRenderState extends WitchRenderState implements GearRenderState {
    public GearRenders gearRenders;

    @Override
    public GearRenders gearRenders() {
        return this.gearRenders;
    }
}
