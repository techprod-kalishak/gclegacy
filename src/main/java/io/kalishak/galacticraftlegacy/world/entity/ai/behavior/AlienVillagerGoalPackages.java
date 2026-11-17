package io.kalishak.galacticraftlegacy.world.entity.ai.behavior;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityTypes;
import io.kalishak.galacticraftlegacy.world.entity.npc.villager.AlienVillager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class AlienVillagerGoalPackages {
    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super AlienVillager>>> getCorePackage(
            Holder<VillagerProfession> profession, float speedModifier
    ) {
        return ImmutableList.of(
                Pair.of(0, new Swim<>(0.8F)),
                Pair.of(0, InteractWithDoor.create()),
                Pair.of(0, new LookAtTargetSink(45, 90)),
                Pair.of(0, new AlienVillagerPanicTrigger()),
                Pair.of(0, WakeUp.create()),
                Pair.of(0, ReactToBell.create()),
                Pair.of(0, ValidateNearbyAlienPoi.create(profession.value().heldJobSite(), MemoryModuleType.JOB_SITE)),
                Pair.of(0, ValidateNearbyAlienPoi.create(profession.value().acquirableJobSite(), MemoryModuleType.POTENTIAL_JOB_SITE)),
                Pair.of(1, new MoveToTargetSink()),
                Pair.of(2, AlienPoiCompetitorScan.create()),
                Pair.of(3, new AlienLookAndFollowTradingPlayerSink(speedModifier)),
                Pair.of(5, GoToWantedItem.create(speedModifier, false, 4)),
                Pair.of(
                        6,
                        AcquirePoi.create(
                                profession.value().acquirableJobSite(),
                                MemoryModuleType.JOB_SITE,
                                MemoryModuleType.POTENTIAL_JOB_SITE,
                                true,
                                Optional.empty(),
                                (_, _) -> true
                        )
                ),
                Pair.of(7, new GoToPotentialAlienJobSite(speedModifier)),
                Pair.of(8, YieldAlienJobSite.create(speedModifier)),
                Pair.of(10, AcquirePoi.create(p -> p.is(PoiTypes.HOME), MemoryModuleType.HOME, false, Optional.of((byte) 14), AlienVillagerGoalPackages::validateBedPoi)),
                Pair.of(10, AcquirePoi.create(p -> p.is(PoiTypes.MEETING), MemoryModuleType.MEETING_POINT, true, Optional.of((byte) 14))),
                Pair.of(10, AssignAlienProfessionFromJobSite.create()),
                Pair.of(10, ResetAlienProfession.create())
        );
    }

    private static boolean validateBedPoi(ServerLevel level, BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);
        return blockState.is(BlockTags.VILLAGERS_CAN_SLEEP_ON_BED) && !blockState.getValue(BedBlock.OCCUPIED);
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super AlienVillager>>> getWorkPackage(Holder<VillagerProfession> profession, float speedModifier) {
        WorkAtAlienPoi workAtPoi = new WorkAtAlienPoi();

        return ImmutableList.of(
                getMinimalLookBehavior(),
                Pair.of(
                        5,
                        new RunOne<>(
                                ImmutableList.of(
                                        Pair.of(workAtPoi, 7),
                                        Pair.of(StrollAroundPoi.create(MemoryModuleType.JOB_SITE, 0.4F, 4), 2),
                                        Pair.of(StrollToPoi.create(MemoryModuleType.JOB_SITE, 0.4F, 1, 10), 5),
                                        Pair.of(StrollToAlienPoiList.create(MemoryModuleType.SECONDARY_JOB_SITE, speedModifier, 1, 6, MemoryModuleType.JOB_SITE), 5)
                                )
                        )
                ),
                Pair.of(10, new ShowAlienTradesToPlayer(400, 1600)),
                Pair.of(10, SetLookAndInteract.create(EntityTypes.PLAYER, 4)),
                Pair.of(2, io.kalishak.galacticraftlegacy.world.entity.ai.behavior.SetWalkTargetFromBlockMemory.create(MemoryModuleType.JOB_SITE, speedModifier, 9, 100, 1200)),
                Pair.of(99, UpdateActivityFromSchedule.create())
        );
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super AlienVillager>>> getPlayPackage(float speedModifier) {
        return ImmutableList.of(
                Pair.of(0, new MoveToTargetSink(80, 120)),
                getFullLookBehavior(),
                Pair.of(5, PlayTagWithOtherKids.create()),
                Pair.of(
                        5,
                        new RunOne<>(
                                ImmutableMap.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryStatus.VALUE_ABSENT),
                                ImmutableList.of(
                                        Pair.of(InteractWith.of(GalacticraftEntityTypes.ALIEN_VILLAGER.get(), 8, MemoryModuleType.INTERACTION_TARGET, speedModifier, 2), 2),
                                        Pair.of(VillageBoundRandomStroll.create(speedModifier), 1),
                                        Pair.of(SetWalkTargetFromLookTarget.create(speedModifier, 2), 1),
                                        Pair.of(new JumpOnBed(speedModifier), 2),
                                        Pair.of(new DoNothing(20, 40), 2)
                                )
                        )
                ),
                Pair.of(99, UpdateActivityFromSchedule.create())
        );
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super AlienVillager>>> getRestPackage(float speedModifier) {
        return ImmutableList.of(
                Pair.of(2, io.kalishak.galacticraftlegacy.world.entity.ai.behavior.SetWalkTargetFromBlockMemory.create(MemoryModuleType.HOME, speedModifier, 1, 150, 1200)),
                Pair.of(3, ValidateNearbyAlienPoi.create(p -> p.is(PoiTypes.HOME), MemoryModuleType.HOME)),
                Pair.of(3, new SleepInBed()),
                Pair.of(
                        5,
                        new RunOne<>(
                                ImmutableMap.of(MemoryModuleType.HOME, MemoryStatus.VALUE_ABSENT),
                                ImmutableList.of(
                                        Pair.of(SetClosestHomeAsWalkTarget.create(speedModifier), 1),
                                        Pair.of(InsideBrownianWalk.create(speedModifier), 4),
                                        Pair.of(GoToClosestMoonVillage.create(speedModifier, 4), 2),
                                        Pair.of(new DoNothing(20, 40), 2)
                                )
                        )
                ),
                getMinimalLookBehavior(),
                Pair.of(99, UpdateActivityFromSchedule.create())
        );
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super AlienVillager>>> getMeetPackage(float speedModifier) {
        return ImmutableList.of(
                Pair.of(
                        2,
                        TriggerGate.triggerOneShuffled(
                                ImmutableList.of(Pair.of(StrollAroundPoi.create(MemoryModuleType.MEETING_POINT, 0.4F, 40), 2), Pair.of(SocializeAtBell.create(), 2))
                        )
                ),
                Pair.of(10, new ShowAlienTradesToPlayer(400, 1600)),
                Pair.of(10, SetLookAndInteract.create(EntityTypes.PLAYER, 4)),
                Pair.of(2, io.kalishak.galacticraftlegacy.world.entity.ai.behavior.SetWalkTargetFromBlockMemory.create(MemoryModuleType.MEETING_POINT, speedModifier, 6, 100, 200)),
                Pair.of(3, ValidateNearbyAlienPoi.create(p -> p.is(PoiTypes.MEETING), MemoryModuleType.MEETING_POINT)),
                Pair.of(
                        3,
                        new GateBehavior<>(
                                ImmutableMap.of(),
                                ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET),
                                GateBehavior.OrderPolicy.ORDERED,
                                GateBehavior.RunningPolicy.RUN_ONE,
                                ImmutableList.of(Pair.of(new TradeWithAlienVillager(), 1))
                        )
                ),
                getFullLookBehavior(),
                Pair.of(99, UpdateActivityFromSchedule.create())
        );
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super AlienVillager>>> getIdlePackage(float speedModifier) {
        return ImmutableList.of(
                Pair.of(
                        2,
                        new RunOne<>(
                                ImmutableList.of(
                                        Pair.of(InteractWith.of(GalacticraftEntityTypes.ALIEN_VILLAGER.get(), 8, MemoryModuleType.INTERACTION_TARGET, speedModifier, 2), 2),
                                        Pair.of(
                                                InteractWith.of(
                                                        GalacticraftEntityTypes.ALIEN_VILLAGER.get(), 8, AgeableMob::canBreed, AgeableMob::canBreed, MemoryModuleType.BREED_TARGET, speedModifier, 2
                                                ),
                                                1
                                        ),
                                        Pair.of(VillageBoundRandomStroll.create(speedModifier), 1),
                                        Pair.of(SetWalkTargetFromLookTarget.create(speedModifier, 2), 1),
                                        Pair.of(new JumpOnBed(speedModifier), 1),
                                        Pair.of(new DoNothing(30, 60), 1)
                                )
                        )
                ),
                Pair.of(3, SetLookAndInteract.create(EntityTypes.PLAYER, 4)),
                Pair.of(3, new ShowAlienTradesToPlayer(400, 1600)),
                Pair.of(
                        3,
                        new GateBehavior<>(
                                ImmutableMap.of(),
                                ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET),
                                GateBehavior.OrderPolicy.ORDERED,
                                GateBehavior.RunningPolicy.RUN_ONE,
                                ImmutableList.of(Pair.of(new TradeWithAlienVillager(), 1))
                        )
                ),
                Pair.of(
                        3,
                        new GateBehavior<>(
                                ImmutableMap.of(),
                                ImmutableSet.of(MemoryModuleType.BREED_TARGET),
                                GateBehavior.OrderPolicy.ORDERED,
                                GateBehavior.RunningPolicy.RUN_ONE,
                                ImmutableList.of(Pair.of(new AlienVillagerMakeLove(), 1))
                        )
                ),
                getFullLookBehavior(),
                Pair.of(99, UpdateActivityFromSchedule.create())
        );
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super AlienVillager>>> getPanicPackage(float speedModifier) {
        float runawaySpeed = speedModifier * 1.5F;
        return ImmutableList.of(
                Pair.of(0, VillagerCalmDown.create()),
                Pair.of(1, SetWalkTargetAwayFrom.entity(MemoryModuleType.NEAREST_HOSTILE, runawaySpeed, 6, false)),
                Pair.of(1, SetWalkTargetAwayFrom.entity(MemoryModuleType.HURT_BY_ENTITY, runawaySpeed, 6, false)),
                Pair.of(3, VillageBoundRandomStroll.create(runawaySpeed, 2, 2)),
                getMinimalLookBehavior()
        );
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super AlienVillager>>> getHidePackage(float speedModifier) {
        return ImmutableList.of(
                Pair.of(0, SetHiddenState.create(15, 3)), Pair.of(1, LocateHidingPlace.create(32, speedModifier * 1.25F, 2)), getMinimalLookBehavior()
        );
    }

    private static Pair<Integer, BehaviorControl<LivingEntity>> getFullLookBehavior() {
        return Pair.of(
                5,
                new RunOne<>(
                        ImmutableList.of(
                                Pair.of(SetEntityLookTarget.create(GalacticraftEntityTypes.ALIEN_VILLAGER.get(), 8.0F), 2),
                                Pair.of(SetEntityLookTarget.create(EntityTypes.PLAYER, 8.0F), 2),
                                Pair.of(SetEntityLookTarget.create(MobCategory.CREATURE, 8.0F), 1),
                                Pair.of(SetEntityLookTarget.create(MobCategory.MONSTER, 8.0F), 1),
                                Pair.of(new DoNothing(30, 60), 2)
                        )
                )
        );
    }

    private static Pair<Integer, BehaviorControl<LivingEntity>> getMinimalLookBehavior() {
        return Pair.of(
                5,
                new RunOne<>(
                        ImmutableList.of(
                                Pair.of(SetEntityLookTarget.create(GalacticraftEntityTypes.ALIEN_VILLAGER.get(), 8.0F), 2),
                                Pair.of(SetEntityLookTarget.create(EntityTypes.PLAYER, 8.0F), 2),
                                Pair.of(new DoNothing(30, 60), 8)
                        )
                )
        );
    }
}
