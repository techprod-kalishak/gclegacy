package io.kalishak.galacticraftlegacy.galaxies.celestial;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.components.GalacticraftDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.TypedInstance;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;


public final class CelestialBodyInstance implements Nameable, DataComponentHolder, TypedInstance<CelestialBody> {
    public static final Codec<CelestialBodyInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CelestialBody.CODEC.fieldOf("celestial_body").forGetter(CelestialBodyInstance::typeHolder),
            DataComponentMap.CODEC.fieldOf("components").forGetter(x -> x.components)
    ).apply(instance, CelestialBodyInstance::new));
    private final Holder<CelestialBody> typeHolder;
    private final PatchedDataComponentMap components;
    private boolean shouldSync;

    private CelestialBodyInstance(Holder<CelestialBody> typeHolder, DataComponentMap components) {
        this.typeHolder = typeHolder;
        this.components = new PatchedDataComponentMap(components);
    }

    public CelestialBodyInstance(Holder<CelestialBody> typeHolder) {
        this(typeHolder, DataComponentMap.builder().build());
    }

    @Override
    public Holder<CelestialBody> typeHolder() {
        return this.typeHolder;
    }

    @Override
    public PatchedDataComponentMap getComponents() {
        return this.components;
    }

    @Override
    public Component getName() {
        return getCustomName() == null ? Component.translatable(this.typeHolder.value().getDescriptionId()) : null;
    }

    @Override
    public @Nullable Component getCustomName() {
        return this.components.get(DataComponents.CUSTOM_NAME);
    }

    public void setOwner(EntityReference<Player> owner) {
        this.components.set(GalacticraftDataComponents.BODY_OWNER.get(), owner);
        this.shouldSync = true;
    }

    public @Nullable EntityReference<Player> getOwner() {
        return this.components.get(GalacticraftDataComponents.BODY_OWNER);
    }

    public void setCustomName(Component customName) {
        this.components.set(DataComponents.CUSTOM_NAME, customName);
        this.shouldSync = true;
    }

    public void setPosition(Vec3 position) {
        this.components.set(GalacticraftDataComponents.BODY_POSITION.get(), position);
        this.shouldSync = true;
    }

    public Vec3 getPosition() {
        return this.components.getOrDefault(GalacticraftDataComponents.BODY_POSITION, Vec3.ZERO);
    }

    public void doSync(Consumer<CelestialBodyInstance> consumer) {
        if (this.shouldSync) {
            consumer.accept(this);
        }
    }
}
