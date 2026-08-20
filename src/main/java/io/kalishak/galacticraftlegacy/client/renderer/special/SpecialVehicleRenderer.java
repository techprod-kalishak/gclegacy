package io.kalishak.galacticraftlegacy.client.renderer.special;

import io.kalishak.galacticraftlegacy.client.renderer.entity.state.RocketRenderState;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
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
}
