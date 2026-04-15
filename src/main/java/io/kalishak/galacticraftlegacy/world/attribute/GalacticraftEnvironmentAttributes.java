package io.kalishak.galacticraftlegacy.world.attribute;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.world.level.EarthPhase;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.attribute.AttributeType;
import net.minecraft.world.attribute.AttributeTypes;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GalacticraftEnvironmentAttributes {
    public static final DeferredRegister<EnvironmentAttribute<?>> REGISTRY = DeferredRegister.create(Registries.ENVIRONMENT_ATTRIBUTE, Galacticraft.MODID);

    public static final DeferredHolder<EnvironmentAttribute<?>, EnvironmentAttribute<Float>> EARTH_ANGLE = REGISTRY.register(
            "visual/earth_angle",
            () -> EnvironmentAttribute.builder(AttributeTypes.ANGLE_DEGREES)
                    .defaultValue(0.0F)
                    .syncable()
                    .build()
    );
    public static final DeferredHolder<EnvironmentAttribute<?>, EnvironmentAttribute<EarthPhase>> EARTH_PHASE = REGISTRY.register(
            "visual/earth_phase",
            () -> EnvironmentAttribute.builder(GalacticraftAttributeTypes.EARTH_PHASE)
                    .defaultValue(EarthPhase.FULL_EARTH)
                    .syncable()
                    .build()
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
