/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.config.condition;

import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.function.Supplier;

public abstract class ConfigCondition<R> implements ICondition {
    protected final Supplier<R> configValue;
    protected final R targetValue;

    protected ConfigCondition(Supplier<R> configValue, R targetValue) {
        this.configValue = configValue;
        this.targetValue = targetValue;
    }

    @Override
    public boolean test(IContext context) {
        return this.configValue.get() == this.targetValue;
    }
}
