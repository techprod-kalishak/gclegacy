/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.config;

import io.kalishak.galacticraftlegacy.config.values.EnergyUnit;
import io.kalishak.galacticraftlegacy.config.values.OxygenTankPosition;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue MORE_STARS = BUILDER
            .push("environment")
            .comment("Setting this to false will revert night skies back to default Minecraft star count")
            .translation("galacticraftlegacy.configgui.client.more_stars")
            .define("enableMoreStars", true);
    public static final ModConfigSpec.BooleanValue DISABLE_VEHICLE_THIRD_PERSON_VIEW = BUILDER
            .comment("If you're using this mod in virtual reality, or if you don't want the camera changes when entering a Galacticraft vehicle, set this to true.")
            .translation("galacticraftlegacy.configgui.client.disable_vehicle_tpv")
            .define("disableVehicleThirdPersonView", false);

    public static final ModConfigSpec.EnumValue<EnergyUnit> ENERGY_UNIT = BUILDER
            .pop()
            .push("gui")
            .comment("Used energy unit to represent used energy values")
            .translation("galacticraftlegacy.configgui.client.energy_unit")
            .defineEnum("energyUnit", EnergyUnit.GIGA_JOULES);
    public static final ModConfigSpec.EnumValue<OxygenTankPosition> OXYGEN_TANKS_POSITION = BUILDER
            .comment("This will move the Oxygen Indicator to the desired position.")
            .translation("galacticraftlegacy.configgui.client.oxygen_tanks_pos")
            .defineEnum("oxygenTanksHorizontalAlignment", OxygenTankPosition.TOP_RIGHT);
    public static final ModConfigSpec.BooleanValue ICONS_ROTATION = BUILDER
            .comment("If you have FPS problems, setting this to true will disable GC Rocket icons from rotating in GUI's")
            .translation("galacticraftlegacy.configgui.client.icons_rotation")
            .define("disableRocketIconsRotation", false);
    public static final ModConfigSpec.DoubleValue MOUSE_SENSITIVITY = BUILDER
            .comment("Increase to make the mouse drag scroll more sensitive, decrease to lower sensitivity.")
            .translation("galacticraftlegacy.configgui.client.scroll_sensitivity")
            .defineInRange("scrollSensitivity", 1.0D, 0.1D, 10.0D);
    public static final ModConfigSpec.BooleanValue INVERT_SCROLL = BUILDER
            .comment("Set to true to invert the mouse scroll feature on the galaxy map.")
            .translation("galacticraftlegacy.configgui.client.invert_scroll")
            .define("invertScroll", false);
    public static final ModConfigSpec.BooleanValue SPACE_RACE_MANAGER_POPUP = BUILDER
            .comment("Space Race Manager will show on-screen after login, if enabled.")
            .translation("galacticraftlegacy.configgui.client.space_race_popup")
            .define("spaceRacePopup", false);

    public static final ModConfigSpec SPEC = BUILDER.pop().build();
}
