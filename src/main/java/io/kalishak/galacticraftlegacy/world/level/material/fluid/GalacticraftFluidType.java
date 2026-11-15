/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.material.fluid;

import io.kalishak.galacticraftlegacy.Galacticraft;
import io.kalishak.galacticraftlegacy.references.GalacticraftFluidIds;
import io.kalishak.galacticraftlegacy.world.level.GalacticraftParticleTypes;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class GalacticraftFluidType {
    private static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, Galacticraft.MODID);

    public static final DeferredHolder<FluidType, FluidType> OXYGEN = REGISTRY.register(
            GalacticraftFluidIds.OXYGEN.identifier().getPath(),
            () -> GaseousFluid.gaseousType("block.galacticraftlegacy.oxygen")
    );
    public static final DeferredHolder<FluidType, FluidType> HYDROGEN = REGISTRY.register(
            GalacticraftFluidIds.HYDROGEN.identifier().getPath(),
            () -> GaseousFluid.gaseousType("block.galacticraftlegacy.hydrogen")
    );
    public static final DeferredHolder<FluidType, FluidType> NITROGEN = REGISTRY.register(
            GalacticraftFluidIds.NITROGEN.identifier().getPath(),
            () -> GaseousFluid.gaseousType("block.galacticraftlegacy.nitrogen")
    );
    public static final DeferredHolder<FluidType, FluidType> CO2 = REGISTRY.register(
            GalacticraftFluidIds.CO2.identifier().getPath(),
            () -> GaseousFluid.gaseousType("block.galacticraftlegacy.co2")
    );

    public static final DeferredHolder<FluidType, FluidType> OIL = REGISTRY.register(
            GalacticraftFluidIds.OIL.identifier().getPath(),
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.galacticraftlegacy.oil")
                    .viscosity(2230)
                    .density(700)
                    .rarity(Rarity.UNCOMMON)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
            )
    );
    public static final DeferredHolder<FluidType, FluidType> FUEL = REGISTRY.register(
            GalacticraftFluidIds.FUEL.identifier().getPath(),
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.galacticraftlegacy.fuel")
                    .viscosity(850)
                    .density(770)
                    .rarity(Rarity.RARE)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
            )
    );
    public static final DeferredHolder<FluidType, FluidType> BACTERIAL_SLUDGE = REGISTRY.register(
            GalacticraftFluidIds.BACTERIAL_SLUDGE.identifier().getPath(),
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.galacticraftlegacy.bacterial_sludge")
                    .viscosity(1500)
                    .density(800)
                    .rarity(Rarity.RARE)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
            )
    );
    public static final DeferredHolder<FluidType, FluidType> SULFURIC_ACID = REGISTRY.register(
            GalacticraftFluidIds.SULFURIC_ACID.identifier().getPath(),
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.galacticraftlegacy.sulfuric_acid")
                    .viscosity(1400)
                    .density(6229)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                    .sound(SoundActions.CAULDRON_DRIP, SoundEvents.POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON)
                    .addDripstoneDripping(0.1F, GalacticraftParticleTypes.DRIPPING_SULFURIC_ACID.get(), GalacticraftBlocks.SULFURIC_ACID_CAULDRON.get(), SoundEvents.BUCKET_FILL)
            )
    );

    public static void init(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
