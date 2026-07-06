package io.kalishak.galacticraftlegacy.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.item.VehicleComponentType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Supplier;

public record VehiclePart(VehicleComponentType vehicleComponentType, List<FeatureTier> acceptsTiers) {
    public static final Codec<VehiclePart> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VehicleComponentType.CODEC.fieldOf("rocket_component_type").forGetter(VehiclePart::vehicleComponentType),
            FeatureTier.CODEC.listOf(1, 4).fieldOf("accepts_tiers").forGetter(VehiclePart::acceptsTiers)
    ).apply(instance, VehiclePart::new));
    public static final StreamCodec<ByteBuf, VehiclePart> STREAM_CODEC = StreamCodec.composite(
            VehicleComponentType.STREAM_CODEC, VehiclePart::vehicleComponentType,
            FeatureTier.STREAM_CODEC.apply(ByteBufCodecs.list(4)), VehiclePart::acceptsTiers,
            VehiclePart::new
    );

    public static Supplier<Item.Properties> simpleProperties(VehicleComponentType componentType, int maxStackSize, FeatureTier... acceptsTiers) {
        return () -> new Item.Properties()
                .component(
                        GalacticraftDataComponents.ROCKET_PART,
                        new VehiclePart(
                                componentType,
                                List.of(acceptsTiers)
                        ))
                .stacksTo(maxStackSize);
    }
}
