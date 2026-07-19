package io.kalishak.galacticraftlegacy.world.item.vehicle;

import net.minecraft.core.Holder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class RocketItem extends Item {
    private final Holder<EntityType<?>> vehicleType;
    private final PlacementRule placementRule;

    public RocketItem(Holder<EntityType<?>> vehicleType, PlacementRule placementRule, Properties properties) {
        super(properties);
        this.vehicleType = vehicleType;
        this.placementRule = placementRule;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return super.useOn(context);
    }

    public Holder<EntityType<?>> getVehicleType() {
        return vehicleType;
    }

    public enum PlacementRule {
        LANDING_PAD,
        FUEL_PAD,
        ASTRO_MINER
    }
}
