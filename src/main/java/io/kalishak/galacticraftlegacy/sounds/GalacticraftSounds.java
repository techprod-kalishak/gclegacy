/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.sounds;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.stream.Stream;

public final class GalacticraftSounds {
    private static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, Galacticraft.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> SHUTTLE = REGISTRY.register("shuttle", () -> SoundEvent.createVariableRangeEvent(Constants.id("shuttle.shuttle")));
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_SPACE_RACE = REGISTRY.register("spacerace", () -> SoundEvent.createVariableRangeEvent(Constants.id("music.spacerace")));
    public static final DeferredHolder<SoundEvent, SoundEvent> AMBIENT_SPACE = REGISTRY.register("scary_scape", () -> SoundEvent.createVariableRangeEvent(Constants.id("ambience.scary_scape")));

    public static final DeferredHolder<SoundEvent, SoundEvent> BOSS_LAUGH = REGISTRY.register("boss_laugh", () -> SoundEvent.createVariableRangeEvent(Constants.id("entity.boss.laugh")));
    public static final DeferredHolder<SoundEvent, SoundEvent> BOSS_DEATH = REGISTRY.register("boss_death", () -> SoundEvent.createVariableRangeEvent(Constants.id("entity.boss.death")));
    public static final DeferredHolder<SoundEvent, SoundEvent> BOSS_HURT = REGISTRY.register("boss_hurt", () -> SoundEvent.createVariableRangeEvent(Constants.id("entity.boss.hurt")));

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }

    public static Stream<SoundEvent> sounds() {
        return REGISTRY.getEntries().stream().map(DeferredHolder::get);
    }
}
