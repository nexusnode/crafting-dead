/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.world.item;

import java.util.List;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.capability.SimpleCapabilityProvider;
import com.craftingdead.core.world.clothing.DefaultClothing;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class ClothingItem extends Item {

  private final Multimap<Attribute, AttributeModifier> attributeModifiers;
  private final boolean fireImmunity;

  public ClothingItem(Properties properties) {
    super(properties);
    this.attributeModifiers = properties.attributeModifiers.build();
    this.fireImmunity = properties.fireImmunity;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SimpleCapabilityProvider<>(
        LazyOptional.of(() -> new DefaultClothing(
            this.attributeModifiers,
            this.fireImmunity,
            new ResourceLocation(this.getRegistryName().getNamespace(), "textures/clothing/"
                + this.getRegistryName().getPath() + "_" + "default" + ".png"))),
        () -> Capabilities.CLOTHING);
  }

  @Override
  public void appendHoverText(ItemStack stack, World world, List<ITextComponent> lines,
      ITooltipFlag tooltipFlag) {
    super.appendHoverText(stack, world, lines, tooltipFlag);

    if (this.fireImmunity) {
      lines.add(new TranslationTextComponent("item_lore.clothing.immune_to_fire")
          .withStyle(TextFormatting.GRAY));
    }

    if (!this.attributeModifiers.isEmpty()) {
      lines.add(StringTextComponent.EMPTY);
      lines.add(
          new TranslationTextComponent("item.modifiers.clothing").withStyle(TextFormatting.GRAY));

      for (Entry<Attribute, AttributeModifier> entry : this.attributeModifiers.entries()) {
        AttributeModifier modifier = entry.getValue();
        double amount = modifier.getAmount();

        double multipliedAmount;
        if (modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE
            && modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
          if (entry.getKey().equals(Attributes.KNOCKBACK_RESISTANCE)) {
            multipliedAmount = amount * 10.0D;
          } else {
            multipliedAmount = amount;
          }
        } else {
          multipliedAmount = amount * 100.0D;
        }

        if (modifier.getId() == Item.BASE_ATTACK_DAMAGE_UUID
            || modifier.getId() == Item.BASE_ATTACK_SPEED_UUID) {
          lines.add((new StringTextComponent(" "))
              .append(new TranslationTextComponent(
                  "attribute.modifier.equals." + modifier.getOperation().toValue(),
                  ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(multipliedAmount),
                  new TranslationTextComponent(entry.getKey().getDescriptionId())))
              .withStyle(TextFormatting.DARK_GREEN));
        } else if (amount > 0.0D) {
          lines.add((new TranslationTextComponent(
              "attribute.modifier.plus." + modifier.getOperation().toValue(),
              ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(multipliedAmount),
              new TranslationTextComponent(entry.getKey().getDescriptionId())))
                  .withStyle(TextFormatting.BLUE));
        } else if (amount < 0.0D) {
          multipliedAmount = multipliedAmount * -1.0D;
          lines.add((new TranslationTextComponent(
              "attribute.modifier.take." + modifier.getOperation().toValue(),
              ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(multipliedAmount),
              new TranslationTextComponent(entry.getKey().getDescriptionId())))
                  .withStyle(TextFormatting.RED));
        }
      }
    }
  }

  public static class Properties extends Item.Properties {

    private ImmutableMultimap.Builder<Attribute, AttributeModifier> attributeModifiers =
        ImmutableMultimap.builder();
    private boolean fireImmunity;

    public Properties addAttributeModifier(Attribute attribute, AttributeModifier modifier) {
      this.attributeModifiers.put(attribute, modifier);
      return this;
    }

    public Properties setFireImmunity(boolean fireImmunity) {
      this.fireImmunity = fireImmunity;
      return this;
    }
  }
}
