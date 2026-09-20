/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.blockentity.state;

import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import net.minecraft.core.Direction;

public class DungeonBlockRenderState extends OpenableBlockRenderState {
    public float open;
    public Direction facing;
    public boolean unlocked;
    public FeatureTier featureTier;
}
