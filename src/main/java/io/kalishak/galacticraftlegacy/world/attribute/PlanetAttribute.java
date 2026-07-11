/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.attribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record PlanetAttribute(float gravity, float fallDamageMultiplier) {
    public static final Codec<PlanetAttribute> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("gravity").forGetter(PlanetAttribute::gravity),
            Codec.FLOAT.fieldOf("fall_damage_multiplier").forGetter(PlanetAttribute::fallDamageMultiplier)
    ).apply(instance, PlanetAttribute::new));
    public static final PlanetAttribute DEFAULT = new PlanetAttribute(0.08F, 1.0F);
}
