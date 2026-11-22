package io.kalishak.galacticraftlegacy.client.renderer.entity.state.monster;

import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenderState;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenders;
import net.minecraft.client.renderer.entity.state.CreeperRenderState;

public class EvolvedCreeperRenderState extends CreeperRenderState implements GearRenderState {
    public GearRenders gearRenders;

    @Override
    public GearRenders gearRenders() {
        return this.gearRenders;
    }
}
