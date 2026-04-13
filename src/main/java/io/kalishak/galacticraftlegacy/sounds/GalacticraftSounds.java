package io.kalishak.galacticraftlegacy.sounds;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.stream.Stream;

public final class GalacticraftSounds {
    private static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, Galacticraft.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> SHUTTLE = REGISTRY.register("shuttle", () -> SoundEvent.createVariableRangeEvent(Constants.id("shuttle")));
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_SPACE_RACE = REGISTRY.register("spacerace", () -> SoundEvent.createVariableRangeEvent(Constants.id("spacerace")));

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }

    public static Stream<SoundEvent> sounds() {
        return REGISTRY.getEntries().stream().map(DeferredHolder::get);
    }
}
