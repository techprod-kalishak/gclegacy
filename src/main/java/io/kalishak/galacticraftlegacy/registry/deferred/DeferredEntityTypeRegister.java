/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.registry.deferred;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class DeferredEntityTypeRegister extends DeferredRegister.Entities {
    protected DeferredEntityTypeRegister(String namespace) {
        super(namespace);
    }

    public static DeferredEntityTypeRegister createEntities(String namespace) {
        return new DeferredEntityTypeRegister(namespace);
    }

    public <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> registerEntityType(ResourceKey<EntityType<?>> name, EntityType.EntityFactory<E> factory, MobCategory category, UnaryOperator<EntityType.Builder<E>> builder) {
        return register(name.identifier().getPath(), () -> builder.apply(EntityType.Builder.of(factory, category)).build(name));
    }
}
