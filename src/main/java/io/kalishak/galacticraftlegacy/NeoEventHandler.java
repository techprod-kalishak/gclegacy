package io.kalishak.galacticraftlegacy;

import io.kalishak.galacticraftlegacy.attachment.AttachmentHelper;
import io.kalishak.galacticraftlegacy.attachment.entity.GearInventoryProvider;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import io.kalishak.galacticraftlegacy.world.item.component.*;
import io.kalishak.galacticraftlegacy.world.item.crafting.recipe.GalacticraftRecipeType;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.AttributeTooltipContext;
import net.neoforged.neoforge.event.AddAttributeTooltipsEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.VanillaGameEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class NeoEventHandler {
    NeoEventHandler() {}

    @SubscribeEvent
    public void updateRecipes(OnDatapackSyncEvent event) {
        event.sendRecipes(GalacticraftRecipeType.CIRCUIT.get());
    }

    @SubscribeEvent
    public void appendHoverText(AddAttributeTooltipsEvent event) {
        ItemStack stack = event.getStack();

        if (stack.getItemHolder().unwrapKey().filter(key -> key.identifier().getNamespace().equals(Galacticraft.MODID)).isPresent()) {
            if (event.shouldShow()) {
                addTooltipIfPresent(stack, GalacticraftDataComponents.getTooltipProviders(), event.getContext(), event::addTooltipLines);

                ItemAccess itemAccess = ItemAccess.forStack(stack);

                EnergyHandler energyHandler = stack.getCapability(Capabilities.Energy.ITEM, itemAccess);
                if (energyHandler != null) {
                    ItemAccessEnergyUtils.addTooltip(energyHandler, event::addTooltipLines);
                }

                ResourceHandler<FluidResource> fluidHandler = stack.getCapability(Capabilities.Fluid.ITEM, itemAccess);
                if (fluidHandler != null) {
                    ItemAccessFluidUtils.addTooltip(fluidHandler, event::addTooltipLines);
                }
            }
        }
    }

    @SubscribeEvent
    public void entityCeased(VanillaGameEvent event) {
        Entity entity = event.getContext().sourceEntity();

        if (entity != null && entity.getType().is(GalacticraftTags.EntityTypes.CAN_EQUIP_GEAR)) {
            if (!AttachmentHelper.hasGearInventory(entity)) {
                return;
            }

            if (event.getVanillaEvent() == GameEvent.ENTITY_DIE) {
                Level level = event.getLevel();
                GearInventoryProvider provider = AttachmentHelper.getGearInventory(entity);

                if ((entity instanceof Player && level instanceof ServerLevel serverLevel && !serverLevel.getGameRules().get(GameRules.KEEP_INVENTORY)) || entity instanceof LivingEntity) {
                    provider.dropAll((LivingEntity) entity);
                }
            }
        }
    }

    @SubscribeEvent
    public void entityTick(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();

        if (entity instanceof LivingEntity livingEntity && entity.getType().is(GalacticraftTags.EntityTypes.CAN_EQUIP_GEAR) && AttachmentHelper.hasGearInventory(entity)) {
            GearInventoryProvider gearInventoryProvider = AttachmentHelper.getGearInventory(entity);

            if (entity.level() instanceof ServerLevel serverLevel) {
                gearInventoryProvider.serverGearTick(serverLevel, livingEntity);
            } else {
                gearInventoryProvider.clientGearTick(entity.level(), livingEntity);
            }
        }
    }

    @SubscribeEvent
    public void handleEntityDamage(LivingIncomingDamageEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.getType().is(GalacticraftTags.EntityTypes.CAN_EQUIP_GEAR) && AttachmentHelper.hasGearInventory(entity)) {
            ItemStack shieldItem = AttachmentHelper.getGearInventory(entity).getStackBySlot(GearEquipmentSlot.SHIELD);

            if (!shieldItem.isEmpty() && entity.level() instanceof ServerLevel serverLevel) {
                event.setAmount(0.0F);

                ShieldController shieldController = shieldItem.get(GalacticraftDataComponents.SHIELD_CONTROLLER);

                if (shieldController != null) {
                    shieldController.depleteByValue(shieldItem, entity, serverLevel, (int) Math.floor(event.getOriginalAmount()));
                }
            }
        }
    }

    @SubscribeEvent
    public void playerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();

        if (AttachmentHelper.hasGearInventory(player)) {
            GearInventoryProvider gearInventory = AttachmentHelper.getGearInventory(player);

            if (player.level() instanceof ServerLevel serverLevel) {
                gearInventory.serverGearTick(serverLevel, player);
            } else {
                gearInventory.clientGearTick(player.level(), player);
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings("ConstantConditions")
    public void onEntityInteraction(PlayerInteractEvent.EntityInteract event) {
        Entity target = event.getTarget();
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack itemInHand = player.getItemInHand(hand);
        GearEquippable gearEquippable = itemInHand.get(GalacticraftDataComponents.GEAR_EQUIPPABLE);

        if (target instanceof LivingEntity livingEntity) {
            if (itemInHand.is(Tags.Items.TOOLS_SHEAR) && !(target instanceof Player)) {
                GearEquippable.shearFromTarget(player, itemInHand, hand, livingEntity, null);
            } else if (itemInHand.has(GalacticraftDataComponents.GEAR_EQUIPPABLE) && target.getType().is(GalacticraftTags.EntityTypes.CAN_EQUIP_GEAR)) {
                gearEquippable.equipOnTarget(player, livingEntity, itemInHand);
            }
        }
    }

    private static void addTooltipIfPresent(ItemStack itemStack, Stream<DataComponentType<?>> components, AttributeTooltipContext cxt, Consumer<Component> consumer) {
        components.forEach(dataComponentType -> {
            if (itemStack.has(dataComponentType)) {
                var component = itemStack.get(dataComponentType);

                if (component instanceof TooltipProvider tooltipProvider) {
                    tooltipProvider.addToTooltip(cxt, consumer, cxt.flag(), itemStack);
                }
            }
        });
    }
}
