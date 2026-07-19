package io.kalishak.galacticraftlegacy.client.data;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.world.level.GalacticraftParticleTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.data.ParticleDescriptionProvider;

public class GalacticraftParticleProvider extends ParticleDescriptionProvider {
    public GalacticraftParticleProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void addDescriptions() {
        spriteSet(GalacticraftParticleTypes.DRIPPING_OIL.get(), Identifier.withDefaultNamespace("drip_hang"));
        spriteSet(GalacticraftParticleTypes.SPARKS.get(), Constants.id("sparks"));
    }
}
