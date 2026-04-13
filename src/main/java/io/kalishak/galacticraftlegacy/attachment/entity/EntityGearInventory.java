package io.kalishak.galacticraftlegacy.attachment.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.kalishak.galacticraftlegacy.transfer.entity.SpaceGearEquipment;
import io.kalishak.galacticraftlegacy.world.entity.GearEquipmentSlot;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class EntityGearInventory extends GearInventoryProvider {
    public static final MapCodec<EntityGearInventory> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SpaceGearEquipment.CODEC.fieldOf("gear_equipment").forGetter(EntityGearInventory::getGearEquipment),
            Codec.INT.listOf(0, GearEquipmentSlot.values().length).xmap(IntArrayList::new, List::copyOf).fieldOf("guaranteed_drop_indexes").forGetter(inventory -> inventory.guaranteedDropIndexes)
    ).apply(instance, EntityGearInventory::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, EntityGearInventory> STREAM_CODEC = StreamCodec.composite(
            SpaceGearEquipment.STREAM_CODEC, EntityGearInventory::getGearEquipment,
            ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list()).map(IntArrayList::new, List::copyOf), inventory -> inventory.guaranteedDropIndexes,
            EntityGearInventory::new
    );

    private final IntArrayList guaranteedDropIndexes;

    private EntityGearInventory(SpaceGearEquipment gearEquipment, IntArrayList guaranteedDropIndexes) {
        super(gearEquipment);
        this.guaranteedDropIndexes = guaranteedDropIndexes;
    }

    public EntityGearInventory() {
        this(new SpaceGearEquipment(), new IntArrayList());
    }

    @Override
    public float getThermalArmorEffectiveness() {
        return 1.0F;
    }

    @Override
    public boolean mayBreath(LivingEntity livingEntity) {
        return livingEntity.getType().is(EntityTypeTags.UNDEAD) || super.mayBreath(livingEntity);
    }

    public void markGuaranteedDrop(int index) {
        this.guaranteedDropIndexes.add(index);
    }

    @Override
    public void dropAll(@NonNull LivingEntity entity) {
        if (!this.guaranteedDropIndexes.isEmpty()) {
            this.guaranteedDropIndexes.forEach(index -> entity.drop(ItemUtil.getStack(this.gearEquipment, index), true, false));
        }

        this.gearEquipment.clearContent();
    }

    public boolean shouldSave() {
        return !ResourceHandlerUtil.isEmpty(this.gearEquipment);
    }
}
