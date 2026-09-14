package io.kalishak.galacticraftlegacy.world.level.block.entity.machine;

public interface MachineInstance {
    Properties getProperties();

    class Properties {
        private int energyCapacity = 25000;
        private int energyExtractionRate = 25;
        private int energyInsertionRate = 25;
        private int inventorySize;
        private int batterySlotIndex;
        private int tankCount = 1;
        private int tankCapacity;
        private int fluidExtractionRate = this.tankCapacity;
        private int fluidInsertionRate = this.tankCapacity;
        private int resultSlot;
        private int resultSlotCount = 1;
        private int inputSlot;
        private int inputSlotCount = 1;

        Properties() {
        }

        public static Properties of() {
            return new Properties();
        }

        public Properties energyCapacity(int energyCapacity) {
            this.energyCapacity = energyCapacity;
            return this;
        }

        public int getEnergyCapacity() {
            return this.energyCapacity;
        }

        public Properties energyExtractionRate(int energyExtractionRate) {
            this.energyExtractionRate = energyExtractionRate;
            return this;
        }

        public int getEnergyExtractionRate() {
            return this.energyExtractionRate;
        }

        public Properties energyInsertionRate(int energyInsertionRate) {
            this.energyInsertionRate = energyInsertionRate;
            return this;
        }

        public int getEnergyInsertionRate() {
            return this.energyInsertionRate;
        }

        public Properties energyTransferRate(int energyTransferRate) {
            energyExtractionRate(energyTransferRate);
            energyInsertionRate(energyTransferRate);
            return this;
        }

        public Properties inventorySize(int inventorySize) {
            this.inventorySize = inventorySize;
            return this;
        }

        public int getInventorySize() {
            return this.inventorySize;
        }

        public Properties batterySlotIndex(int batterySlotIndex) {
            this.batterySlotIndex = batterySlotIndex;
            return this;
        }

        public int getBatterySlotIndex() {
            return this.batterySlotIndex;
        }

        public Properties tankCount(int tankCount) {
            this.tankCount = tankCount;
            return this;
        }

        public int getTankCount() {
            return this.tankCount;
        }

        public Properties tankCapacity(int tankCapacity) {
            this.tankCapacity = tankCapacity;
            return this;
        }

        public int getTankCapacity() {
            return this.tankCapacity;
        }

        public Properties fluidExtractionRate(int fluidExtractionRate) {
            this.fluidExtractionRate = fluidExtractionRate;
            return this;
        }

        public int getFluidExtractionRate() {
            return this.fluidExtractionRate;
        }

        public Properties fluidInsertionRate(int fluidInsertionRate) {
            this.fluidInsertionRate = fluidInsertionRate;
            return this;
        }

        public int getFluidInsertionRate() {
            return this.fluidInsertionRate;
        }

        public Properties fluidTransferRate(int fluidTransferRate) {
            fluidExtractionRate(fluidTransferRate);
            fluidInsertionRate(fluidTransferRate);
            return this;
        }

        public Properties resultSlot(int resultSlot) {
            this.resultSlot = resultSlot;
            return this;
        }

        public int getResultSlot() {
            return this.resultSlot;
        }

        public Properties resultSlotCount(int resultSlotCount) {
            this.resultSlotCount = resultSlotCount;
            return this;
        }

        public int getResultSlotCount() {
            return this.resultSlotCount;
        }

        public int[] getResultSlots() {
            if (this.resultSlotCount < 1) {
                return new int[] { this.resultSlot };
            }

            int[] slots = new int[this.resultSlotCount];

            for (int i = 0; i < this.resultSlotCount; i++) {
                slots[i] = this.resultSlot + i;
            }

            return slots;
        }

        public Properties inputSlot(int inputSlot) {
            this.inputSlot = inputSlot;
            return this;
        }

        public int getInputSlot() {
            return this.inputSlot;
        }

        public Properties inputSlotCount(int inputSlotCount) {
            this.inputSlotCount = inputSlotCount;
            return this;
        }

        public int getInputSlotCount() {
            return this.inputSlotCount;
        }
    }
}
