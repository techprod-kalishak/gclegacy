package io.kalishak.galacticraftlegacy.world.level.block.entity;

import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.transfer.VoidingResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import org.jspecify.annotations.NonNull;

public abstract class AbstractFluidTankMachineBlockEntity extends AbstractMachineBlockEntity {
    protected final NonNullList<FluidStack> tanks = NonNullList.withSize(tankSize(), FluidStack.EMPTY);
    protected final FluidStacksResourceHandler innerTankResourceHandler = new FluidStacksResourceHandler(this.tanks, tankCapacity());

    protected AbstractFluidTankMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    protected static <M extends AbstractFluidTankMachineBlockEntity> void registerFluidEnergyHandler(@NonNull Direction direction, BlockEntityType<M> type, RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Fluid.BLOCK,
                type,
                (machine, cxt) -> {
                    if (cxt == null || cxt == direction) {
                        return machine.innerTankResourceHandler;
                    }

                    return new VoidingResourceHandler<>(FluidResource.EMPTY);
                }
        );
    }

    protected abstract int tankSize();
    protected abstract int tankCapacity();

    public void setTank(int index, FluidResource resource, int amount) {
        this.innerTankResourceHandler.set(index, resource, amount);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.innerTankResourceHandler.deserialize(input);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.innerTankResourceHandler.serialize(output);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter componentGetter) {
        super.applyImplicitComponents(componentGetter);

        componentGetter.getOrDefault(GalacticraftDataComponents.FLUID_TANK, SimpleFluidContent.EMPTY).copy();
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(GalacticraftDataComponents.FLUID_TANK, SimpleFluidContent.EMPTY);
    }
}
