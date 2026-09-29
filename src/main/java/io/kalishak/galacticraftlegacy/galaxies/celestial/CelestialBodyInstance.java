package io.kalishak.galacticraftlegacy.galaxies.celestial;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.TypedInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.jspecify.annotations.Nullable;

import java.util.Optional;


public final class CelestialBodyInstance implements Nameable, TypedInstance<CelestialBody>, ValueIOSerializable {
    public static final String TAG_POS = "Position";
    public static final String TAG_OWNER = "Owner";
    public static final String TAG_CUSTOM_NAME = "CustomName";
    public static final MapCodec<CelestialBodyInstance> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CelestialBody.CODEC.fieldOf("celestial_body").forGetter(CelestialBodyInstance::typeHolder),
            Vec3.CODEC.optionalFieldOf("position", Vec3.ZERO).forGetter(CelestialBodyInstance::getPosition),
            ComponentSerialization.CODEC.optionalFieldOf("custom_name").forGetter(bodyInstance -> Optional.ofNullable(bodyInstance.customName)),
            EntityReference.<Player>codec().optionalFieldOf("owner").forGetter(bodyInstance -> Optional.ofNullable(bodyInstance.owner))
    ).apply(instance, CelestialBodyInstance::fromCodec));
    private final Holder<CelestialBody> typeHolder;
    private Vec3 position;
    private @Nullable Component customName;
    private @Nullable EntityReference<Player> owner;
    private boolean shouldSync;

    public CelestialBodyInstance(Holder<CelestialBody> typeHolder) {
        this.typeHolder = typeHolder;
        this.position = typeHolder.value().getInitialPosition();
    }

    private static CelestialBodyInstance fromCodec(Holder<CelestialBody> typeHolder, Vec3 position, Optional<Component> customName, Optional<EntityReference<Player>> owner) {
        CelestialBodyInstance instance = new CelestialBodyInstance(typeHolder);
        instance.setPosition(position);
        customName.ifPresent(instance::setCustomName);
        owner.ifPresent(instance::setOwner);

        return instance;
    }

    @Override
    public Holder<CelestialBody> typeHolder() {
        return this.typeHolder;
    }

    @Override
    public Component getName() {
        return getCustomName() == null ? Component.translatable(this.typeHolder.value().getDescriptionId()) : null;
    }

    @Override
    public @Nullable Component getCustomName() {
        return this.customName;
    }

    public void setOwner(@Nullable EntityReference<Player> owner) {
        this.owner = owner;
        this.shouldSync = true;
    }

    public @Nullable EntityReference<Player> getOwner() {
        return this.owner;
    }

    public void setCustomName(@Nullable Component customName) {
        this.customName = customName;
        this.shouldSync = true;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
        this.shouldSync = true;
    }

    public Vec3 getPosition() {
        return this.position;
    }

    @Override
    public void serialize(ValueOutput output) {
        output.store(TAG_POS, Vec3.CODEC, this.position);
        output.storeNullable(TAG_CUSTOM_NAME, ComponentSerialization.CODEC, this.customName);
        output.storeNullable(TAG_OWNER, EntityReference.codec(), this.owner);
    }

    @Override
    public void deserialize(ValueInput input) {
        input.read(TAG_POS, Vec3.CODEC).ifPresent(this::setPosition);
        input.read(TAG_CUSTOM_NAME, ComponentSerialization.CODEC).ifPresent(this::setCustomName);
        input.read(TAG_OWNER, EntityReference.<Player>codec()).ifPresent(this::setOwner);
    }
}
