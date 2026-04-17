/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.config.condition;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.conditions.ICondition;

public class GalacticraftGameModes implements ICondition {
    @Override
    public boolean test(IContext context) {
        return false;
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return null;
    }
}
