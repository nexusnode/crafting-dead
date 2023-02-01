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

import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.world.item.equipment.Equipment;
import com.craftingdead.core.world.item.equipment.SimpleClothing;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ClothingItem extends EquipmentItem {

  public static final UUID ARMOR_MODIFIER_ID =
      UUID.fromString("4117e432-16f5-4eea-a4fe-127b54d39af1");

  private final Multimap<Attribute, AttributeModifier> attributeModifiers;
  private final boolean fireImmunity;
  private final boolean enhancesSwimming;

  public ClothingItem(Properties properties) {
    super(properties);
    this.attributeModifiers = properties.attributeModifiers.build();
    this.fireImmunity = properties.fireImmunity;
    this.enhancesSwimming = properties.enhancesSwimming;
  }

  @Override
  public void appendHoverText(ItemStack stack, Level world, List<Component> lines,
      TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, world, lines, tooltipFlag);
    if (this.fireImmunity) {
      lines.add(new TranslatableComponent("clothing.immune_to_fire")
          .withStyle(ChatFormatting.GRAY));
    }
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundTag nbt) {
    return CapabilityUtil.provider(
        () -> new SimpleClothing(this.attributeModifiers, this.fireImmunity, this.enhancesSwimming,
            new ResourceLocation(this.getRegistryName().getNamespace(), "textures/clothing/"
                + this.getRegistryName().getPath() + "_" + "default" + ".png")),
        Equipment.CAPABILITY);
  }

  public static class Properties extends Item.Properties {

    private ImmutableMultimap.Builder<Attribute, AttributeModifier> attributeModifiers =
        ImmutableMultimap.builder();
    private boolean fireImmunity;
    private boolean enhancesSwimming;

    public Properties attributeModifier(Attribute attribute, AttributeModifier modifier) {
      this.attributeModifiers.put(attribute, modifier);
      return this;
    }

    public Properties fireImmunity() {
      this.fireImmunity = true;
      return this;
    }

    public Properties enhancesSwimming() {
      this.enhancesSwimming = true;
      return this;
    }
  }
}
