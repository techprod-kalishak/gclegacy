/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.data;

import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.SulfurCubeArchetype;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class GalacticraftSulfurCubeArchetypes {
    public static final ResourceKey<SulfurCubeArchetype> SPACY = Constants.key(Registries.SULFUR_CUBE_ARCHETYPE, "spacy");

    public static void bootstrap(BootstrapContext<SulfurCubeArchetype> cxt) {
        register(
                cxt,
                SPACY,
                GalacticraftTags.Items.SULFUR_CUBE_ARCHETYPE_SPACY,
                archetype(0.7F, 0.5F, 0.15F, 0.05F, 0.055F),
                false,
                knockBackHitScale(0.76F, 0.15F),
                soundSettings(SoundEvents.SULFUR_CUBE_REGULAR_HIT, SoundEvents.SULFUR_CUBE_REGULAR_PUSH, 0.25F, 0.9F)
        );
    }

    private static Function<ResourceKey<SulfurCubeArchetype>, SulfurCubeArchetype.AttributeEntry> add(Holder<Attribute> attribute, double amount) {
        return key -> SulfurCubeArchetype.AttributeEntry.add(attribute, amount, key);
    }

    private static Function<ResourceKey<SulfurCubeArchetype>, SulfurCubeArchetype.AttributeEntry> multiply(Holder<Attribute> attribute, double amount) {
        return key -> SulfurCubeArchetype.AttributeEntry.multiply(attribute, amount, key);
    }

    private static SulfurCubeArchetype.KnockbackModifiers knockBackHitScale(float horizontalPower, float verticalPower) {
        return new SulfurCubeArchetype.KnockbackModifiers(horizontalPower, verticalPower);
    }

    private static List<Function<ResourceKey<SulfurCubeArchetype>, SulfurCubeArchetype.AttributeEntry>> archetype(
            float speed, float bounce, float friction, float drag, float gravity
    ) {
        return List.of(
                add(Attributes.KNOCKBACK_RESISTANCE, -speed),
                add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, -speed),
                add(Attributes.BOUNCINESS, bounce),
                multiply(Attributes.FRICTION_MODIFIER, friction),
                multiply(Attributes.AIR_DRAG_MODIFIER, drag),
                add(Attributes.GRAVITY, -gravity)
        );
    }

    private static SulfurCubeArchetype.SoundSettings soundSettings(Holder<SoundEvent> hitSound, Holder<SoundEvent> pushSound, float threshold, float cooldown) {
        return new SulfurCubeArchetype.SoundSettings(hitSound, pushSound, threshold, cooldown);
    }

    private static void register(
            BootstrapContext<SulfurCubeArchetype> context,
            ResourceKey<SulfurCubeArchetype> name,
            TagKey<Item> blocks,
            List<Function<ResourceKey<SulfurCubeArchetype>, SulfurCubeArchetype.AttributeEntry>> modifiers,
            boolean floats,
            SulfurCubeArchetype.KnockbackModifiers knockbackModifiers,
            SulfurCubeArchetype.SoundSettings soundSettings
    ) {
        context.register(
                name,
                new SulfurCubeArchetype(
                        context.lookup(Registries.ITEM).getOrThrow(blocks),
                        modifiers.stream().map(f -> f.apply(name)).toList(),
                        floats,
                        Optional.empty(),
                        Optional.empty(),
                        knockbackModifiers,
                        soundSettings
                )
        );
    }
}
