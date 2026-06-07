/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.level.block.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.transfer.ResourcefulHelper;
import io.kalishak.galacticraftlegacy.world.inventory.CraftingContainerWrapper;
import io.kalishak.galacticraftlegacy.world.inventory.MagneticCraftingMenu;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.EmptyResourceHandler;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class MagneticCraftingBlockEntity extends BlockEntity implements MenuProvider, CraftingContainerWrapper, RecipeCraftingHolder {
    public static final Codec<RecipeHolder<?>> RECIPE_HOLDER_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.RECIPE).fieldOf("id").forGetter(RecipeHolder::id),
            Recipe.CODEC.fieldOf("value").forGetter(RecipeHolder::value)
    ).apply(instance, RecipeHolder::new));
    private final ItemStacksResourceHandler items = new ItemStacksResourceHandler(10);
    private @Nullable RecipeHolder<?> recipeUsed;

    public MagneticCraftingBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(GalacticraftBlockEntityType.MAGNETIC_CRAFTING.get(), worldPosition, blockState);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                GalacticraftBlockEntityType.MAGNETIC_CRAFTING.get(),
                (blockEntity, context) -> switch (context) {
                    case UP -> RangedResourceHandler.of(blockEntity.items, 1, 10);
                    case DOWN -> RangedResourceHandler.ofSingleIndex(blockEntity.items, 0);
                    case null -> blockEntity.items;
                    default -> EmptyResourceHandler.instance();
                }
        );
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.galacticraftlegacy.magnetic_crafting_table");
    }

    @Override
    public ResourceHandler<ItemResource> getHandler() {
        return this.items;
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 3;
    }

    public void set(int index, ItemResource resource, int count) {
        this.items.set(index, resource, count);
    }

    @Override
    public void fillStackedContents(StackedItemContents contents) {
        this.items.copyToList().forEach(contents::accountSimpleStack);
    }

    public void refreshRecipeResult(@Nullable RecipeHolder<CraftingRecipe> recipeHint, Consumer<ItemStack> stackConsumer) {
        if (this.level instanceof ServerLevel serverLevel) {
            CraftingInput craftInput = asCraftInput();
            serverLevel.recipeAccess()
                    .getRecipeFor(RecipeType.CRAFTING, craftInput, serverLevel, recipeHint)
                    .ifPresent(recipe -> {
                        setRecipeUsed(recipe);
                        ItemStack result = recipe.value().assemble(craftInput);
                        stackConsumer.accept(result);
                    });
        }
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return removeItemNoUpdate(slot);
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipeUsed) {
        this.recipeUsed = recipeUsed;
    }

    @Override
    public @Nullable RecipeHolder<?> getRecipeUsed() {
        return this.recipeUsed;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.items.serialize(output);

        if (this.recipeUsed != null) {
            output.store("LastRecipe", RECIPE_HOLDER_CODEC, this.recipeUsed);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.items.deserialize(input);
        input.read("LastRecipe", RECIPE_HOLDER_CODEC).ifPresent(this::setRecipeUsed);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        ResourcefulHelper.collectContainerComponent(components, this.items);
        components.set(GalacticraftDataComponents.RECIPE_HOLDER, this.recipeUsed);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        ResourcefulHelper.applyContainerComponent(components, this::set);
        this.recipeUsed = components.get(GalacticraftDataComponents.RECIPE_HOLDER);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new MagneticCraftingMenu(containerId, inventory, player, this);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (this.level != null) {
            Containers.dropContents(this.level, pos, this.items.copyToList());
        }
    }
}
