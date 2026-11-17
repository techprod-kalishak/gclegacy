package io.kalishak.galacticraftlegacy.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.kalishak.galacticraftlegacy.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.entity.npc.villager.AlienVillager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TradeWithAlienVillager extends Behavior<AlienVillager> {
    private Set<Item> trades = ImmutableSet.of();

    public TradeWithAlienVillager() {
        super(ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AlienVillager body) {
        return BehaviorUtils.targetIsValid(body.getBrain(), MemoryModuleType.INTERACTION_TARGET, EntityTypes.VILLAGER);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, AlienVillager body, long timestamp) {
        return checkExtraStartConditions(level, body);
    }

    @Override
    protected void start(ServerLevel level, AlienVillager myBody, long timestamp) {
        AlienVillager target = (AlienVillager) myBody.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        BehaviorUtils.lockGazeAndWalkToEachOther(myBody, target, 0.5F, 2);
        this.trades = figureOutWhatIAmWillingToTrade(myBody, target);
    }

    @Override
    protected void tick(ServerLevel level, AlienVillager body, long timestamp) {
        AlienVillager target = (AlienVillager)body.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();

        if (!(body.distanceToSqr(target) > 5.0)) {
            BehaviorUtils.lockGazeAndWalkToEachOther(body, target, 0.5F, 2);
            body.gossip(level, target, timestamp);

            if (body.hasExcessFood() && (target.wantsMoreFood())) {
                throwHalfStack(body, itemStack -> itemStack.has(GalacticraftDataComponents.ALIEN_VILLAGER_FOOD), target);
            }

            if (!this.trades.isEmpty() && body.getInventory().hasAnyOf(this.trades)) {
                throwHalfStack(body, itemStack -> this.trades.contains(itemStack.getItem()), target);
            }
        }
    }

    @Override
    protected void stop(ServerLevel level, AlienVillager body, long timestamp) {
        body.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
    }

    private static Set<Item> figureOutWhatIAmWillingToTrade(AlienVillager myBody, AlienVillager target) {
        ImmutableSet<Item> targetItems = target.getVillagerData().profession().value().requestedItems();
        ImmutableSet<Item> selfItems = myBody.getVillagerData().profession().value().requestedItems();
        return targetItems.stream().filter(entry -> !selfItems.contains(entry)).collect(Collectors.toSet());
    }

    private static void throwHalfStack(AlienVillager villager, Predicate<ItemStack> predicate, LivingEntity target) {
        SimpleContainer inventory = villager.getInventory();
        ItemStack toThrow = ItemStack.EMPTY;
        int i = 0;

        while (i < inventory.getContainerSize()) {
            ItemStack itemStack;
            int count;
            label28: {
                itemStack = inventory.getItem(i);
                if (!itemStack.isEmpty() && predicate.test(itemStack)) {
                    if (itemStack.getCount() > itemStack.getMaxStackSize() / 2) {
                        count = itemStack.getCount() / 2;
                        break label28;
                    }

                    if (itemStack.getCount() > 24) {
                        count = itemStack.getCount() - 24;
                        break label28;
                    }
                }

                i++;
                continue;
            }

            toThrow = itemStack.split(count);
            break;
        }

        if (!toThrow.isEmpty()) {
            BehaviorUtils.throwItem(villager, toThrow, target.position());
        }
    }
}
