/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Optional;

public record ExtinguishedWithoutOxygen(BlockState state, Optional<Direction> applyFace) {
    public static final Codec<ExtinguishedWithoutOxygen> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("state").forGetter(ExtinguishedWithoutOxygen::state),
            BlockStateProperties.FACING.codec().optionalFieldOf("apply_face").forGetter(ExtinguishedWithoutOxygen::applyFace)
    ).apply(instance, ExtinguishedWithoutOxygen::new));

    public ExtinguishedWithoutOxygen(BlockState state) {
        this(state, Optional.empty());
    }
}
