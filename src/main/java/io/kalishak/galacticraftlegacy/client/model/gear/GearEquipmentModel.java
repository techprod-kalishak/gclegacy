/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.model.gear;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class GearEquipmentModel<S extends LivingEntityRenderState> extends Model<S> {
    public GearEquipmentModel(ModelPart root, Function<Identifier, RenderType> renderType) {
        super(root, renderType);
    }

    protected UnaryOperator<Float> animationSupplier() {
        return rot -> rot * (float) (Math.PI / 180);
    }

    @Override
    public void setupAnim(S renderState) {
        super.setupAnim(renderState);
        this.root.xRot = animationSupplier().apply(renderState.xRot);
        this.root.yRot = animationSupplier().apply(renderState.yRot);
    }

    public static <S extends LivingEntityRenderState> GearEquipmentModel<S> simple(ModelPart root, Function<Identifier, RenderType> renderType) {
        return new GearEquipmentModel<>(root, renderType);
    }
}
