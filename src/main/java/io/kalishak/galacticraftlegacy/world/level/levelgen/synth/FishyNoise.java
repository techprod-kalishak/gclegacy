/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.levelgen.synth;

import net.minecraft.util.RandomSource;

public class FishyNoise {
    private final int[] perm = new int[512];
    private final double[][] GRADIENT_2D = {
            { 1.0D, 0.0D },
            { 0.9239000082015991D, 0.38269999623298645D },
            { 0.7071070075035095D, 0.7071070075035095D },
            { 0.38269999623298645D, 0.9239000082015991D },
            { 0.0D, 1.0D },
            { -0.38269999623298645D, 0.9239000082015991D },
            { -0.7071070075035095D, 0.7071070075035095D },
            { -0.9239000082015991D, 0.38269999623298645D },
            { -1.0D, 0.0D },
            { -0.9239000082015991D, -0.38269999623298645D },
            { -0.7071070075035095D, -0.7071070075035095D },
            { -0.38269999623298645D, -0.9239000082015991D },
            { 0.0D, -1.0D },
            { 0.38269999623298645D, -0.9239000082015991D },
            { 0.7071070075035095D, -0.7071070075035095D },
            { 0.9239000082015991D, -0.38269999623298645D }
    };
    private final int[][] GRADIENT = {
            { 1, 1, 0 },
            { -1, 1, 0 },
            { 1, -1, 0 },
            { -1, -1, 0 },
            { 1, 0, 1 },
            { -1, 0, 1 },
            { 1, 0, -1 },
            { -1, 0, -1 },
            { 0, 1, 1 },
            { 0, -1, 1 },
            { 0, 1, -1 },
            { 0, -1, -1 },
            { 1, 1, 0 },
            { -1, 1, 0 },
            { 0, -1, 1 },
            { 0, -1, -1 }
    };

    public FishyNoise(RandomSource random) {
        for (int i = 0; i < 256; i++) {
            this.perm[i] = i;
        }

        for (int i = 0; i < 256; i++) {
            final int j = random.nextInt(256);
            this.perm[i] = this.perm[i] ^ this.perm[j];
            this.perm[j] = this.perm[i] ^ this.perm[j];
            this.perm[i] = this.perm[i] ^ this.perm[j];
        }

        System.arraycopy(this.perm, 0, this.perm, 256, 256);
    }

    private double getCorner(double value) {
        return value * value * value * (value * (value * 6.0D - 15.0D) + 10.0D);
    }

    public double getValue(double xIn, double yIn) {
        int largeX = (xIn > 0.0D) ? (int) xIn : ((int) xIn - 1);
        int largeY = (yIn > 0.0D) ? (int) yIn : ((int) yIn - 1);
        xIn -= largeX;
        yIn -= largeY;
        largeX &= 0xFF;
        largeY &= 0xFF;
        double u = getCorner(xIn);
        double v = getCorner(yIn);
        int randY = this.perm[largeY] + largeX;
        int randY1 = this.perm[largeY + 1] + largeX;
        double[] grad2 = this.GRADIENT_2D[this.perm[randY] & 0xF];
        double grad00 = grad2[0] * xIn + grad2[1] * yIn;
        grad2 = this.GRADIENT_2D[this.perm[randY1] & 0xF];
        double grad01 = grad2[0] * xIn + grad2[1] * (yIn - 1.0D);
        grad2 = this.GRADIENT_2D[this.perm[1 + randY1] & 0xF];
        double grad11 = grad2[0] * (xIn - 1.0D) + grad2[1] * (yIn - 1.0D);
        grad2 = this.GRADIENT_2D[this.perm[1 + randY] & 0xF];
        double grad10 = grad2[0] * (xIn - 1.0D) + grad2[1] * yIn;
        double lerpX0 = grad00 + u * (grad10 - grad00);
        return lerpX0 + v * (grad01 + u * (grad11 - grad01) - lerpX0);
    }

    public double getValue(double xIn, double yIn, double zIn) {
        int unitX = (xIn > 0.0D) ? (int) xIn : ((int) xIn - 1);
        int unitY = (yIn > 0.0D) ? (int) yIn : ((int) yIn - 1);
        int unitZ = (zIn > 0.0D) ? (int) zIn : ((int) zIn - 1);
        xIn -= unitX;
        yIn -= unitY;
        zIn -= unitZ;
        unitX &= 0xFF;
        unitY &= 0xFF;
        unitZ &= 0xFF;
        double u = getCorner(xIn);
        double v = getCorner(yIn);
        double w = getCorner(zIn);
        int randZ = this.perm[unitZ] + unitY;
        int randZ1 = this.perm[unitZ + 1] + unitY;
        int randYZ = this.perm[randZ] + unitX;
        int randY1Z = this.perm[1 + randZ] + unitX;
        int randYZ1 = this.perm[randZ1] + unitX;
        int randY1Z1 = this.perm[1 + randZ1] + unitX;
        int[] grad3 = this.GRADIENT[this.perm[randYZ] & 0xF];
        double grad000 = grad3[0] * xIn + grad3[1] * yIn + grad3[2] * zIn;
        grad3 = this.GRADIENT[this.perm[1 + randYZ] & 0xF];
        double grad100 = grad3[0] * (xIn - 1.0D) + grad3[1] * yIn + grad3[2] * zIn;
        grad3 = this.GRADIENT[this.perm[randY1Z] & 0xF];
        double grad010 = grad3[0] * xIn + grad3[1] * (yIn - 1.0D) + grad3[2] * zIn;
        grad3 = this.GRADIENT[this.perm[1 + randY1Z] & 0xF];
        double grad110 = grad3[0] * (xIn - 1.0D) + grad3[1] * (yIn - 1.0D) + grad3[2] * zIn;
        zIn--;
        grad3 = this.GRADIENT[this.perm[randYZ1] & 0xF];
        double grad001 = grad3[0] * xIn + grad3[1] * yIn + grad3[2] * zIn;
        grad3 = this.GRADIENT[this.perm[1 + randYZ1] & 0xF];
        double grad101 = grad3[0] * (xIn - 1.0D) + grad3[1] * yIn + grad3[2] * zIn;
        grad3 = this.GRADIENT[this.perm[randY1Z1] & 0xF];
        double grad011 = grad3[0] * xIn + grad3[1] * (yIn - 1.0D) + grad3[2] * zIn;
        grad3 = this.GRADIENT[this.perm[1 + randY1Z1] & 0xF];
        double grad111 = grad3[0] * (xIn - 1.0D) + grad3[1] * (yIn - 1.0D) + grad3[2] * zIn;
        double f1 = grad000 + u * (grad100 - grad000);
        double f2 = grad010 + u * (grad110 - grad010);
        double f3 = grad001 + u * (grad101 - grad001);
        double f4 = grad011 + u * (grad111 - grad011);
        double lerp1 = f1 + v * (f2 - f1);
        return lerp1 + w * (f3 + v * (f4 - f3) - lerp1);
    }
}
