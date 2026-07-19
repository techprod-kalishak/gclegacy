package io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket;

import io.kalishak.galacticraftlegacy.world.item.FeatureTier;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.level.block.entity.machine.FuelableDock;
import io.kalishak.galacticraftlegacy.world.level.telemetry.GloballyReferencedEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class Tier1Rocket extends TieredRocket {
    public Tier1Rocket(EntityType<?> entityType, Level level, Type type, @Nullable Player owner) {
        super(entityType, level, FeatureTier.TIER_1, type, GalacticraftItems.TIER_1_ROCKET::value, owner);
    }

    public Tier1Rocket(EntityType<?> entityType, Level level) {
        super(entityType, level, FeatureTier.TIER_1, Type.DEFAULT, GalacticraftItems.TIER_1_ROCKET::value);
    }

    @Override
    public float getZoom() {
        return 0;
    }

    @Override
    public boolean thirdPerson() {
        return false;
    }

    @Override
    public void setPad(FuelableDock pad) {

    }

    @Override
    public FuelableDock getPad() {
        return null;
    }

    @Override
    public void onPadDestroyed() {

    }

    @Override
    public boolean isDockValid(FuelableDock dock) {
        return false;
    }

    @Override
    public boolean inFlight() {
        return false;
    }

    @Override
    public LoadingState addCargo(ItemStack stack, boolean simulate, @Nullable Transaction tx) {
        return null;
    }

    @Override
    public Result removeCargo(boolean simulate, @Nullable Transaction tx) {
        return null;
    }

    @Override
    public int getPreLaunchDelay() {
        return 0;
    }

    @Override
    protected ItemStack getDropAsRocket() {
        return null;
    }

    @Override
    public Collection<ItemEntity> captureDrops() {
        return List.of();
    }

    @Override
    public GloballyReferencedEntity asReference() {
        return null;
    }
}
