package io.kalishak.galacticraftlegacy.world.attribute;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.world.level.EarthPhase;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.attribute.AttributeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GalacticraftAttributeTypes {
    public static final DeferredRegister<AttributeType<?>> REGISTRY = DeferredRegister.create(Registries.ATTRIBUTE_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<AttributeType<?>, AttributeType<EarthPhase>> EARTH_PHASE = REGISTRY.register(
            "earth_phase",
            () -> AttributeType.ofNotInterpolated(EarthPhase.CODEC)
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
