package io.kalishak.galacticraftlegacy.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.kalishak.galacticraftlegacy.world.entity.npc.villager.AlienVillager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ShowAlienTradesToPlayer extends Behavior<AlienVillager> {
    private static final int MAX_LOOK_TIME = 900;
    private static final int STARTING_LOOK_TIME = 40;
    private @Nullable ItemStack playerItemStack;
    private final List<ItemStack> displayItems = Lists.newArrayList();
    private int cycleCounter;
    private int displayIndex;
    private int lookTime;

    public ShowAlienTradesToPlayer(int minDuration, int maxDuration) {
        super(ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_PRESENT), minDuration, maxDuration);
    }

    @Override
    public boolean checkExtraStartConditions(ServerLevel level, AlienVillager body) {
        Brain<?> brain = body.getBrain();

        if (brain.getMemory(MemoryModuleType.INTERACTION_TARGET).isEmpty()) {
            return false;
        }

        LivingEntity target = brain.getMemory(MemoryModuleType.INTERACTION_TARGET).get();

        return target.is(EntityTypes.PLAYER) && body.isAlive() && target.isAlive() && !body.isBaby() && body.distanceToSqr(target) <= 17.0;
    }

    @Override
    public boolean canStillUse(ServerLevel level, AlienVillager body, long timestamp) {
        return checkExtraStartConditions(level, body) && this.lookTime > 0 && body.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).isPresent();
    }

    @Override
    public void start(ServerLevel level, AlienVillager body, long timestamp) {
        super.start(level, body, timestamp);
        this.lookAtTarget(body);
        this.cycleCounter = 0;
        this.displayIndex = 0;
        this.lookTime = 40;
    }

    @Override
    public void tick(ServerLevel level, AlienVillager body, long timestamp) {
        LivingEntity target = this.lookAtTarget(body);
        findItemsToDisplay(target, body);

        if (!this.displayItems.isEmpty()) {
            this.displayCyclingItems(body);
        } else {
            clearHeldItem(body);
            this.lookTime = Math.min(this.lookTime, 40);
        }

        this.lookTime--;
    }

    @Override
    public void stop(ServerLevel level, AlienVillager body, long timestamp) {
        super.stop(level, body, timestamp);

        body.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
        clearHeldItem(body);
        this.playerItemStack = null;
    }

    private void findItemsToDisplay(LivingEntity player, AlienVillager villager) {
        boolean changed = false;
        ItemStack currentPlayerItemStack = player.getMainHandItem();

        if (this.playerItemStack == null || !ItemStack.isSameItem(this.playerItemStack, currentPlayerItemStack)) {
            this.playerItemStack = currentPlayerItemStack;
            changed = true;
            this.displayItems.clear();
        }

        if (changed && !this.playerItemStack.isEmpty()) {
            updateDisplayItems(villager);

            if (!this.displayItems.isEmpty()) {
                this.lookTime = 900;
                displayFirstItem(villager);
            }
        }
    }

    private void displayFirstItem(AlienVillager villager) {
        displayAsHeldItem(villager, this.displayItems.get(0));
    }

    private void updateDisplayItems(AlienVillager villager) {
        for (MerchantOffer offer : villager.getOffers()) {
            if (!offer.isOutOfStock() && playerItemStackMatchesCostOfOffer(offer)) {
                this.displayItems.add(offer.assemble());
            }
        }
    }

    private boolean playerItemStackMatchesCostOfOffer(MerchantOffer offer) {
        return this.playerItemStack != null && (ItemStack.isSameItem(this.playerItemStack, offer.getCostA()) || ItemStack.isSameItem(this.playerItemStack, offer.getCostB()));
    }

    private static void clearHeldItem(AlienVillager body) {
        body.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        body.setDropChance(EquipmentSlot.MAINHAND, 0.085F);
    }

    private static void displayAsHeldItem(AlienVillager body, ItemStack itemStack) {
        body.setItemSlot(EquipmentSlot.MAINHAND, itemStack);
        body.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    private LivingEntity lookAtTarget(AlienVillager myBody) {
        Brain<?> brain = myBody.getBrain();
        LivingEntity target = brain.getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        return target;
    }

    private void displayCyclingItems(AlienVillager villager) {
        if (this.displayItems.size() >= 2 && ++this.cycleCounter >= 40) {
            this.displayIndex++;
            this.cycleCounter = 0;
            if (this.displayIndex > this.displayItems.size() - 1) {
                this.displayIndex = 0;
            }

            displayAsHeldItem(villager, this.displayItems.get(this.displayIndex));
        }
    }
}
