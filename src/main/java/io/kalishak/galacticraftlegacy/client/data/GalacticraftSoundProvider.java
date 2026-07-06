/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.data;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.sounds.GalacticraftSounds;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class GalacticraftSoundProvider extends SoundDefinitionsProvider {
    public GalacticraftSoundProvider(PackOutput output) {
        super(output, Galacticraft.MODID);
    }

    @Override
    public void registerSounds() {
        add(GalacticraftSounds.SHUTTLE, definition().with(sound(Constants.id("shuttle/shuttle"))));
        add(GalacticraftSounds.MUSIC_SPACE_RACE, definition().with(sound(Constants.id("music/spacerace"))));
        add(GalacticraftSounds.AMBIENT_SPACE, definition().with(sound(Constants.id("ambience/scary_scape"))));
    }
}
