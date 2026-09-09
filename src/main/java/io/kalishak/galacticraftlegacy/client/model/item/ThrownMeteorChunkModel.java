/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.model.item;

import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

public class ThrownMeteorChunkModel {
    public static final Identifier ID = Constants.id("entity/thrown_meteor_chunk");
    public static final StandaloneModelKey<QuadCollection> KEY = new StandaloneModelKey<>(ID::toString);
}
