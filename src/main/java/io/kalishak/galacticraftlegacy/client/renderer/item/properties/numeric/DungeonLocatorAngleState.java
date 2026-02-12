package io.kalishak.galacticraftlegacy.client.renderer.item.properties.numeric;

import com.mojang.serialization.MapCodec;
import io.kalishak.galacticraftlegacy.world.item.component.GalacticraftDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.NeedleDirectionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class DungeonLocatorAngleState extends NeedleDirectionHelper {
    public static final MapCodec<DungeonLocatorAngleState> MAP_CODEC = MapCodec.unit(DungeonLocatorAngleState::new);
    private final NeedleDirectionHelper.Wobbler wobbler;
    private final NeedleDirectionHelper.Wobbler noTargetWobbler;
    private final RandomSource random = RandomSource.create();

    public DungeonLocatorAngleState() {
        super(true);
        this.wobbler = this.newWobbler(0.8F);
        this.noTargetWobbler = this.newWobbler(0.8F);
    }

    private float getRandomlySpinningRotation(int seed, long gameTime) {
        if (this.noTargetWobbler.shouldUpdate(gameTime)) {
            this.noTargetWobbler.update(gameTime, this.random.nextFloat());
        }

        float f = this.noTargetWobbler.rotation() + hash(seed) / 2.1474836E9F;
        return Mth.positiveModulo(f, 1.0F);
    }

    private float getRotationTowardsCompassTarget(ItemOwner owner, long gameTime, BlockPos pos) {
        float f = (float)getAngleFromEntityToPos(owner, pos);
        float f1 = getWrappedVisualRotationY(owner);
        float f2;
        if (owner.asLivingEntity() instanceof Player player && player.isLocalPlayer() && player.level().tickRateManager().runsNormally()) {
            if (this.wobbler.shouldUpdate(gameTime)) {
                this.wobbler.update(gameTime, 0.5F - (f1 - 0.25F));
            }

            f2 = f + this.wobbler.rotation();
        } else {
            f2 = 0.5F - (f1 - 0.25F - f);
        }

        return Mth.positiveModulo(f2, 1.0F);
    }

    @Override
    protected float calculate(ItemStack itemStack, ClientLevel clientLevel, int seed, ItemOwner itemOwner) {
        GlobalPos globalpos = itemStack.get(GalacticraftDataComponents.STRUCTURE_POS);
        long i = clientLevel.getGameTime();
        return !isValidCompassTargetPos(itemOwner, globalpos)
                ? getRandomlySpinningRotation(seed, i)
                : getRotationTowardsCompassTarget(itemOwner, i, globalpos.pos());
    }

    private static boolean isValidCompassTargetPos(ItemOwner owner, @Nullable GlobalPos pos) {
        return pos != null
                && pos.dimension() == owner.level().dimension()
                && !(pos.pos().distToCenterSqr(owner.position()) < 1.0E-5F);
    }

    private static double getAngleFromEntityToPos(ItemOwner owner, BlockPos pos) {
        Vec3 vec3 = Vec3.atCenterOf(pos);
        Vec3 vec31 = owner.position();
        return Math.atan2(vec3.z() - vec31.z(), vec3.x() - vec31.x()) / (float) (Math.PI * 2);
    }

    private static float getWrappedVisualRotationY(ItemOwner owner) {
        return Mth.positiveModulo(owner.getVisualRotationYInDegrees() / 360.0F, 1.0F);
    }

    private static int hash(int seed) {
        return seed * 1272137883;
    }
}
