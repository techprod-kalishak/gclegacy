/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.entity.state.monster;

import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenderState;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenders;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;

public class EvolvedZombieRenderState extends ZombieRenderState implements GearRenderState {
    public GearRenders gearRenders;

    @Override
    public GearRenders gearRenders() {
        return this.gearRenders;
    }
}
