package io.kalishak.galacticraftlegacy.world.entity.npc.villager;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.data.villager.GalacticraftVillagerProfession;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityTypes;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.entity.ai.attributes.GalacticraftAttributes;
import io.kalishak.galacticraftlegacy.world.entity.ai.behavior.AlienVillagerGoalPackages;
import io.kalishak.galacticraftlegacy.world.entity.ai.sensing.GalacticraftSensorTypes;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.component.AlienVillagerFood;
import io.kalishak.galacticraftlegacy.world.timeline.GalacticraftTimelines;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.npc.villager.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.food.VillagerFood;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

@ParametersAreNonnullByDefault
public class AlienVillager extends AbstractVillager implements VillagerDataHolder, ReputationEventHandler {
    private static final EntityDataAccessor<VillagerData> DATA_VILLAGER_DATA = SynchedEntityData.defineId(AlienVillager.class, EntityDataSerializers.VILLAGER_DATA);
    private static final EntityDataAccessor<Boolean> DATA_VILLAGER_DATA_FINALIZED = SynchedEntityData.defineId(AlienVillager.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.49F, 0.98F).withEyeHeight(0.63F);
    private @Nullable Player lastTradedPlayer;
    private int foodLevel = 0;
    private final GossipContainer gossips = new GossipContainer();
    private long lastGossipTime;
    private long lastGossipDecayTime = 0L;
    private int villagerXp = 0;
    private long lastRestockGameTime = 0L;
    private int numberOfRestocksToday = 0;
    private long lastRestockCheckDay;
    private static final Brain.Provider<AlienVillager> BRAIN_PROVIDER = Brain.provider(
            List.of(
                    SensorType.NEAREST_LIVING_ENTITIES,
                    SensorType.NEAREST_PLAYERS,
                    SensorType.NEAREST_ITEMS,
                    SensorType.HURT_BY,
                    GalacticraftSensorTypes.ALIEN_VILLAGER_HOSTILES.get(),
                    GalacticraftSensorTypes.ALIEN_VILLAGER_BABIES.get(),
                    GalacticraftSensorTypes.SECONDARY_ALIEN_POIS.get()
            ),
            body -> {
                Holder<VillagerProfession> profession = body.getVillagerData().profession();
                List<ActivityData<AlienVillager>> activities = new ArrayList<>();
                if (body.isBaby()) {
                    activities.add(ActivityData.create(Activity.PLAY, AlienVillagerGoalPackages.getPlayPackage(0.5F)));
                } else {
                    activities.add(
                            ActivityData.create(
                                    Activity.WORK,
                                    AlienVillagerGoalPackages.getWorkPackage(profession, 0.5F),
                                    ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT))
                            )
                    );
                }

                activities.add(ActivityData.create(Activity.CORE, AlienVillagerGoalPackages.getCorePackage(profession, 0.5F)));
                activities.add(
                        ActivityData.create(
                                Activity.MEET,
                                AlienVillagerGoalPackages.getMeetPackage(0.5F),
                                ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT))
                        )
                );
                activities.add(ActivityData.create(Activity.IDLE, AlienVillagerGoalPackages.getIdlePackage(0.5F)));
                activities.add(ActivityData.create(Activity.PANIC, AlienVillagerGoalPackages.getPanicPackage(0.5F)));
                activities.add(ActivityData.create(Activity.HIDE, AlienVillagerGoalPackages.getHidePackage(0.5F)));
                return activities;
            }
    );
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<AlienVillager, Holder<PoiType>>> POI_MEMORIES = ImmutableMap.of(
            MemoryModuleType.HOME,
            (_, poiType) -> poiType.is(PoiTypes.HOME),
            MemoryModuleType.JOB_SITE,
            (villager, poiType) -> villager.getVillagerData().profession().value().heldJobSite().test(poiType),
            MemoryModuleType.POTENTIAL_JOB_SITE,
            (_, poiType) -> VillagerProfession.ALL_ACQUIRABLE_JOBS.test(poiType),
            MemoryModuleType.MEETING_POINT,
            (_, poiType) -> poiType.is(PoiTypes.MEETING)
    );

    public AlienVillager(EntityType<? extends AlienVillager> entityType, Level level) {
        super(entityType, level);
        getNavigation().setCanOpenDoors(true);
        getNavigation().setCanFloat(true);
        getNavigation().setRequiredPathLength(48.0F);
        setCanPickUpLoot(true);
    }

    public AlienVillager(Level level) {
        this(GalacticraftEntityTypes.ALIEN_VILLAGER.get(), level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(GalacticraftAttributes.THERMAL_PROTECTION, 1.0F);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NonNull Brain<AlienVillager> getBrain() {
        return (Brain<AlienVillager>) super.getBrain();
    }

    @Override
    protected @NonNull Brain<AlienVillager> makeBrain(Brain.Packed packedBrain) {
        Brain<AlienVillager> brain = BRAIN_PROVIDER.makeBrain(this, packedBrain);
        registerBrainGoals(brain);

        return brain;
    }

    public void refreshBrain(ServerLevel level) {
        Brain<AlienVillager> oldBrain = getBrain();
        oldBrain.stopAll(level, this);
        this.brain = BRAIN_PROVIDER.makeBrain(this, oldBrain.pack());
        registerBrainGoals(getBrain());
    }

    private void registerBrainGoals(Brain<AlienVillager> brain) {
        if (this.isBaby()) {
            brain.setSchedule(EnvironmentAttributes.BABY_VILLAGER_ACTIVITY);
        } else {
            brain.setSchedule(EnvironmentAttributes.VILLAGER_ACTIVITY);
        }

        brain.updateActivityFromSchedule(level().environmentAttributes(), level().getGameTime(), position());
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        if (level() instanceof ServerLevel serverLevel) {
            refreshBrain(serverLevel);
        }
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        ProfilerFiller profiler = Profiler.get();
        profiler.push("alienVillagerBrain");
        getBrain().tick(level, this);
        profiler.pop();

        if (this.lastTradedPlayer != null) {
            level.onReputationEvent(ReputationEventType.TRADE, this.lastTradedPlayer, this);
            level.broadcastEntityEvent(this, (byte) 14);
            this.lastTradedPlayer = null;
        }

        if (!isNoAi() && this.random.nextInt(100) == 0) {
            Raid raid = level.getRaidAt(blockPosition());
            if (raid != null && raid.isActive() && !raid.isOver()) {
                level.broadcastEntityEvent(this, (byte) 42);
            }
        }

        if ((this.offers == null || this.offers.isEmpty()) && isTrading()) {
            stopTrading();
        }

        super.customServerAiStep(level);
    }

    @Override
    public void tick() {
        super.tick();

        if (getUnhappyCounter() > 0) {
            setUnhappyCounter(getUnhappyCounter() - 1);
        }

        maybeDecayGossip();
    }

    @Override
    public @NonNull InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (itemStack.is(GalacticraftItems.ALIEN_VILLAGER_SPAWN_EGG) || !isAlive() || isTrading() || isSleeping() || player.isSecondaryUseActive()) {
            return super.mobInteract(player, hand);
        }

        if (isBaby()) {
            setUnhappy();

            return InteractionResult.SUCCESS;
        }

        if (!level().isClientSide()) {
            if (!canCommunicate(player)) {
                setUnhappyCounter(40);
                makeSound(SoundEvents.VILLAGER_NO);
                player.sendSystemMessage(GalacticraftComponents.NEED_FREQUENCY_MODULE.translated());

                return InteractionResult.SUCCESS;
            }

            boolean noOffers = getOffers().isEmpty();

            if (hand == InteractionHand.MAIN_HAND) {
                if (noOffers) {
                    setUnhappy();
                }
            }

            if (noOffers) {
                return InteractionResult.CONSUME;
            }

            startTrading(player);
        }

        return InteractionResult.SUCCESS;
    }

    private void setUnhappy() {
        setUnhappyCounter(40);

        if (!level().isClientSide()) {
            makeSound(SoundEvents.VILLAGER_NO);
        }
    }

    private boolean canCommunicate(Player player) {
        SpaceGearEquipment equipment = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA).getGearEquipment();
        ItemStack frequencyModule = equipment.get(GearEquipmentSlot.FREQUENCY_MODULE);

        return !frequencyModule.isEmpty();
    }

    private void startTrading(Player player) {
        updateSpecialPrices(player);
        setTradingPlayer(player);
        openTradingScreen(player, getDisplayName(), getVillagerData().level());
    }

    @Override
    public void setTradingPlayer(@Nullable Player player) {
        boolean shouldStop = getTradingPlayer() != null && player == null;

        super.setTradingPlayer(player);

        if (shouldStop) {
            stopTrading();
        }
    }

    private void resetSpecialPrices() {
        if (!level().isClientSide()) {
            for (MerchantOffer offer : getOffers()) {
                offer.resetSpecialPriceDiff();
            }
        }
    }

    @Override
    public @NonNull EntityDimensions getDefaultDimensions(Pose pose) {
        return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    @Override
    public boolean canRestock() {
        return true;
    }

    public void restock() {
        updateDemand();

        for (MerchantOffer offer : getOffers()) {
            offer.resetUses();
        }

        Player tradingPlayer = getTradingPlayer();

        if (tradingPlayer != null) {
            updateSpecialPrices(tradingPlayer);
        }

        this.lastRestockGameTime = level().getGameTime();
        this.numberOfRestocksToday++;
    }

    private boolean needsToRestock() {
        for (MerchantOffer offer : getOffers()) {
            if (offer.needsRestock()) {
                return true;
            }
        }

        return false;
    }

    private boolean allowedToRestock() {
        return this.numberOfRestocksToday == 0 || this.numberOfRestocksToday < 2 && level().getGameTime() > this.lastRestockGameTime + 354000L; //Moon day
    }

    public boolean shouldRestock(ServerLevel level) {
        long halfDayPassedTime = this.lastRestockGameTime + 12000L;
        long gameTime = level().getGameTime();
        boolean isNewDay = gameTime > halfDayPassedTime;

        long currentDay = level()
                .registryAccess()
                .get(GalacticraftTimelines.MOON_DAY)
                .map(timeline -> timeline.value().getPeriodCount(level.clockManager()))
                .orElse(0);
        isNewDay |= this.lastRestockCheckDay > 0L && currentDay > this.lastRestockCheckDay;
        this.lastRestockCheckDay = currentDay;

        if (isNewDay) {
            this.lastRestockGameTime = gameTime;
            resetNumberOfRestocks();
        }

        return allowedToRestock() && needsToRestock();
    }

    private void catchUpDemand() {
        int missedUpdates = 2 - this.numberOfRestocksToday;

        if (missedUpdates > 0) {
            for (MerchantOffer offer : this.getOffers()) {
                offer.resetUses();
            }
        }

        for (int i = 0; i < missedUpdates; i++) {
            updateDemand();
        }

        Player tradingPlayer = getTradingPlayer();

        if (tradingPlayer != null) {
            updateSpecialPrices(tradingPlayer);
        }
    }

    private void updateDemand() {
        for (MerchantOffer offer : this.getOffers()) {
            offer.updateDemand();
        }
    }

    private void updateSpecialPrices(Player player) {
        resetSpecialPrices();

        int reputation = getPlayerReputation(player);
        MobEffectInstance heroOfTheVillage = player.getEffect(MobEffects.HERO_OF_THE_VILLAGE);
        double heroModifier = heroOfTheVillage == null ? 0.0 : 0.3F + 0.0625F * heroOfTheVillage.getAmplifier();
        MerchantOffers merchantOffers = this.getOffers();

        for (MerchantOffer offer : merchantOffers) {
            if (reputation != 0) {
                offer.addToSpecialPriceDiff(-Mth.floor(reputation * offer.getPriceMultiplier()));
            }

            if (heroModifier > 0.0) {
                int costReduction = (int) Math.floor(heroModifier * offer.getBaseCostA().getCount());

                offer.addToSpecialPriceDiff(-Math.max(costReduction, 1));
            }
        }

        Player tradingPlayer = getTradingPlayer();

        if (tradingPlayer != null && !merchantOffers.isEmpty()) {
            if (tradingPlayer.containerMenu instanceof MerchantMenu menu) {
                menu.updateSellItem();
            }

            tradingPlayer.sendMerchantOffers(
                    tradingPlayer.containerMenu.containerId,
                    merchantOffers,
                    getVillagerData().level(),
                    getVillagerXp(),
                    showProgressBar(),
                    canRestock()
            );
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_VILLAGER_DATA, createDefaultAlienVillagerData());
        entityData.define(DATA_VILLAGER_DATA_FINALIZED, false);
    }

    public static VillagerData createDefaultAlienVillagerData() {
        return new VillagerData(
                BuiltInRegistries.VILLAGER_TYPE.getOrThrow(VillagerData.DEFAULT_TYPE), BuiltInRegistries.VILLAGER_PROFESSION.getOrThrow(VillagerProfession.NONE), 1
        );
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store("VillagerData", VillagerData.CODEC, getVillagerData());
        output.putBoolean("VillagerDataFinalized", getVillagerDataFinalized());
        output.putByte("FoodLevel", (byte) this.foodLevel);
        output.store("Gossips", GossipContainer.CODEC, this.gossips);
        output.putInt("Xp", this.villagerXp);
        output.putLong("LastRestock", this.lastRestockGameTime);
        output.putLong("LastGossipDecay", this.lastGossipDecayTime);
        output.putInt("RestocksToday", this.numberOfRestocksToday);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);

        Optional<VillagerData> villagerDataOptional = input.read("VillagerData", VillagerData.CODEC);

        if (input.getBooleanOr("VillagerDataFinalized", false) || villagerDataOptional.isPresent()) {
            setVillagerDataFinalized(true);
            VillagerData villagerData = villagerDataOptional.orElseGet(Villager::createDefaultVillagerData);
            this.entityData.set(DATA_VILLAGER_DATA, villagerData);
        }

        this.foodLevel = input.getByteOr("FoodLevel", (byte) 0);
        this.gossips.clear();
        input.read("Gossips", GossipContainer.CODEC).ifPresent(this.gossips::putAll);
        this.villagerXp = input.getIntOr("Xp", 0);
        this.lastRestockGameTime = input.getLongOr("LastRestock", 0L);
        this.lastGossipDecayTime = input.getLongOr("LastGossipDecay", 0L);
        this.numberOfRestocksToday = input.getIntOr("RestocksToday", 0);

        if (level() instanceof ServerLevel serverLevel) {
            refreshBrain(serverLevel);
        }
    }

    @Override
    public void postDataManipulated() {
        Player tradingPlayer = getTradingPlayer();

        if (tradingPlayer != null) {
            updateSpecialPrices(tradingPlayer);
        }
    }

    @Override
    public boolean removeWhenFarAway(double distSqr) {
        return false;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        if (isSleeping()) {
            return null;
        } else {
            return isTrading() ? SoundEvents.VILLAGER_TRADE : SoundEvents.VILLAGER_AMBIENT;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.VILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }

    public void playWorkSound() {
        this.makeSound(getVillagerData().profession().value().workSound());
    }

    @Override
    public void setVillagerData(VillagerData data) {
        VillagerData currentData = getVillagerData();

        if (!currentData.profession().equals(data.profession())) {
            this.offers = null;
        }

        this.entityData.set(DATA_VILLAGER_DATA, data);
    }

    @Override
    public boolean getVillagerDataFinalized() {
        return this.entityData.get(DATA_VILLAGER_DATA_FINALIZED);
    }

    @Override
    public void setVillagerDataFinalized(boolean villagerDataFinalized) {
        this.entityData.set(DATA_VILLAGER_DATA_FINALIZED, villagerDataFinalized);
    }

    @Override
    public @NonNull VillagerData getVillagerData() {
        return this.entityData.get(DATA_VILLAGER_DATA);
    }

    @Override
    protected void rewardTradeXp(MerchantOffer offer) {
        int popXp = 3 + this.random.nextInt(4);
        this.villagerXp = this.villagerXp + offer.getXp();
        this.lastTradedPlayer = getTradingPlayer();

        if (shouldIncreaseLevel()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                increaseMerchantCareer(serverLevel);
                addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
            }

            popXp += 5;
        }

        if (offer.shouldRewardExp()) {
            level().addFreshEntity(new ExperienceOrb(level(), getX(), getY() + 0.5, getZ(), popXp));
        }
    }

    @Override
    public void setLastHurtByMob(@Nullable LivingEntity hurtBy) {
        if (hurtBy != null && this.level() instanceof ServerLevel serverLevel) {
            serverLevel.onReputationEvent(ReputationEventType.VILLAGER_HURT, hurtBy, this);

            if (isAlive() && hurtBy instanceof Player) {
                level().broadcastEntityEvent(this, (byte) 13);
            }
        }

        super.setLastHurtByMob(hurtBy);
    }

    @Override
    public void die(DamageSource source) {
        Entity murderer = source.getEntity();

        if (murderer != null) {
            tellWitnessesThatIWasMurdered(murderer);
        }

        releaseAllPois();
        super.die(source);
    }

    private void tellWitnessesThatIWasMurdered(Entity murderer) {
        if (this.level() instanceof ServerLevel serverLevel) {
            Optional<NearestVisibleLivingEntities> witnesses = this.brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);

            witnesses.ifPresent(nearestVisibleLivingEntities -> nearestVisibleLivingEntities
                    .findAll(ReputationEventHandler.class::isInstance)
                    .forEach(witness -> serverLevel.onReputationEvent(ReputationEventType.VILLAGER_KILLED, murderer, (ReputationEventHandler) witness)));
        }
    }

    private void releaseAllPois() {
        releasePoi(MemoryModuleType.HOME);
        releasePoi(MemoryModuleType.JOB_SITE);
        releasePoi(MemoryModuleType.POTENTIAL_JOB_SITE);
        releasePoi(MemoryModuleType.MEETING_POINT);
    }

    public void releasePoi(MemoryModuleType<GlobalPos> memoryType) {
        if (this.level() instanceof ServerLevel serverLevel) {
            MinecraftServer server = serverLevel.getServer();
            this.brain.getMemory(memoryType).ifPresent(memory -> {
                ServerLevel poiLevel = server.getLevel(memory.dimension());

                if (poiLevel != null) {
                    PoiManager poiManager = poiLevel.getPoiManager();
                    Optional<Holder<PoiType>> type = poiManager.getType(memory.pos());
                    BiPredicate<AlienVillager, Holder<PoiType>> poiTypePredicate = POI_MEMORIES.get(memoryType);

                    if (type.isPresent() && poiTypePredicate.test(this, type.get())) {
                        poiManager.release(memory.pos());
                        poiLevel.debugSynchronizers().updatePoi(memory.pos());
                    }
                }
            });
        }
    }

    @Override
    public boolean canBreed() {
        return this.foodLevel + countFoodPointsInInventory() >= 12 && !isSleeping() && getAge() == 0;
    }

    private boolean hungry() {
        return this.foodLevel < 12;
    }

    private void eatUntilFull() {
        if (hungry()) {
            for (int slot = 0; slot < getInventory().getContainerSize(); slot++) {
                ItemStack itemStack = getInventory().getItem(slot);
                AlienVillagerFood villagerFood = itemStack.get(GalacticraftDataComponents.ALIEN_VILLAGER_FOOD);

                if (villagerFood != null) {
                    int itemCount = itemStack.getCount();
                    int toRemove = 0;

                    for (int count = itemCount; count > 0; count--) {
                        this.foodLevel = this.foodLevel + villagerFood.nutrition();
                        toRemove++;

                        if (!hungry()) {
                            getInventory().removeItem(slot, toRemove);
                            return;
                        }
                    }

                    getInventory().removeItem(slot, toRemove);
                }
            }
        }
    }

    public int getPlayerReputation(Player player) {
        return this.gossips.getReputation(player.getUUID(), _ -> true);
    }

    private void digestFood() {
        this.foodLevel -= 12;
    }

    public void eatAndDigestFood() {
        eatUntilFull();
        digestFood();
    }

    public void setOffers(MerchantOffers offers) {
        this.offers = offers;
    }

    private boolean shouldIncreaseLevel() {
        int currentLevel = getVillagerData().level();
        return VillagerData.canLevelUp(currentLevel) && this.villagerXp >= VillagerData.getMaxXpPerLevel(currentLevel);
    }

    private void increaseMerchantCareer(ServerLevel level) {
        this.setVillagerData(this.getVillagerData().withLevel(getVillagerData().level() + 1));
        this.updateTrades(level);
    }

    @Override
    protected @NonNull Component getTypeName() {
        return getVillagerData().profession().value().name();
    }

    @Override
    public void handleEntityEvent(@EntityEvent.Value byte id) {
        if (id == 12) {
            addParticlesAroundSelf(ParticleTypes.HEART);
        } else if (id == 13) {
            addParticlesAroundSelf(ParticleTypes.ANGRY_VILLAGER);
        } else if (id == 14) {
            addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);
        } else if (id == 42) {
            addParticlesAroundSelf(ParticleTypes.SPLASH);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        if (spawnReason == EntitySpawnReason.BREEDING) {
            setVillagerData(getVillagerData().withProfession(GalacticraftVillagerProfession.MOON));
        }

        finalizeVillagerType(level, blockPosition());
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
    }

    @Override
    public void finalizeVillagerType(ServerLevelAccessor level, BlockPos pos) {
        if (!getVillagerDataFinalized()) {
            setVillagerData(getVillagerData().withType(GalacticraftVillagerTypes.MOON));
            setVillagerDataFinalized(true);
        }
    }

    @Override
    public @Nullable AlienVillager getBreedOffspring(ServerLevel level, AgeableMob partner) {
        double biomeRoll = this.random.nextDouble();
        Holder<VillagerType> type;

        if (biomeRoll < 0.5) {
            type = GalacticraftVillagerTypes.MOON;
        } else if (biomeRoll < 0.75) {
            type = getVillagerData().type();
        } else {
            type = ((AlienVillager) partner).getVillagerData().type();
        }

        AlienVillager baby = new AlienVillager(GalacticraftEntityTypes.ALIEN_VILLAGER.get(), level);
        baby.setVillagerData(baby.getVillagerData().withType(type).withProfession(GalacticraftVillagerProfession.MOON));
        baby.setVillagerDataFinalized(true);

        return baby;
    }

//    @Override
//    public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
//        if (level.getDifficulty() != Difficulty.PEACEFUL && EventHooks.canLivingConvert(this, GalacticraftEntityTypes.EVOLVED_WITCH.get(), _ -> {})) {
//
//            EvolvedWitch witch = convertTo(GalacticraftEntityTypes.EVOLVED_WITCH.get(), ConversionParams.single(this, false, false), w -> {
//                w.finalizeSpawn(level, level.getCurrentDifficultyAt(w.blockPosition()), EntitySpawnReason.CONVERSION, null);
//                EventHooks.onLivingConvert(this, w);
//                w.setPersistenceRequired();
//                releaseAllPois();
//            });
//
//            if (witch == null) {
//                super.thunderHit(level, lightningBolt);
//            }
//        } else {
//            super.thunderHit(level, lightningBolt);
//        }
//    }

    @Override
    protected void pickUpItem(ServerLevel level, ItemEntity entity) {
        InventoryCarrier.pickUpItem(level, this, this, entity);
    }

    @Override
    public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
        Item item = itemStack.getItem();

        return (itemStack.is(ItemTags.VILLAGER_PICKS_UP)
                || itemStack.has(GalacticraftDataComponents.ALIEN_VILLAGER_FOOD)
                || getVillagerData().profession().value().requestedItems().contains(item)
        ) && getInventory().canAddItem(itemStack);
    }

    public boolean hasExcessFood() {
        return countFoodPointsInInventory() >= 24;
    }

    public boolean wantsMoreFood() {
        return countFoodPointsInInventory() < 12;
    }

    private int countFoodPointsInInventory() {
        SimpleContainer inventory = getInventory();
        int points = 0;

        for (ItemStack item : inventory.getItems()) {
            VillagerFood villagerFood = item.get(DataComponents.VILLAGER_FOOD);

            if (villagerFood != null) {
                points += item.count() * villagerFood.nutrition();
            }
        }

        return points;
    }

    @Override
    protected void updateTrades(ServerLevel level) {
        VillagerData data = getVillagerData();
        VillagerProfession profession = data.profession().value();
        ResourceKey<TradeSet> trades = profession.getTrades(data.level());

        if (trades != null) {
            addOffersFromTradeSet(level, getOffers(), trades);

            if (SharedConstants.DEBUG_UNLOCK_ALL_TRADES && data.level() < 5) {
                increaseMerchantCareer(level);
            }

            Player tradingPlayer = getTradingPlayer();

            if (tradingPlayer != null) {
                updateSpecialPrices(tradingPlayer);
            }
        }
    }

    public void gossip(ServerLevel level, AlienVillager target, long timestamp) {
        if ((timestamp < this.lastGossipTime || timestamp >= this.lastGossipTime + 177000L)
                && (timestamp < target.lastGossipTime || timestamp >= target.lastGossipTime + 177000L)) {
            int newGossip = this.gossips.transferFrom(target.gossips, this.random, 10);
            this.lastGossipTime = timestamp;
            target.lastGossipTime = timestamp;

            Player tradingPlayer = getTradingPlayer();

            if (tradingPlayer != null && newGossip > 0) {
                updateSpecialPrices(tradingPlayer);
            }
        }
    }

    private void maybeDecayGossip() {
        long timestamp = level().getGameTime();
        if (this.lastGossipDecayTime == 0L) {
            this.lastGossipDecayTime = timestamp;
        } else if (timestamp >= this.lastGossipDecayTime + 354000L) {
            this.gossips.decay();
            this.lastGossipDecayTime = timestamp;
        }
    }

    @Override
    public void onReputationEventFrom(ReputationEventType type, Entity source) {
        if (type == ReputationEventType.ZOMBIE_VILLAGER_CURED) {
            this.gossips.add(source.getUUID(), GossipType.MAJOR_POSITIVE, 20);
            this.gossips.add(source.getUUID(), GossipType.MINOR_POSITIVE, 25);
        } else if (type == ReputationEventType.TRADE) {
            this.gossips.add(source.getUUID(), GossipType.TRADING, 2);
        } else if (type == ReputationEventType.VILLAGER_HURT) {
            this.gossips.add(source.getUUID(), GossipType.MINOR_NEGATIVE, 25);
        } else if (type == ReputationEventType.VILLAGER_KILLED) {
            this.gossips.add(source.getUUID(), GossipType.MAJOR_NEGATIVE, 25);
        }

        Player tradingPlayer = getTradingPlayer();
        
        if (tradingPlayer != null && tradingPlayer.getUUID().equals(source.getUUID())) {
            updateSpecialPrices(tradingPlayer);
        }
    }

    @Override
    public int getVillagerXp() {
        return this.villagerXp;
    }

    public void setVillagerXp(int value) {
        this.villagerXp = value;
    }

    private void resetNumberOfRestocks() {
        this.catchUpDemand();
        this.numberOfRestocksToday = 0;
    }

    public GossipContainer getGossips() {
        return this.gossips;
    }

    public void setGossips(GossipContainer gossips) {
        this.gossips.putAll(gossips);
    }

    @Override
    public void stopSleeping() {
        super.stopSleeping();
        this.brain.setMemory(MemoryModuleType.LAST_WOKEN, level().getGameTime());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> @Nullable T get(DataComponentType<? extends T> type) {
        return type == DataComponents.VILLAGER_VARIANT ? castComponentValue((DataComponentType<T>)type, getVillagerData().type()) : super.get(type);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        applyImplicitComponentIfPresent(components, DataComponents.VILLAGER_VARIANT);
        super.applyImplicitComponents(components);
    }

    @Override
    protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
        if (type == DataComponents.VILLAGER_VARIANT) {
            Holder<VillagerType> variant = castComponentValue(DataComponents.VILLAGER_VARIANT, value);
            setVillagerData(getVillagerData().withType(variant));
            setVillagerDataFinalized(true);
            
            return true;
        }

        return super.applyImplicitComponent(type, value);
    }
}
