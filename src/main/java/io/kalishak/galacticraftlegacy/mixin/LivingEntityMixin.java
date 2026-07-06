package io.kalishak.galacticraftlegacy.mixin;

import io.kalishak.galacticraftlegacy.world.entity.SpaceEntity;
import net.minecraft.core.Holder;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Shadow
    public abstract double getAttributeValue(Holder<Attribute> attribute);

    @Shadow
    protected abstract double calculateFallPower(double fallDistance);

    @Inject(method = "getDefaultGravity", at = @At("RETURN"), cancellable = true)
    private void galacticraftlegacy$getDefaultGravity(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue((double) SpaceEntity.getGravity(this.level()));
    }

    @Inject(method = "calculateFallDamage", at = @At("HEAD"), cancellable = true)
    private void galacticraftlegacy$calculateFallDamage(double fallDistance, float damageModifier, CallbackInfoReturnable<Integer> cir) {
        double modifier = getAttributeValue(Attributes.FALL_DAMAGE_MULTIPLIER);
        double moddedModifier = SpaceEntity.getFallDamageMultiplier(this.level());

        if (modifier != moddedModifier) {
            if (!this.is(EntityTypeTags.FALL_DAMAGE_IMMUNE)) {
                double baseDamage = this.calculateFallPower(fallDistance) * moddedModifier;
                cir.setReturnValue(Mth.floor(baseDamage * damageModifier * this.getAttributeValue(Attributes.FALL_DAMAGE_MULTIPLIER)));
            }
        }
    }
}
