package io.kalishak.galacticraftlegacy.client.data.models.model;

import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;

public class GalacticraftTexturedModel {
    public static final TexturedModel.Provider SIMPLE_MACHINE = TexturedModel.createDefault(GalacticraftTextureMapping::simpleMachine, ModelTemplates.CUBE);
}
