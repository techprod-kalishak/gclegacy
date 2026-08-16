package io.kalishak.galacticraftlegacy.client.renderer.entity.state;

import io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket.AbstractSpaceShip;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class RocketRenderState extends EntityRenderState {
    public AbstractSpaceShip.LaunchPhase launchPhase;
    public boolean launched;
    public int timeUntilLaunch;
    public float roll;
    public float previousYRot;
    public float previousXRot;
}
