package io.kalishak.galacticraftlegacy.world.item.crafting;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public enum VehicleCraftingBookCategory implements SerializableEnum {
    ROCKET(0, "rocket"),
    LAND_VEHICLE(1, "land_vehicle"),
    FLOATING_VEHICLE(2, "floating_vehicle"),
    MISC(3, "misc"),;

    public static final Codec<VehicleCraftingBookCategory> CODEC = SerializableEnum.codec(VehicleCraftingBookCategory.class);
    public static final StreamCodec<ByteBuf, VehicleCraftingBookCategory> STREAM_CODEC = SerializableEnum.streamCodec(VehicleCraftingBookCategory.class);
    private final int id;
    private final String name;

    VehicleCraftingBookCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getIndex() {
        return this.id;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
