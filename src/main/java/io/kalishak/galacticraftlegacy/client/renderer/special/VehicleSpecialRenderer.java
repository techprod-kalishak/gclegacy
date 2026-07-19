package io.kalishak.galacticraftlegacy.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.item.vehicle.RocketItem;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class VehicleSpecialRenderer implements SpecialModelRenderer<Holder<EntityType<?>>> {
    @Override
    public void submit(@Nullable Holder<EntityType<?>> entityType, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int i1, boolean b, int i2) {

    }

    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {

    }

    @Override
    public @Nullable Holder<EntityType<?>> extractArgument(ItemStack itemStack) {
        Item item = itemStack.getItem();

        if (item instanceof RocketItem rocketItem) {
            return rocketItem.getVehicleType();
        }

        return null;
    }

    public record Unbaked(Holder<EntityType<?>> vehicleType) implements SpecialModelRenderer.Unbaked<Holder<EntityType<?>>> {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                BuiltInRegistries.ENTITY_TYPE.holderByNameCodec().fieldOf("vehicle_type").forGetter(Unbaked::vehicleType)
        ).apply(instance, Unbaked::new));

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public VehicleSpecialRenderer bake(BakingContext bakingContext) {
            return new VehicleSpecialRenderer();
        }
    }
}
