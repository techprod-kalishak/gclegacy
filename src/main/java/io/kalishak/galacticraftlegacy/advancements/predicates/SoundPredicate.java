/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.advancements.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.UnaryOperator;

public record SoundPredicate(Optional<HolderSet<SoundEvent>> soundEvents, LocationPredicate location) implements BiPredicate<Holder<SoundEvent>, ServerPlayer> {
    public static final Codec<SoundPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.SOUND_EVENT).optionalFieldOf("sound_events").forGetter(SoundPredicate::soundEvents),
            LocationPredicate.CODEC.fieldOf("location").forGetter(SoundPredicate::location)
    ).apply(instance, SoundPredicate::new));

    @Override
    public boolean test(Holder<SoundEvent> soundEvent, ServerPlayer entity) {
        if (this.location.matches(entity.level(), entity.getX(), entity.getY(), entity.getZ())) {
            return this.soundEvents.isPresent() && this.soundEvents.get().contains(soundEvent);
        }

        return false;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static class Builder {
        private Optional<HolderSet<SoundEvent>> soundEvents = Optional.empty();
        private LocationPredicate location = LocationPredicate.Builder.location().build();

        private Builder() {

        }

        public static Builder soundEvent() {
            return new Builder();
        }

        @SafeVarargs
        public final Builder of(HolderGetter<SoundEvent> lookup, ResourceKey<SoundEvent>... sounds) {
            this.soundEvents = Optional.of(HolderSet.direct(Arrays.stream(sounds).map(lookup::getOrThrow).toList()));
            return this;
        }

        public final Builder ofSingle(HolderGetter<SoundEvent> lookup, ResourceKey<SoundEvent> sound) {
            this.soundEvents = Optional.of(HolderSet.direct(lookup.getOrThrow(sound)));
            return this;
        }

        public Builder atLocation(LocationPredicate location) {
            this.location = location;
            return this;
        }

        public Builder withLocation(UnaryOperator<LocationPredicate.Builder> locationBuilder) {
            return atLocation(locationBuilder.apply(LocationPredicate.Builder.location()).build());
        }

        public SoundPredicate build() {
            return new SoundPredicate(this.soundEvents, this.location);
        }
    }
}
