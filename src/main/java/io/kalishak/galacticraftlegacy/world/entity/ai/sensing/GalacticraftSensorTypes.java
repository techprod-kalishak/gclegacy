package io.kalishak.galacticraftlegacy.world.entity.ai.sensing;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GalacticraftSensorTypes {
    private static final DeferredRegister<SensorType<?>> REGISTRY = DeferredRegister.create(Registries.SENSOR_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<SensorType<?>, SensorType<AlienVillagerBabiesSensor>> ALIEN_VILLAGER_BABIES = REGISTRY.register(
            "alien_villager_babies",
            () -> new SensorType<>(AlienVillagerBabiesSensor::new)
    );
    public static final DeferredHolder<SensorType<?>, SensorType<AlienVillagerHostilesSensor>> ALIEN_VILLAGER_HOSTILES = REGISTRY.register(
            "alien_villager_hostiles",
            () -> new SensorType<>(AlienVillagerHostilesSensor::new)
    );
    public static final DeferredHolder<SensorType<?>, SensorType<SecondaryAlienPoiSensor>> SECONDARY_ALIEN_POIS = REGISTRY.register(
            "secondary_alien_pois",
            () -> new SensorType<>(SecondaryAlienPoiSensor::new)
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
