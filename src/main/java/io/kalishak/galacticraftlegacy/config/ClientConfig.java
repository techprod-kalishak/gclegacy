/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.config;

import io.kalishak.galacticraftlegacy.config.values.EnergyUnit;
import io.kalishak.galacticraftlegacy.config.values.OxygenTankPosition;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue MORE_STARS = BUILDER
            .push("environment")
            .comment("Setting this to false will revert night skies back to default Minecraft star count")
            .translation(GalacticraftComponents.CONFIG_MORE_STARS)
            .define("enableMoreStars", true);
    public static final ModConfigSpec.BooleanValue DISABLE_VEHICLE_THIRD_PERSON_VIEW = BUILDER
            .comment("If you're using this mod in virtual reality, or if you don't want the camera changes when entering a Galacticraft vehicle, set this to true.")
            .translation(GalacticraftComponents.CONFIG_DISABLE_THIRD_PERSON_VIEW)
            .define("disableVehicleThirdPersonView", false);

    public static final ModConfigSpec.EnumValue<EnergyUnit> ENERGY_UNIT = BUILDER
            .pop()
            .push("gui")
            .comment("Used energy unit to represent used energy values")
            .translation(GalacticraftComponents.CONFIG_ENERGY_UNIT)
            .defineEnum("energyUnit", EnergyUnit.GIGA_JOULES);
    public static final ModConfigSpec.EnumValue<OxygenTankPosition> OXYGEN_TANKS_POSITION = BUILDER
            .comment("This will move the Oxygen Indicator to the desired position.")
            .translation(GalacticraftComponents.CONFIG_OXYGEN_TANK_POS)
            .defineEnum("oxygenTanksHorizontalAlignment", OxygenTankPosition.TOP_RIGHT);
    public static final ModConfigSpec.BooleanValue ICONS_ROTATION = BUILDER
            .comment("If you have FPS problems, setting this to true will disable GC Rocket icons from rotating in GUI's")
            .translation(GalacticraftComponents.CONFIG_ICON_ROTATIONS)
            .define("disableRocketIconsRotation", false);
    public static final ModConfigSpec.DoubleValue MOUSE_SENSITIVITY = BUILDER
            .comment("Increase to make the mouse drag scroll more sensitive, decrease to lower sensitivity.")
            .translation(GalacticraftComponents.CONFIG_SCROLL_SENSITIVITY)
            .defineInRange("scrollSensitivity", 1.0D, 0.1D, 10.0D);
    public static final ModConfigSpec.BooleanValue INVERT_SCROLL = BUILDER
            .comment("Set to true to invert the mouse scroll feature on the galaxy map.")
            .translation(GalacticraftComponents.CONFIG_INVERT_SCROLL)
            .define("invertScroll", false);
    public static final ModConfigSpec.BooleanValue SPACE_RACE_MANAGER_POPUP = BUILDER
            .comment("Space Race Manager will show on-screen after login, if enabled.")
            .translation(GalacticraftComponents.CONFIG_SPACE_RACE_POPUP)
            .define("spaceRacePopup", false);

    public static final ModConfigSpec SPEC = BUILDER.pop().build();
}
