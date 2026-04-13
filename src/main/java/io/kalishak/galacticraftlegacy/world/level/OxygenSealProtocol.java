package io.kalishak.galacticraftlegacy.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class OxygenSealProtocol extends Thread {
    public static AtomicBoolean isLoopInRunning = new AtomicBoolean();
    private static int buckets;
    private static int checkedSize;

    public AtomicBoolean isSealed = new AtomicBoolean();
    public AtomicBoolean isLooped = new AtomicBoolean();

    private Level level;
    private BlockPos startPos;
    private boolean sealFound;
    private List<BlockEntity> oxygenSealers;
    private int checkCount;
    private HashMap<BlockPos, BlockEntity> sealersAround;
    private List<BlockPos> currentLayer;
    private List<BlockPos> airToReplace;
    private List<BlockPos> fireToReplace;
    private List<BlockPos> breatheableToReplace;
    private List<BlockPos> airToReplaceBright;
    private List<BlockPos> breatheableToReplaceBright;
    private List<BlockPos> ambientThermalTracked;
    private List<BlockPos> ambientThermalTrackedBright;
    private List<BlockEntity> otherSealers;
    private List<BlockPos> torchesToUpdate;
    private boolean foundAmbientThermal;
    public List<BlockPos> leakTrace;

    public OxygenSealProtocol() {
        if (isAlive()) {
            interrupt();
        }

        start();
    }

    @Override
    public void run() {
        //check();
        OxygenSealProtocol.isLoopInRunning.set(false);
    }

    public static class CountedBucket {
        private int maxSize = 64;
        private int currentSize = 0;
        private int[] contents = new int[maxSize];

        public void add(int index) {
            if (contains(index)) {
                return;
            }

            if (this.currentSize >= this.maxSize)
            {
                int[] newTable = new int[this.maxSize + this.maxSize];
                System.arraycopy(this.contents, 0, newTable, 0, this.maxSize);
                this.contents = newTable;
                this.maxSize += maxSize;
            }
            this.contents[this.currentSize] = index;
            this.currentSize++;
            OxygenSealProtocol.checkedSize++;
        }

        public boolean contains(int index) {
            for (int i = this.currentSize - 1; i >= 0; i--) {
                if ((contents[i] & 0xFFFFFFF) == index) {
                    return true;
                }
            }

            return false;
        }

        public int getMSB4shifted(int test) {
            for (int i = this.currentSize - 1; i >= 0; i--) {
                if ((this.contents[i] & 0xFFFFFFF) == test) {
                    return (this.contents[i] & 0xF0000000) >> 22;
                }
            }

            return -1;
        }

        public void clear() {
            this.currentSize = 0;
        }

        public int getSize() {
            return this.currentSize;
        }

        public int[] getContents() {
            return this.contents;
        }
    }
}
