package io.kalishak.galacticraftlegacy;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.GearInventory;
import io.kalishak.galacticraftlegacy.world.item.EnergyTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.AddAttributeTooltipsEvent;
import net.neoforged.neoforge.event.VanillaGameEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.InfiniteEnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public class NeoEventHandler {
    NeoEventHandler() {}

    private void gearTick(LivingEntity entity) {
        GearInventory inventory = entity.getData(GalacticraftAttachments.GEAR_INVENTORY);
        inventory.tick(entity);
    }

    @SubscribeEvent
    public void appendHoverText(AddAttributeTooltipsEvent event) {
        ItemStack stack = event.getStack();

        if (stack.getItemHolder().unwrapKey().filter(key -> key.identifier().getNamespace().equals(Galacticraft.MODID)).isPresent()) {
            EnergyHandler energyHandler = stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack));

            if (energyHandler != null && event.shouldShow()) {
                if (energyHandler instanceof InfiniteEnergyHandler) {
                    event.addTooltipLines(Component.translatable("item.galacticraftlegacy.infinite"));
                } else {
                    event.addTooltipLines(EnergyTooltip.addTooltip(energyHandler));
                }
            }

            ResourceHandler<FluidResource> fluidResource = stack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(stack));
            if (fluidResource != null && event.shouldShow()) {
                FluidResource resource = fluidResource.getResource(0);
                if (resource.isEmpty()) {
                    event.addTooltipLines(Component.translatable("item.galacticraftlegact.fluid_tank.empty"));
                } else {
                    event.addTooltipLines(Component.translatable(
                            "item.galacticraftlegacy.fluid_tank.tooltip",
                            Component.translatable(resource.getFluidType().getDescriptionId()),
                            Component.literal(fluidResource.getCapacityAsInt(0, resource) + "mB").withStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)))
                    );
                }
            }
        }
    }

    @SubscribeEvent
    public void playerCeased(VanillaGameEvent event) {
        if (event.getVanillaEvent() == GameEvent.ENTITY_DIE) {
            Entity entity = event.getContext().sourceEntity();
            Level level = event.getLevel();

            if (level instanceof ServerLevel serverLevel && !serverLevel.getGameRules().get(GameRules.KEEP_INVENTORY)) {
                if (entity instanceof LivingEntity livingEntity) {
                    GearInventory inventory = livingEntity.getData(GalacticraftAttachments.GEAR_INVENTORY);
                    inventory.dropAll(livingEntity, livingEntity instanceof Player);
                }
            }
        }
    }

    @SubscribeEvent
    public void gearTickEvent(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof LivingEntity livingEntity) {
            gearTick(livingEntity);
        }
    }

    @SubscribeEvent
    public void gearPlayerTickEvent(PlayerTickEvent.Pre event) {
        gearTick(event.getEntity());
    }
}
