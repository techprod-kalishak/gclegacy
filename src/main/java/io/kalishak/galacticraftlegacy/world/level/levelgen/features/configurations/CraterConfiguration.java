/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.features.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.level.levelgen.CraterSize;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record CraterConfiguration(CraterSize craterSize, IntProvider spacing) implements FeatureConfiguration {
    public static final Codec<CraterConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CraterSize.CODEC.fieldOf("crater_size").forGetter(CraterConfiguration::craterSize),
            IntProviders.codec(0, 32).fieldOf("spacing").forGetter(CraterConfiguration::spacing)
    ).apply(instance, CraterConfiguration::new));
}
