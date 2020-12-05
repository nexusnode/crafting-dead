/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core.item;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SimpleCapabilityProvider;
import com.craftingdead.core.capability.clothing.DefaultClothing;
import com.craftingdead.core.util.Text;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ClothingItem extends Item {

  private final boolean enhancedProtection;
  @Nullable
  private final Integer slownessAmplifier;
  private final boolean fireImmunity;

  public ClothingItem(Properties properties) {
    super(properties);
    this.enhancedProtection = properties.enhancedProtection;
    this.slownessAmplifier = properties.slownessAmplifier;
    this.fireImmunity = properties.fireImmunity;
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
    if (!worldIn.isRemote) {
      int randomRagAmount = random.nextInt(3) + 3;

      for (int i = 0; i < randomRagAmount; i++) {
        if (random.nextBoolean()) {
          entityLiving.entityDropItem(new ItemStack(ModItems.CLEAN_RAG::get));
        } else {
          entityLiving.entityDropItem(new ItemStack(ModItems.DIRTY_RAG::get));
        }
      }
    }

    if (entityLiving instanceof PlayerEntity && this.hasContainerItem(stack)) {
      ((PlayerEntity) entityLiving).addItemStackToInventory(this.getContainerItem(stack));
    }

    stack.shrink(1);
    return stack;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SimpleCapabilityProvider<>(
        new DefaultClothing(this.enhancedProtection, Optional.ofNullable(this.slownessAmplifier),
            this.fireImmunity,
            new ResourceLocation(this.getRegistryName().getNamespace(), "textures/models/clothing/"
                + this.getRegistryName().getPath() + "_" + "default" + ".png")),
        () -> ModCapabilities.CLOTHING);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
      Hand handIn) {
    ItemStack itemstack = playerIn.getHeldItem(handIn);
    playerIn.setActiveHand(handIn);
    return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
  }

  @Override
  public void addInformation(ItemStack stack, World world, List<ITextComponent> lines,
      ITooltipFlag tooltipFlag) {
    super.addInformation(stack, world, lines, tooltipFlag);
    ITextComponent armorLevelText = Text.of(this.enhancedProtection).mergeStyle(TextFormatting.RED);
    lines.add(Text
        .translate("item_lore.clothing.enhanced_protection")
        .mergeStyle(TextFormatting.GRAY)
        .append(armorLevelText));

    if (this.slownessAmplifier != null) {
      String potionNameAndLevel = I18n.format(Effects.SLOWNESS.getName()) + " "
          + I18n.format("enchantment.level." + (this.slownessAmplifier + 1));
      lines.add(Text.of(potionNameAndLevel).mergeStyle(TextFormatting.GRAY));
    }

    if (this.fireImmunity) {
      lines
          .add(Text.translate("item_lore.clothing.immune_to_fire").mergeStyle(TextFormatting.GRAY));
    }
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return 32;
  }

  @Override
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.BLOCK;
  }

  public static class Properties extends Item.Properties {

    private boolean enhancedProtection;
    private Integer slownessAmplifier;
    private boolean fireImmunity;

    public Properties setEnhancedProtection(boolean enhancedProtection) {
      this.enhancedProtection = enhancedProtection;
      return this;
    }

    public Properties setSlownessAmplifier(int slownessAmplifier) {
      this.slownessAmplifier = slownessAmplifier;
      return this;
    }

    public Properties setFireImmunity(boolean fireImmunity) {
      this.fireImmunity = fireImmunity;
      return this;
    }
  }
}
