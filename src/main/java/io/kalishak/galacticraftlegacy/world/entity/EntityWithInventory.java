/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public abstract class EntityWithInventory extends Entity {
    protected final ItemStacksResourceHandler items;

    public EntityWithInventory(EntityType<?> type, Level level, int inventorySize) {
        super(type, level);
        this.items = new ItemStacksResourceHandler(inventorySize);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        this.items.serialize(valueOutput);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        this.items.deserialize(valueInput);
    }
}
