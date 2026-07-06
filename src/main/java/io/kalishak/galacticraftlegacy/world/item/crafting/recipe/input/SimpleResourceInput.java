/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.item.crafting.recipe.input;

import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.function.Supplier;

public class SimpleResourceInput extends RangedResourceHandler<ItemResource> implements ResourceHandlerInput {
    public SimpleResourceInput(Supplier<ResourceHandler<ItemResource>> delegate, int start, int end) {
        super(delegate, start, end);
    }
}
