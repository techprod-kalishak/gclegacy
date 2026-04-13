package io.kalishak.galacticraftlegacy.world.level.levelgen;

import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record MoonOceansDensityFunction() implements DensityFunction {
    @Override
    public double compute(FunctionContext functionContext) {
        return 0;
    }

    @Override
    public void fillArray(double[] doubles, ContextProvider contextProvider) {

    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return null;
    }

    @Override
    public double minValue() {
        return 0;
    }

    @Override
    public double maxValue() {
        return 0;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return null;
    }
}
