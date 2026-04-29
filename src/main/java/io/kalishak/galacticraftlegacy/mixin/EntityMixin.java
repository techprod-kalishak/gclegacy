package io.kalishak.galacticraftlegacy.mixin;

import io.kalishak.galacticraftlegacy.world.entity.SpaceEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract Level level();

    @Inject(method = "getDefaultGravity", at = @At("RETURN"), cancellable = true)
    private void galacticraftlegacy$getDefaultGravity(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue((double) SpaceEntity.getGravity(this.level()));
    }
}
