package io.kalishak.galacticraftlegacy.mixin;

import io.kalishak.galacticraftlegacy.advancements.BedUsedInSpaceTrigger;
import io.kalishak.galacticraftlegacy.world.attribute.GalacticraftWorldAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public class BedBlockMixin {
    @Inject(method = "useWithoutItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/attribute/EnvironmentAttributeSystem;getValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;Lnet/minecraft/core/BlockPos;)Ljava/lang/Object;"), cancellable = true)
    private void galacticraftlegacy$useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        BedRule bedRule = level.environmentAttributes().getValue(EnvironmentAttributes.BED_RULE, pos);

        if (bedRule == GalacticraftWorldAttributes.BED_RULE_CRYO_CHAMBER) {
            bedRule.errorMessage().ifPresent(player::sendOverlayMessage);

            if (player instanceof ServerPlayer) {
                BedUsedInSpaceTrigger.TriggerInstance.triedToLayInBed(player, pos);
            }

            cir.setReturnValue(InteractionResult.SUCCESS_SERVER);
        } else cir.cancel();
    }
}
