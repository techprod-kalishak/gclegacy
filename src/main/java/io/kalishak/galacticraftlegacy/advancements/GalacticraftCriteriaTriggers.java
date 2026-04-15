package io.kalishak.galacticraftlegacy.advancements;

import io.kalishak.galacticraftlegacy.Galacticraft;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GalacticraftCriteriaTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> REGISTRY = DeferredRegister.create(Registries.TRIGGER_TYPE, Galacticraft.MODID);

    public static final DeferredHolder<CriterionTrigger<?>, GearEquippedTrigger> GEAR_EQUIPPED = REGISTRY.register(
            "gear_equipped",
            GearEquippedTrigger::new
    );
    public static final DeferredHolder<CriterionTrigger<?>, ChecklistDoneTrigger> CHECKLIST_CHECK = REGISTRY.register(
            "checklist_check",
            ChecklistDoneTrigger::new
    );
    public static final DeferredHolder<CriterionTrigger<?>, BedUsedInSpaceTrigger> BED_USED_IN_SPACE = REGISTRY.register(
            "bed_used_in_space",
            BedUsedInSpaceTrigger::new
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
