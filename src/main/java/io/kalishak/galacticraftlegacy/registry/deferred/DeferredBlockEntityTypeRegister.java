/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.registry.deferred;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.stream.Collectors;

public class DeferredBlockEntityTypeRegister extends DeferredRegister<BlockEntityType<?>> {
    protected DeferredBlockEntityTypeRegister(String namespace) {
        super(Registries.BLOCK_ENTITY_TYPE, namespace);
    }

    public static DeferredBlockEntityTypeRegister createBlockEntities(String namespace) {
        return new DeferredBlockEntityTypeRegister(namespace);
    }

    public <BE extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<BE>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<BE> supplier, Set<Holder<Block>> validBlocks) {
        return register(name, () -> new BlockEntityType<>(supplier, validBlocks.stream().map(Holder::value).collect(Collectors.toSet())));
    }

    public <BE extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<BE>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<BE> supplier, Holder<Block> validBlock) {
        return register(name, () -> new BlockEntityType<>(supplier, Set.of(validBlock.value())));
    }
}
