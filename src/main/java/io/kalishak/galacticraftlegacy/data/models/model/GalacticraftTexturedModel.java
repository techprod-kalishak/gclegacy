package io.kalishak.galacticraftlegacy.data.models.model;

import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;

public class GalacticraftTexturedModel {
    public static final TexturedModel.Provider COAL_GENERATOR = TexturedModel.createDefault(GalacticraftTextureMapping::coalGenerator, ModelTemplates.CUBE);
}
