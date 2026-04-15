/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.item.properties.numeric;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class DungeonLocatorAngle implements RangeSelectItemModelProperty {
    public static final MapCodec<DungeonLocatorAngle> MAP_CODEC = DungeonLocatorAngleState.MAP_CODEC.xmap(DungeonLocatorAngle::new, dungeonLocatorAngle -> dungeonLocatorAngle.state);

    private final DungeonLocatorAngleState state;

    private DungeonLocatorAngle(DungeonLocatorAngleState state) {
        this.state = state;
    }

    public DungeonLocatorAngle() {
        this(new DungeonLocatorAngleState());
    }

    @Override
    public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        return this.state.get(stack, level, owner, seed);
    }

    @Override
    public MapCodec<DungeonLocatorAngle> type() {
        return MAP_CODEC;
    }
}
