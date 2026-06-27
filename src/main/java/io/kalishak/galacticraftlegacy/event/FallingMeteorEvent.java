/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.event;

import io.kalishak.galacticraftlegacy.world.level.block.FallenMeteorBlock;
import net.minecraft.core.BlockPos;
import net.neoforged.bus.api.Event;

public abstract class FallingMeteorEvent extends Event {
    private final BlockPos worldPosition;
    private final FallenMeteorBlock fallenMeteorBlock;

    protected FallingMeteorEvent(BlockPos worldPosition, FallenMeteorBlock fallenMeteorBlock) {
        this.worldPosition = worldPosition;
        this.fallenMeteorBlock = fallenMeteorBlock;
    }

    public BlockPos getWorldPosition() {
        return this.worldPosition;
    }

    public FallenMeteorBlock getFallenMeteorBlock() {
        return this.fallenMeteorBlock;
    }

    public static class Pre extends FallingMeteorEvent {
        public Pre(BlockPos worldPosition, FallenMeteorBlock fallenMeteorBlock) {
            super(worldPosition, fallenMeteorBlock);
        }
    }

    public static class Post extends FallingMeteorEvent {
        public Post(BlockPos worldPosition, FallenMeteorBlock fallenMeteorBlock) {
            super(worldPosition, fallenMeteorBlock);
        }
    }
}
