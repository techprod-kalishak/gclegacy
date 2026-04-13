package io.kalishak.galacticraftlegacy.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.attachment.AttachmentHelper;
import io.kalishak.galacticraftlegacy.registry.ChecklistEntry;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;

import java.util.Map;
import java.util.Optional;

public class ChecklistDoneTrigger extends SimpleCriterionTrigger<ChecklistDoneTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ResourceKey<ChecklistEntry> entry) {
        SpaceGearEquipment provider = AttachmentHelper.getGearInventory(player).getGearEquipment();
        Optional<ChecklistEntry> checklistEntry = player.registryAccess()
                .lookup(GalacticraftRegistries.Keys.CHECKLIST)
                .flatMap(registry -> registry.get(entry))
                .map(Holder.Reference::value);

        if (!ResourceHandlerUtil.isEmpty(provider) && checklistEntry.isPresent()) {
            trigger(player, triggerInstance -> triggerInstance.matches(provider, checklistEntry.get()));
        }
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, ResourceKey<ChecklistEntry> entry) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                ResourceKey.codec(GalacticraftRegistries.Keys.CHECKLIST).fieldOf("entry").forGetter(TriggerInstance::entry)
        ).apply(instance, ChecklistDoneTrigger.TriggerInstance::new));

        public static Criterion<TriggerInstance> hasChecklistCompleted(ResourceKey<ChecklistEntry> entry) {
            return GalacticraftCriteriaTriggers.CHECKLIST_CHECK.get().createCriterion(new TriggerInstance(Optional.empty(), entry));
        }

        public boolean matches(SpaceGearEquipment gearInventory, ChecklistEntry entry) {
            for (Map.Entry<GearEquipmentSlot, ItemPredicate> mapEntry : entry.requiredItems().entrySet()) {
                ItemStack stack = gearInventory.get(mapEntry.getKey());

                if (stack.isEmpty() || !mapEntry.getValue().test(stack)) {
                    return false;
                }
            }

            return true;
        }
    }
}
