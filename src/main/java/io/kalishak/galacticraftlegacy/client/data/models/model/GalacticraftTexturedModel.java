/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.data.models.model;

import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;

public class GalacticraftTexturedModel {
    public static final TexturedModel.Provider SIMPLE_MACHINE = TexturedModel.createDefault(GalacticraftTextureMapping::simpleMachine, ModelTemplates.CUBE);
    public static final TexturedModel.Provider OXYGEN_COLLECTOR = TexturedModel.createDefault(GalacticraftTextureMapping::oxygenCollector, ModelTemplates.CUBE);
}
