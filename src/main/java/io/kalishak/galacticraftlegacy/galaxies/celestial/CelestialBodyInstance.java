package io.kalishak.galacticraftlegacy.galaxies.celestial;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.components.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket.TieredRocket;
import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.TypedInstance;
import net.minecraft.core.component.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public final class CelestialBodyInstance implements Nameable, DataComponentHolder, TypedInstance<Celestial> {
    public static final Codec<CelestialBodyInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.lazyInitialized(() -> Celestial.CODEC).fieldOf("celestial_body").forGetter(CelestialBodyInstance::typeHolder),
            DataComponentMap.CODEC.fieldOf("components").forGetter(x -> x.components)
    ).apply(instance, CelestialBodyInstance::new));
    public static final Codec<CelestialBodyInstance> OPTIONAL_CODEC = ExtraCodecs.optionalEmptyMap(CODEC)
            .xmap(optionalCelestial -> optionalCelestial.orElse(CelestialBodyInstance.EMPTY), celestial -> celestial.isEmpty() ? Optional.empty() : Optional.of(celestial));
    public static final StreamCodec<RegistryFriendlyByteBuf, CelestialBodyInstance> STREAM_CODEC = createOptionalStreamCodec(DataComponentPatch.STREAM_CODEC);
    public static final CelestialBodyInstance EMPTY = new CelestialBodyInstance(null, DataComponentMap.EMPTY);
    private final Holder<Celestial> typeHolder;
    private final PatchedDataComponentMap components;
    private boolean shouldSync;

    private CelestialBodyInstance(Holder<Celestial> typeHolder, DataComponentMap components) {
        this.typeHolder = typeHolder;
        this.components = new PatchedDataComponentMap(components);
    }

    public CelestialBodyInstance(Holder<Celestial> typeHolder) {
        this(typeHolder, DataComponentMap.builder().build());
    }

    private static StreamCodec<RegistryFriendlyByteBuf, CelestialBodyInstance> createOptionalStreamCodec(StreamCodec<RegistryFriendlyByteBuf, DataComponentPatch> patchCodec) {
        return new StreamCodec<>() {
            public CelestialBodyInstance decode(RegistryFriendlyByteBuf input) {
                int isEmpty = input.readVarInt();

                if (isEmpty == 0) {
                    return CelestialBodyInstance.EMPTY;
                }

                Holder<Celestial> celestialHolder = Celestial.STREAM_CODEC.decode(input);
                DataComponentPatch patch = patchCodec.decode(input);

                return new CelestialBodyInstance(celestialHolder, PatchedDataComponentMap.fromPatch(celestialHolder.components(), patch));
            }

            public void encode(RegistryFriendlyByteBuf output, CelestialBodyInstance celestial) {
                if (celestial.isEmpty()) {
                    output.writeVarInt(0);
                } else {
                    Celestial.STREAM_CODEC.encode(output, celestial.typeHolder());
                    patchCodec.encode(output, celestial.components.asPatch());
                }
            }
        };
    }

    @Override
    public Holder<Celestial> typeHolder() {
        return this.typeHolder;
    }

    @Override
    public PatchedDataComponentMap getComponents() {
        return this.components;
    }

    @Override
    public Component getName() {
        return getCustomName() == null ? this.typeHolder.value().description() : null;
    }

    @Override
    public @Nullable Component getCustomName() {
        return this.components.get(DataComponents.CUSTOM_NAME);
    }

    public boolean isEmpty() {
        return this.typeHolder == null;
    }

    public CelestialBodyInstance parent(WorldCelestials worldCelestials) {
        return this.typeHolder.value().parent().map(worldCelestials::get).orElse(EMPTY);
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

    public boolean is(CelestialBodyInstance other) {
        return is(other.typeHolder);
    }

    public boolean isReachable() {
        return this.typeHolder.value().targetDimension().isPresent() && this.typeHolder.value().requiredTier() != FeatureTier.UNREACHABLE;
    }

    public boolean isReachable(FeatureTier featureTier) {
        return this.typeHolder.value().targetDimension().isPresent() && this.typeHolder.value().requiredTier().getLevel() <= featureTier.getLevel();
    }

    public boolean isSatellite() {
        return hasParent() && this.typeHolder.value().recipe().isPresent();
    }

    public boolean hasParent() {
        return isSatellite() || this.typeHolder.value().parent().isPresent();
    }

    public static boolean isLesserBody(RegistryAccess registryAccess, CelestialBodyInstance celestial, WorldCelestials worldCelestials) {
        return celestial.isSatellite() || getChildren(registryAccess, celestial, worldCelestials).isEmpty();
    }

    public static boolean areSameType(Holder<Celestial> first, Holder<Celestial> other) {
        return first.tags().anyMatch(other::is);
    }

    public static boolean isOwnedByPlayer(CelestialBodyInstance celestialBody, Player player) {
        return celestialBody.getOwner() != null && celestialBody.getOwner().matches(player);
    }

    public static Set<CelestialBodyInstance> showReachable(Collection<CelestialBodyInstance> registry, TieredRocket rocket) {
        return registry.stream().filter(celestialObject -> celestialObject.isReachable(rocket.getFeatureTier())).collect(Collectors.toSet());
    }

    public static Collection<CelestialBodyInstance> getChildren(RegistryAccess access, CelestialBodyInstance celestialBody, WorldCelestials worldCelestials) {
        Registry<Celestial> celestialBodies = access.lookupOrThrow(GalacticraftRegistries.Keys.CELESTIAL);

        return celestialBodies.registryKeySet().stream().map(celestialBodies::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(body -> body.value().parent().isPresent())
                .filter(child -> celestialBody.typeHolder.unwrapKey()
                        .filter(bodyId -> child.value().parent().filter(celestialHolder -> bodyId.equals(celestialHolder.getKey())).isPresent())
                        .isPresent())
                .map(worldCelestials::get)
                .toList();
    }

    public static Collection<CelestialBodyInstance> getSiblings(RegistryAccess access, CelestialBodyInstance celestialBody, WorldCelestials worldCelestials) {
        Registry<Celestial> celestialBodies = access.lookupOrThrow(GalacticraftRegistries.Keys.CELESTIAL);

        return celestialBodies.registryKeySet().stream().map(celestialBodies::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(body -> body.value().parent().isPresent())
                .map(worldCelestials::get)
                .filter(body -> haveSameParent(celestialBody, body, worldCelestials))
                .toList();
    }

    public static CelestialBodyInstance getLastParent(CelestialBodyInstance child, WorldCelestials worldCelestials) {
        CelestialBodyInstance parent = child.parent(worldCelestials);

        if (parent.isEmpty()) {
            return child;
        }

        CelestialBodyInstance nextParent = parent.parent(worldCelestials);

        if (!nextParent.isEmpty()) {
            return getLastParent(parent, worldCelestials);
        }

        return parent;
    }

    public static @Nullable CelestialBodyInstance getStar(CelestialBodyInstance body, WorldCelestials worldCelestials) {
        CelestialBodyInstance solarSystem = getLastParent(body, worldCelestials);

        if (solarSystem.typeHolder.value() instanceof SolarSystem system) {
            return worldCelestials.get(system.getStar());
        }

        return null;
    }

    private static boolean haveSameParent(CelestialBodyInstance first, CelestialBodyInstance second, WorldCelestials worldCelestials) {
        if (first.parent(worldCelestials).isEmpty() || second.parent(worldCelestials).isEmpty()) {
            return false;
        }

        return first.parent(worldCelestials).is(second.parent(worldCelestials));
    }
}
