package io.kalishak.galacticraftlegacy.client.data.models;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.aunified.data.model.CustomModelGenerator;
import net.minecraft.data.PackOutput;

public class GalacticraftCustomModelProvider extends CustomModelGenerator {
    public GalacticraftCustomModelProvider(PackOutput output) {
        super(output, Galacticraft.MODID);
    }

    @Override
    public void generateModels() {
        obj(prefixed("frequency_module"))
                .flipV(true)
                .visibility("Radar", false);
        obj(prefixed("frequency_module_radar"))
                .flipV(true);
        obj(prefixed("thrown_meteor_chunk"))
                .flipV(true);
    }
}
