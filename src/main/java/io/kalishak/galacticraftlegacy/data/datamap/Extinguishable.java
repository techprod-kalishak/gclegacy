/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;

public record Extinguishable(BlockState unlitState, boolean hasFacingProperty) {
    public static final Codec<Extinguishable> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("unlit_state").forGetter(Extinguishable::unlitState),
            Codec.BOOL.optionalFieldOf("has_facing_property", false).forGetter(Extinguishable::hasFacingProperty)
    ).apply(instance, Extinguishable::new));

    public Extinguishable(BlockState unlitState) {
        this(unlitState, false);
    }
}
