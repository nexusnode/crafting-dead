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

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.GunLike;
import com.craftingdead.core.world.item.gun.GunType;
import com.craftingdead.core.world.item.gun.TypedGun;
import com.craftingdead.core.world.item.gun.TypedGunClient;
import com.craftingdead.core.world.item.gun.attachment.Attachment;
import com.craftingdead.core.world.item.gun.magazine.Magazine;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;

@RegisterGunColor
public class GunItem extends ProjectileWeaponItem implements GunLike {

  private final Supplier<? extends GunType> type;

  protected GunItem(Supplier<? extends GunType> type) {
    super(new Properties().stacksTo(1).tab(ModItems.COMBAT_TAB));
    this.type = type;
  }

  @Override
  public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
    return itemStack -> this.type.get().getAcceptedMagazines()
        .stream()
        .anyMatch(itemStack.getItem()::equals);
  }

  // TODO: Maybe rework this - juanmuscaria
  public ICapabilityProvider initCapabilities(ItemStack itemStack, CompoundTag nbt) {
    return this.type.get().initCapabilities(itemStack, nbt);
  }

  protected <T extends TypedGun<?>> Function<T, TypedGunClient<? super T>> getClientFactory() {
    return this.type.get().getClientFactory();
  }

  @Override
  public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
    return true;
  }

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack,
      boolean slotChanged) {
    return oldStack.getItem() != newStack.getItem();
  }

  @Override
  public void appendHoverText(ItemStack itemStack, Level world,
      List<Component> lines, TooltipFlag tooltipFlag) {
    super.appendHoverText(itemStack, world, lines, tooltipFlag);

    itemStack.getCapability(Gun.CAPABILITY).ifPresent(gun -> {
      Component ammoCount =
          new TextComponent(String.valueOf(gun.getAmmoProvider().getMagazine()
              .map(Magazine::getSize)
              .orElse(0))).withStyle(ChatFormatting.RED);
      Component damageText =
          new TextComponent(String.valueOf(this.type.get().getDamage()))
              .withStyle(ChatFormatting.RED);
      //TODO: Perhaps add a headshot property instead of using a multiplier? - juanmuscaria
      Component headshotDamageText = new TextComponent(
          String.valueOf((int) (this.type.get().getDamage() * CraftingDead.serverConfig.headshotBonusDamage.get())))
              .withStyle(ChatFormatting.RED);
      Component accuracyText =
          new TextComponent((int) (this.type.get().getAccuracyPct() * 100D) + "%")
              .withStyle(ChatFormatting.RED);
      Component rpmText =
          new TextComponent(String.valueOf(this.type.get().getFireRateRPM()))
              .withStyle(ChatFormatting.RED);
      Component rangeText =
          new TextComponent(this.type.get().getRange() + " blocks")
              .withStyle(ChatFormatting.RED);

      lines.add(new TranslatableComponent("gun.ammo_amount")
          .withStyle(ChatFormatting.GRAY)
          .append(ammoCount));
      lines.add(new TranslatableComponent("gun.damage")
          .withStyle(ChatFormatting.GRAY)
          .append(damageText));
      lines.add(new TranslatableComponent("gun.headshot_damage")
          .withStyle(ChatFormatting.GRAY)
          .append(headshotDamageText));

      if (this.type.get().getRoundsPerShot() > 1) {
        Component pelletsText =
            new TextComponent(String.valueOf(this.type.get().getRoundsPerShot()))
                .withStyle(ChatFormatting.RED);

        lines.add(new TranslatableComponent("gun.pellets_shot")
            .withStyle(ChatFormatting.GRAY)
            .append(pelletsText));
      }

      for (Attachment attachment : gun.getAttachments()) {
        Component attachmentName = attachment.getDescription()
            .plainCopy()
            .withStyle(ChatFormatting.RED);
        lines.add(new TranslatableComponent("gun.attachment")
            .withStyle(ChatFormatting.GRAY)
            .append(attachmentName));
      }

      lines.add(new TranslatableComponent("gun.rpm")
          .withStyle(ChatFormatting.GRAY)
          .append(rpmText));
      lines.add(new TranslatableComponent("gun.accuracy")
          .withStyle(ChatFormatting.GRAY)
          .append(accuracyText));
      lines.add(new TranslatableComponent("gun.range")
          .withStyle(ChatFormatting.GRAY)
          .append(rangeText));
    });
  }

  @Override
  public boolean isEnchantable(ItemStack stack) {
    return true;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
    return enchantment == Enchantments.FLAMING_ARROWS
        || enchantment == Enchantments.POWER_ARROWS
        || enchantment == Enchantments.UNBREAKING
        || super.canApplyAtEnchantingTable(stack, enchantment);
  }

  @Override
  public int getEnchantmentValue() {
    return 1;
  }

  @Override
  public int getDefaultProjectileRange() {
    return 0;
  }

  @Override
  public GunType asGun() {
    return type.get();
  }

}
