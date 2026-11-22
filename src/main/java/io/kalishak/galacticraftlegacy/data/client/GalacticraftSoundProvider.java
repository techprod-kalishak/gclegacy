/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data.client;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.sounds.GalacticraftSounds;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

import java.util.function.UnaryOperator;

public class GalacticraftSoundProvider extends SoundDefinitionsProvider {
    public GalacticraftSoundProvider(PackOutput output) {
        super(output, Galacticraft.MODID);
    }

    @Override
    public void registerSounds() {
        add(GalacticraftSounds.SHUTTLE);
        add(GalacticraftSounds.MUSIC_SPACE_RACE);
        add(GalacticraftSounds.AMBIENT_SPACE);

        add(GalacticraftSounds.BOSS_LAUGH, definition -> definition.subtitle(GalacticraftComponents.SUBTITLE_BOSS_LAUGH.asString()));
        add(GalacticraftSounds.BOSS_HURT, definition -> definition.subtitle(GalacticraftComponents.SUBTITLE_BOSS_HURT.asString()));
        add(GalacticraftSounds.BOSS_DEATH, definition -> definition.subtitle(GalacticraftComponents.SUBTITLE_BOSS_DEATH.asString()));
    }

    private void add(Holder<SoundEvent> soundEvent, UnaryOperator<SoundDefinition> definition) {
        add(soundEvent, definition.apply(fromHolder(soundEvent)));
    }

    private void add(Holder<SoundEvent> soundEvent) {
        add(soundEvent, fromHolder(soundEvent));
    }

    private static SoundDefinition fromHolder(Holder<SoundEvent> eventHolder) {
        return definition().with(sound(eventHolder.value().location().withPath(s -> s.replace('.', '/'))));
    }
}
