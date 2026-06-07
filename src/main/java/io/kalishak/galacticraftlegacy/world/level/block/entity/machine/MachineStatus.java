/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.codec.SerializableEnum;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public record MachineStatus(Type type) {
    public static final String TAG_VALUE = "MachineStatus";
    public static final MachineStatus DEFAULT = new MachineStatus(Type.IDLE);
    public static final MapCodec<MachineStatus> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Type.CODEC.fieldOf("type").forGetter(MachineStatus::type)
    ).apply(instance, MachineStatus::new));
    public static final StreamCodec<ByteBuf, MachineStatus> STREAM_CODEC = Type.STREAM_CODEC.map(MachineStatus::new, MachineStatus::type);

    public static void deserialize(ValueInput input, Consumer<MachineStatus> consumer) {
        consumer.accept(input.read(TAG_VALUE, MAP_CODEC.codec()).orElse(DEFAULT));
    }

    public static void serialize(ValueOutput output, @Nullable MachineStatus value) {
        if (value != null && value != DEFAULT) {
            output.store(TAG_VALUE, MAP_CODEC.codec(), value);
        }
    }

    public enum Type implements SerializableEnum {
        IDLE("idle", 0),
        GENERATING("generating", 1),
        COLLECTING("collecting", 2),
        NOT_ENOUGH_POWER("not_enough_power", 3),
        NOT_ENOUGH_OXYGEN("not_enough_oxygen", 4),
        EMPTY_TANK("empty_tank", 5),
        NEEDS_REDSTONE_SIGNAL("needs_redstone_signal", 6),
        BLOCKED("blocked", 7);

        public static final Codec<Type> CODEC = SerializableEnum.codec(Type.class);
        public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = SerializableEnum.streamCodec(Type.class);
        private final String name;
        private final int id;

        Type(String name, int id) {
            this.name = name;
            this.id = id;
        }

        @Override
        public @NonNull String getSerializedName() {
            return this.name;
        }

        @Override
        public int getIndex() {
            return this.id;
        }
    }
}
