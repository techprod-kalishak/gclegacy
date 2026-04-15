package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

import io.kalishak.galacticraftlegacy.world.inventory.CircuitFabricatorMenu;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.CircuitRecipe;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input.SimpleResourceInput;
import io.kalishak.galacticraftlegacy.world.level.block.GalacticraftBlocks;
import io.kalishak.galacticraftlegacy.world.level.block.entity.GalacticraftBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

public class CircuitFabricatorBlockEntity extends AbstractMachineBlockEntity implements RecipeCraftingHolder {
    public static final int DATA_PROCESS_PROGRESS = 0;
    public static final int DATA_PROGRESS_TIME_TOTAL = 1;
    public static final int PROCESS_RETRACT_SPEED = 2;
    public static final int SLOT_BATTERY = 0;
    public static final int SLOT_DIAMOND = 1;
    public static final int SLOT_SILICON_1 = 2;
    public static final int SLOT_SILICON_2 = 3;
    public static final int SLOT_REDSTONE = 4;
    public static final int SLOT_INGREDIENT = 5;
    public static final int SLOT_OUTPUT = 6;
    public static final int SLOT_COUNT = 7;
    private int processProgress;
    private int processTimeTotal;
    private final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case DATA_PROCESS_PROGRESS -> CircuitFabricatorBlockEntity.this.processProgress;
                case DATA_PROGRESS_TIME_TOTAL -> CircuitFabricatorBlockEntity.this.processTimeTotal;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case DATA_PROCESS_PROGRESS -> CircuitFabricatorBlockEntity.this.processProgress = value;
                case DATA_PROGRESS_TIME_TOTAL -> CircuitFabricatorBlockEntity.this.processTimeTotal = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };
    private final RecipeManager.CachedCheck<SimpleResourceInput, CircuitRecipe> quickCheck = RecipeManager.createCheck(GalacticraftRecipeType.CIRCUIT.get());

    public CircuitFabricatorBlockEntity(BlockPos pos, BlockState blockState) {
        super(GalacticraftBlockEntityType.CIRCUIT_FABRICATOR.get(), pos, blockState);
    }

    public static void serverTick(ServerLevel serverLevel, BlockPos tickerPos, BlockState tickerState, CircuitFabricatorBlockEntity circuitFabricator) {
        boolean shouldUpdate = false;
        boolean canProcess;

        energyTransferTick(circuitFabricator, circuitFabricator.processProgress > 0, false, BASIC_MACHINE_MAX_TRANSFER_RATE, null);

        if (circuitFabricator.processTimeTotal > 0) {
            circuitFabricator.processProgress++;
        }

        ItemResource ingredientResource = circuitFabricator.innerResourceHandler.getResource(SLOT_INGREDIENT);

        try (Transaction tx = Transaction.open(null)) {
            SimpleResourceInput simpleResourceInput = new SimpleResourceInput(() -> circuitFabricator.innerResourceHandler, SLOT_DIAMOND, SLOT_COUNT);
            RecipeHolder<CircuitRecipe> recipeHolder = circuitFabricator.quickCheck.getRecipeFor(simpleResourceInput, serverLevel).orElse(null);

            canProcess = canProcess(serverLevel.registryAccess(), recipeHolder, simpleResourceInput, circuitFabricator.innerResourceHandler, circuitFabricator.energyHandler, tx);

            if (circuitFabricator.processProgress <= 0 && !ingredientResource.isEmpty()) {
                if (canProcess) {
                    circuitFabricator.processTimeTotal = 200;
                }
            } else if (circuitFabricator.processProgress == circuitFabricator.processTimeTotal && canProcess) {
                if (process(serverLevel.registryAccess(), recipeHolder, simpleResourceInput, circuitFabricator.innerResourceHandler, circuitFabricator.energyHandler, tx)) {
                    tx.commit();
                    shouldUpdate = true;

                    circuitFabricator.processTimeTotal = 0;
                }
            }
        }

        if (!canProcess || circuitFabricator.processProgress > 0) {
            circuitFabricator.processProgress = Mth.clamp(circuitFabricator.processProgress - PROCESS_RETRACT_SPEED, 0, circuitFabricator.processTimeTotal);
        } else {
            circuitFabricator.processTimeTotal = 0;
        }

        if (shouldUpdate) {
            BlockEntity.setChanged(serverLevel, tickerPos, tickerState);
        }
    }

    private static boolean canProcess(RegistryAccess registryAccess, @Nullable RecipeHolder<CircuitRecipe> recipeHolder, SimpleResourceInput simpleResourceInput, ResourceHandler<ItemResource> items, EnergyHandler energyHandler, Transaction parentTx) {
        if (hasRequiredIngredients(items, 1, 6) && recipeHolder != null) {
            try (Transaction tx = Transaction.open(parentTx)) {
                if (energyHandler.extract(CircuitFabricatorBlockEntity.BASIC_MACHINE_MAX_TRANSFER_RATE, tx) > 0) {
                    ItemStack assembledResult = recipeHolder.value().assemble(simpleResourceInput);

                    if (assembledResult.isEmpty()) return false;

                    if (!recipeHolder.value().disassembleIngredients(simpleResourceInput, tx, registryAccess, true).isEmpty()) {
                        if (items.insert(SLOT_OUTPUT, ItemResource.of(assembledResult), assembledResult.getCount(), tx) > 0) {
                            tx.close();

                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private static boolean process(RegistryAccess registryAccess, @Nullable RecipeHolder<CircuitRecipe> recipeHolder, SimpleResourceInput simpleResourceInput, ResourceHandler<ItemResource> items, EnergyHandler energyHandler, Transaction parentTx) {
        if (recipeHolder != null && canProcess(registryAccess, recipeHolder, simpleResourceInput, items, energyHandler, parentTx)) {
            try (Transaction tx = Transaction.open(parentTx)) {
                if (energyHandler.extract(CircuitFabricatorBlockEntity.BASIC_MACHINE_MAX_TRANSFER_RATE, tx) > 0) {
                    ItemStack assembledResult = recipeHolder.value().disassembleIngredients(simpleResourceInput, tx, registryAccess, false);

                    if (items.insert(SLOT_OUTPUT, ItemResource.of(assembledResult.typeHolder()), assembledResult.getCount(), tx) > 0) {
                        tx.commit();

                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerSingleEnergyInputEnergyHandler(Direction.EAST, GalacticraftBlockEntityType.CIRCUIT_FABRICATOR.get(), event);
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                GalacticraftBlockEntityType.CIRCUIT_FABRICATOR.get(),
                (machine, direction) -> {
                    if (direction == Direction.UP) {
                        return RangedResourceHandler.of(() -> machine.innerResourceHandler, SLOT_BATTERY, SLOT_OUTPUT);
                    } else if (direction == Direction.DOWN) {
                        return RangedResourceHandler.ofSingleIndex(() -> machine.innerResourceHandler, SLOT_OUTPUT);
                    }

                    return machine.innerResourceHandler;
                }
        );
    }

    private static boolean checkRecipe(CircuitFabricatorBlockEntity machine, ServerLevel serverLevel) {
        SimpleResourceInput simpleResourceInput = new SimpleResourceInput(() -> machine.innerResourceHandler, SLOT_DIAMOND, SLOT_COUNT);
        return machine.quickCheck.getRecipeFor(simpleResourceInput, serverLevel).isPresent();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("ProcessProgress", this.processProgress);
        output.putInt("ProcessTotalTime", this.processTimeTotal);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.processProgress = input.getIntOr("ProcessProgress", 0);
        this.processTimeTotal = input.getIntOr("ProcessTotalTime", 0);
    }

    @Override
    public void set(int index, ItemResource resource, int amount) {
        super.set(index, resource, amount);

        if (hasRequiredIngredients(this, SLOT_DIAMOND, SLOT_OUTPUT)) {
            if (this.level instanceof ServerLevel serverLevel) {
                if (checkRecipe(this, serverLevel) && this.processTimeTotal == 0) {
                    this.processProgress = 0;
                    this.processTimeTotal = 400;
                }
            }
        }

        setChanged();
    }

    @Override
    protected int size() {
        return SLOT_COUNT;
    }

    @Override
    protected int getBatterySlotIndex() {
        return SLOT_BATTERY;
    }

    @Override
    protected Component getDefaultName() {
        return GalacticraftBlocks.CIRCUIT_FABRICATOR.get().getName();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new CircuitFabricatorMenu(containerId, playerInventory, this, this.containerData);
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {

    }

    @Override
    public @Nullable RecipeHolder<?> getRecipeUsed() {
        return null;
    }
}
