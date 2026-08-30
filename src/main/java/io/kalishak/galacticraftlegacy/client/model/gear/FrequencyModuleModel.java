package io.kalishak.galacticraftlegacy.client.model.gear;

import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

import java.util.function.BiConsumer;

public class FrequencyModuleModel {
    private static final Identifier ID = Constants.id("entity/frequency_module");
    private static final Identifier RADAR_ID = Constants.id("entity/frequency_module_radar");
    public static final StandaloneModelKey<QuadCollection> BASE_MODEL_KEY = new StandaloneModelKey<>(ID::toString);
    public static final StandaloneModelKey<QuadCollection> RADAR_MODEL_KEY = new StandaloneModelKey<>(RADAR_ID::toString);
    public static final SimpleUnbakedStandaloneModel<QuadCollection> UNBAKED_MODEL = SimpleUnbakedStandaloneModel.quadCollection(ID);
    public static final SimpleUnbakedStandaloneModel<QuadCollection> RADAR_UNBAKED_MODEL = SimpleUnbakedStandaloneModel.quadCollection(RADAR_ID);

    public static void load(BiConsumer<StandaloneModelKey<QuadCollection>, SimpleUnbakedStandaloneModel<QuadCollection>> biConsumer) {
        biConsumer.accept(BASE_MODEL_KEY, UNBAKED_MODEL);
        biConsumer.accept(RADAR_MODEL_KEY, RADAR_UNBAKED_MODEL);
    }
}
