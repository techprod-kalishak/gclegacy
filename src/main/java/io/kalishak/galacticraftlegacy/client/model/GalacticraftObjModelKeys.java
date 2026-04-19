/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.model;

import io.kalishak.galacticraftlegacy.Constants;
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
    private static final Identifier TELEMETRY_MODULE = Constants.id("telemetry_module");
    public static final StandaloneModelKey<ObjModel> TELEMETRY_MODULE_KEY = new StandaloneModelKey<>(TELEMETRY_MODULE::toString);
    public static final ObjGeometry.Settings TELEMETRY_SETTINGS = new ObjGeometry.Settings(
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

            ), ObjLoader.INSTANCE.loadGeometry(TELEMETRY_SETTINGS)));

    @SubscribeEvent
    public static void modelLoad(ModelEvent.RegisterStandalone event) {
        event.register(TELEMETRY_MODULE_KEY, TELEMETRY_MODEL_BAKER);
    }
}
