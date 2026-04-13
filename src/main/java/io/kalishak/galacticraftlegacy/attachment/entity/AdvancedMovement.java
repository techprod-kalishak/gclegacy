package io.kalishak.galacticraftlegacy.attachment.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class AdvancedMovement {
    public static final MapCodec<AdvancedMovement> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.DOUBLE.fieldOf("x").forGetter(AdvancedMovement::getAdvancedPositionX),
            Codec.DOUBLE.fieldOf("y").forGetter(AdvancedMovement::getAdvancedPositionY),
            Codec.DOUBLE.fieldOf("z").forGetter(AdvancedMovement::getAdvancedPositionZ),
            Codec.DOUBLE.fieldOf("yaw").forGetter(AdvancedMovement::getAdvancedYaw),
            Codec.DOUBLE.fieldOf("pitch").forGetter(AdvancedMovement::getAdvancedPitch)
    ).apply(instance, AdvancedMovement::new));
    public static final StreamCodec<ByteBuf, AdvancedMovement> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, AdvancedMovement::getAdvancedPositionX,
            ByteBufCodecs.DOUBLE, AdvancedMovement::getAdvancedPositionY,
            ByteBufCodecs.DOUBLE, AdvancedMovement::getAdvancedPositionZ,
            ByteBufCodecs.DOUBLE, AdvancedMovement::getAdvancedYaw,
            ByteBufCodecs.DOUBLE, AdvancedMovement::getAdvancedPitch,
            AdvancedMovement::new
    );
    public double advancedPositionX;
    public double advancedPositionY;
    public double advancedPositionZ;
    public double advancedYaw;
    public double advancedPitch;

    public AdvancedMovement(double posX, double posY, double posZ, double posYaw, double posPitch) {
        this.advancedPositionX = posX;
        this.advancedPositionY = posY;
        this.advancedPositionZ = posZ;
        this.advancedYaw = posYaw;
        this.advancedPitch = posPitch;
    }

    public AdvancedMovement() {}

    public double getAdvancedPositionX() {
        return this.advancedPositionX;
    }

    public void setAdvancedPositionX(double advancedPositionX) {
        this.advancedPositionX = advancedPositionX;
    }

    public double getAdvancedPositionY() {
        return this.advancedPositionY;
    }

    public void setAdvancedPositionY(double advancedPositionY) {
        this.advancedPositionY = advancedPositionY;
    }

    public double getAdvancedPositionZ() {
        return this.advancedPositionZ;
    }

    public void setAdvancedPositionZ(double advancedPositionZ) {
        this.advancedPositionZ = advancedPositionZ;
    }

    public double getAdvancedYaw() {
        return this.advancedYaw;
    }

    public void setAdvancedYaw(double advancedYaw) {
        this.advancedYaw = advancedYaw;
    }

    public double getAdvancedPitch() {
        return this.advancedPitch;
    }

    public void setAdvancedPitch(double advancedPitch) {
        this.advancedPitch = advancedPitch;
    }
}
