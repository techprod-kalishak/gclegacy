package io.kalishak.galacticraftlegacy.client.model;

import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.obj.ObjGeometry;
import net.neoforged.neoforge.client.model.obj.ObjModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

public class GalacticraftObjModelKeys {
    public static final Identifier TELEMETRY_MODULE_ID = Constants.id("telemetry_module");
    public static final StandaloneModelKey<ObjModel> TELEMETRY_MODULE = new StandaloneModelKey<>(TELEMETRY_MODULE_ID::toString);
    public static final ObjGeometry.Settings TELEMETRY_SETTINGS = new ObjGeometry.Settings(
            TELEMETRY_MODULE_ID.withPrefix("models/"),
            false,
            true,
            false,
            false,
            null
    );
}
