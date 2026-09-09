/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.RocketRenderState;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public interface SpecialVehicleRenderer<S extends RocketRenderState, T extends Model<S>> extends SpecialModelRenderer<T> {
    T model();

    @Override
    default void getExtents(Consumer<Vector3fc> output) {
    }

    @Override
    default @Nullable T extractArgument(ItemStack stack) {
        return model();
    }

    default void submitModel(SubmitNodeCollector submitNodeCollector, S renderState, PoseStack poseStack, Identifier textures, int lightCoords, int overlayCoords, int outlineColor) {
        submitNodeCollector.submitModel(
                model(),
                renderState,
                poseStack,
                model().renderType(textures),
                lightCoords,
                overlayCoords,
                0,
                null,
                outlineColor,
                null
        );
    }
}
