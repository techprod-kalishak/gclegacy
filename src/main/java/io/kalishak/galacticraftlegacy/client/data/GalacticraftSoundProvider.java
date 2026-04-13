package io.kalishak.galacticraftlegacy.client.data;

import io.kalishak.galacticraftlegacy.Constants;
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
    }
}
