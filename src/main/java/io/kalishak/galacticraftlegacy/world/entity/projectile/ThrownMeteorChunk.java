/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.world.entity.projectile;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.world.damagesource.GalacticraftDamageTypes;
import io.kalishak.galacticraftlegacy.world.entity.GalacticraftEntityType;
import io.kalishak.galacticraftlegacy.world.item.GalacticraftItems;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import io.kalishak.galacticraftlegacy.world.item.component.HotContent;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class ThrownMeteorChunk extends ThrowableItemProjectile {
    public ThrownMeteorChunk(EntityType<? extends ThrownMeteorChunk> type, Level level) {
        super(type, level);
    }

    public ThrownMeteorChunk(Level level, LivingEntity mob, ItemStack itemStack) {
        super(GalacticraftEntityType.THROWN_METEOR_CHUNK.get(), mob, level, itemStack);
    }

    @Override
    public void tick() {
        decrease(1);
        super.tick();

        if (isInWater() && isHot()) {
            for (int i = 0; i < 4; i++) {
                Vec3 mov = getDeltaMovement();

                double x = getX() - mov.x * 0.25F;
                double y = getY() - mov.y * 0.25F;
                double z = getZ() - mov.z * 0.25F;

                level().addParticle(ParticleTypes.BUBBLE, x, y, z, mov.x, mov.y, mov.z);

                decrease(5000);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity victim = hitResult.getEntity();

        if (victim instanceof LivingEntity livingVictim) {
            DamageSource source = level().damageSources().source(GalacticraftDamageTypes.METEOR_CHUNK, this, getOwner());

            if (isOnFire() && !livingVictim.is(EntityTypeTags.DEFLECTS_PROJECTILES)) {
                victim.setRemainingFireTicks(2);
            }

            if (level() instanceof ServerLevel serverLevel) {
                victim.hurtServer(serverLevel, source, isHot() ? 1.0F : 0.5F);
            }
        }
    }

    @Override
    protected Item getDefaultItem() {
        return GalacticraftItems.THROWABLE_METEOR_CHUNK.asItem();
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        applyImplicitComponentIfPresent(components, GalacticraftDataComponents.HOT_CONTENT.get());
    }

    @Override
    protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
        if (type == GalacticraftDataComponents.HOT_CONTENT.get()) {
            setData(GalacticraftAttachments.HOT_CONTENT, castComponentValue(GalacticraftDataComponents.HOT_CONTENT.get(), value));
            return true;
        }

        return super.applyImplicitComponent(type, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable <T> T get(DataComponentType<? extends T> type) {
        if (type == GalacticraftDataComponents.HOT_CONTENT.get()) {
            return castComponentValue((DataComponentType<T>) type, getData(GalacticraftAttachments.HOT_CONTENT));
        }

        return super.get(type);
    }

    public boolean isHot() {
        return getData(GalacticraftAttachments.HOT_CONTENT).getHeatLevel() > 0;
    }

    public void decrease(int amount) {
        HotContent content = getData(GalacticraftAttachments.HOT_CONTENT);

        if (content.getHeatLevel() > 0) {
            content.setHeatLevel(content.getHeatLevel() - amount);
            setData(GalacticraftAttachments.HOT_CONTENT, content);
        }
    }

    @Override
    public void playerTouch(Player player) {
        if (!level().isClientSide() && onGround()) {
            ItemStack itemStack = getItem();
            Item item = itemStack.getItem();

            if (player.getInventory().add(itemStack)) {

                player.take(this, 1);
                if (itemStack.isEmpty()) {
                    discard();
                    itemStack.setCount(1);
                }

                player.awardStat(Stats.ITEM_PICKED_UP.get(item), 1);
            }
        }
    }
}
