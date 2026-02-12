package io.kalishak.galacticraftlegacy.world.entity;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GalacticraftEntityType {
    private static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<FallingParachest>> FALLING_PARACHEST = REGISTRY.register(
            "falling_parachest",
            () -> EntityType.Builder.of(FallingParachest::new, MobCategory.MISC)
                    .noLootTable()
                    .canSpawnFarFromPlayer()
                    .build(Constants.key(Registries.ENTITY_TYPE, "falling_parachest"))
    );
    public static final DeferredHolder<EntityType<?>, EntityType<Flag>> FLAG = REGISTRY.register(
            "flag",
            () -> EntityType.Builder.<Flag>of(Flag::new, MobCategory.MISC)
                    .noLootTable()
                    .noSummon()
                    .sized(0.4F, 3.0F)
                    .build(Constants.key(Registries.ENTITY_TYPE, "flag"))
    );
    public static final DeferredHolder<EntityType<?>, EntityType<SchematicEntity>> SCHEMATIC = REGISTRY.register(
            "schematic",
            () -> EntityType.Builder.<SchematicEntity>of(SchematicEntity::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(Constants.key(Registries.ENTITY_TYPE, "schematic"))
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
        bus.addListener(GalacticraftEntityType::registerCapabilities);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        FallingParachest.registerCapabilities(event);
    }
}
