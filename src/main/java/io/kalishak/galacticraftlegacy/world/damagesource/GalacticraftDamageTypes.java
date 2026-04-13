package io.kalishak.galacticraftlegacy.world.damagesource;

import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.EnumExtensions;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public final class GalacticraftDamageTypes {
    public static final ResourceKey<DamageType> SUFFOCATION = Constants.key(Registries.DAMAGE_TYPE, "suffocation");
    public static final ResourceKey<DamageType> ACID_VICTIM = Constants.key(Registries.DAMAGE_TYPE, "acid_victim");
    public static final ResourceKey<DamageType> SUN_RADIATION = Constants.key(Registries.DAMAGE_TYPE, "sun_radiation");
    public static final ResourceKey<DamageType> SPACESHIP_CRASH = Constants.key(Registries.DAMAGE_TYPE, "spaceship_crash");

    public static void bootstrap(BootstrapContext<DamageType> cxt) {
        cxt.register(
                SUFFOCATION,
                new DamageType(
                        "galacticraft.suffocation",
                        EnumExtensions.DAMAGE_SCALING_BY_CELESTIAL_BODY.getValue(),
                        1.0F
                )
        );
        cxt.register(
                ACID_VICTIM,
                new DamageType(
                        "galacticraft.acidVictim",
                        DamageScaling.ALWAYS,
                        0.1F
                )
        );
        cxt.register(
                SUN_RADIATION,
                new DamageType(
                        "galacticraft.sunRadiation",
                        EnumExtensions.DAMAGE_SCALING_BY_CELESTIAL_BODY.getValue(),
                        0.2F,
                        DamageEffects.DROWNING
                )
        );
        cxt.register(
                SPACESHIP_CRASH,
                new DamageType(
                        "galacticraft.spaceship_crash",
                        DamageScaling.NEVER,
                        2.0F
                )
        );
    }
}
