/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.data.models.model;

import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

public class GalacticraftModelTemplates {
    public static final ModelTemplate ROTATING_ITEM = ExtendedModelTemplateBuilder.of(ModelTemplates.PARTICLE_ONLY)
            .transform(
                    ItemDisplayContext.GUI,
                    builder -> builder
                            .rotation(30.0F, 45.0F, 0.0F)
                            .translation(0.0F, 1.0F, 0.0F)
                            .scale(0.5F)
            )
            .build();
    public static final ModelTemplate NASA_WORKBENCH = ExtendedModelTemplateBuilder.of(ModelTemplates.CUBE_BOTTOM_TOP)
            .transform(
                    ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
                    builder -> builder
                            .rotation(10.0F, -45.0F, 170.0F)
                            .translation(0.0F, 1.5F, 2.75F)
                            .scale(0.375F)
            )
            .transform(
                    ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                    builder -> builder
                            .rotation(10.0F, -45.0F, 170.0F)
                            .translation(0.0F, 1.5F, -2.75F)
                            .scale(0.375F)
            )
            .transform(
                    ItemDisplayContext.ON_SHELF,
                    builder -> builder
                            .scale(0.8F)
            )
            .build();
    public static final ModelTemplate PAD = ExtendedModelTemplateBuilder.builder()
            .requiredTextureSlot(TextureSlot.TEXTURE)
            .parent(Constants.id("block/pad"))
            .build();
    public static final ModelTemplate FULL_PAD = ExtendedModelTemplateBuilder.builder()
            .requiredTextureSlot(TextureSlot.TEXTURE)
            .requiredTextureSlot(TextureSlot.TOP)
            .parent(Constants.id("block/full_pad"))
            .suffix("_full")
            .build();
}
