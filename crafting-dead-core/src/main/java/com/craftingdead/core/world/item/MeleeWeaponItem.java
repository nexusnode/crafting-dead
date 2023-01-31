/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.item;

import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.world.item.combatslot.CombatSlot;
import com.craftingdead.core.world.item.combatslot.CombatSlotProvider;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class MeleeWeaponItem extends ToolItem {

  private final int attackDamage;
  private final double attackSpeed;

  private final Multimap<Attribute, AttributeModifier> attributeModifiers;

  public MeleeWeaponItem(int attackDamage, double attackSpeed, Item.Properties properties) {
    super(properties);
    this.attackSpeed = attackSpeed;
    this.attackDamage = attackDamage;
    this.attributeModifiers = ImmutableMultimap.of(
        Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID,
            "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION),
        Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID,
            "Weapon modifier", this.attackSpeed, AttributeModifier.Operation.ADDITION));
  }

  @Override
  public Multimap<Attribute, AttributeModifier> getAttributeModifiers(
      EquipmentSlot equipmentSlot, ItemStack itemStack) {
    return equipmentSlot == EquipmentSlot.MAINHAND
        ? this.attributeModifiers
        : super.getAttributeModifiers(equipmentSlot, itemStack);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundTag nbt) {
    return CapabilityUtil.provider(() -> CombatSlot.MELEE, CombatSlotProvider.CAPABILITY);
  }
}
