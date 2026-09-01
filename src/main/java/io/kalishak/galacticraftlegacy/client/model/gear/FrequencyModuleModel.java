package io.kalishak.galacticraftlegacy.client.model.gear;

import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

public class FrequencyModuleModel {
    public static final Identifier ID = Constants.id("entity/frequency_module");
    public static final Identifier RADAR_ID = Constants.id("entity/frequency_module_radar");
    public static final StandaloneModelKey<QuadCollection> BASE_MODEL_KEY = new StandaloneModelKey<>(ID::toString);
    public static final StandaloneModelKey<QuadCollection> RADAR_MODEL_KEY = new StandaloneModelKey<>(RADAR_ID::toString);
}
