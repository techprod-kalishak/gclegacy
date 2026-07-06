/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.model;

import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.obj.ObjGeometry;
import net.neoforged.neoforge.client.model.obj.ObjLoader;
import net.neoforged.neoforge.client.model.obj.ObjModel;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

import java.util.Map;

@EventBusSubscriber(modid = Galacticraft.MODID, value = Dist.CLIENT)
public class GalacticraftObjModelKeys {
    /**
     * 32 Tin Ingot
     * 16 Aluminum Ingot
     * 1 Advanced Wafer
     * 24 Iron Ingot
     */

    private static final Identifier TELEMETRY_MODULE = Constants.id("telemetry_module");
    private static final Identifier THROWN_METEOR_CHUNK = Constants.id("thrown_meteor_chunk");
    public static final StandaloneModelKey<ObjModel> TELEMETRY_MODULE_KEY = new StandaloneModelKey<>(TELEMETRY_MODULE::toString);
    public static final StandaloneModelKey<ObjModel> THROWN_METEOR_CHUNK_KEY = new StandaloneModelKey<>(THROWN_METEOR_CHUNK::toString);
    public static final ObjGeometry.Settings TELEMETRY_SETTINGS = new ObjGeometry.Settings(
            TELEMETRY_MODULE.withPrefix("entity/"),
            false,
            true,
            false,
            false,
            null
    );
    public static final ObjGeometry.Settings THROWN_METEOR_SETTINGS = new ObjGeometry.Settings(
            TELEMETRY_MODULE.withPrefix("entity/"),
            false,
            true,
            false,
            false,
            null
    );
    public static final SimpleUnbakedStandaloneModel<ObjModel> TELEMETRY_MODEL_BAKER = new SimpleUnbakedStandaloneModel<>(
            TELEMETRY_MODULE,
            (_, _, _) -> new ObjModel(
                    new StandardModelParameters(
                            null,
                            new TextureSlots.Data.Builder()
                                    .addTexture(
                                            "surface",
                                            new Material(TELEMETRY_MODULE.withPath(path -> "textures/model/" + path + ".png"))
                                    ).build(),
                            null,
                            true,
                            UnbakedModel.GuiLight.FRONT,
                            null,
                            Map.of()

            ), ObjLoader.INSTANCE.loadGeometry(TELEMETRY_SETTINGS))
    );
    public static final SimpleUnbakedStandaloneModel<ObjModel> THROWN_METEOR_BAKER = new SimpleUnbakedStandaloneModel<>(
            THROWN_METEOR_CHUNK,
            (_, _, _) -> new ObjModel(
                    new StandardModelParameters(
                            null,
                            new TextureSlots.Data.Builder()
                                    .addTexture(
                                            "surface",
                                            new Material(THROWN_METEOR_CHUNK.withPath(path -> "textures/model/" + path + ".png"))
                                    )
                                    .build(),
                            null,
                            true,
                            UnbakedModel.GuiLight.FRONT,
                            null,
                            Map.of()
                    ), ObjLoader.INSTANCE.loadGeometry(THROWN_METEOR_SETTINGS)
            )
    );

    @SubscribeEvent
    public static void modelLoad(ModelEvent.RegisterStandalone event) {
        event.register(TELEMETRY_MODULE_KEY, TELEMETRY_MODEL_BAKER);
        event.register(THROWN_METEOR_CHUNK_KEY, TELEMETRY_MODEL_BAKER);
    }
}
