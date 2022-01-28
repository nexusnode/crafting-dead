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
import java.util.function.BiFunction;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.SimpleCapabilityProvider;
import com.craftingdead.core.world.entity.grenade.Grenade;
import com.craftingdead.core.world.item.combatslot.CombatSlot;
import com.craftingdead.core.world.item.combatslot.CombatSlotProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class GrenadeItem extends Item {

  private final BiFunction<LivingEntity, Level, Grenade> grenadeEntitySupplier;
  private final float throwSpeed;

  public GrenadeItem(Properties properties) {
    super(properties);
    this.grenadeEntitySupplier = properties.grenadeEntitySupplier;
    this.throwSpeed = properties.throwSpeed;
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level world,
      List<Component> texts, TooltipFlag tooltipFlag) {
    texts.add(new TranslatableComponent("item_lore.grenade").withStyle(ChatFormatting.GRAY));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    var itemStack = player.getItemInHand(hand);
    level.playSound(null, player.getX(), player.getY(), player.getZ(),
        SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F,
        0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
    if (!level.isClientSide) {
      Grenade grenadeEntity = this.grenadeEntitySupplier.apply(player, level);

      float force = player.isShiftKeyDown() ? 0.4F : this.throwSpeed;
      grenadeEntity.teleportTo(player.getX(),
          player.getY() + player.getEyeHeight(),
          player.getZ());
      grenadeEntity.shootFromEntity(player, player.getXRot(), player.getYRot(), 0,
          force, 1.0F);
      level.addFreshEntity(grenadeEntity);
    }

    player.awardStat(Stats.ITEM_USED.get(this));
    if (!player.getAbilities().instabuild) {
      itemStack.shrink(1);
    }

    return InteractionResultHolder.success(itemStack);
  }


  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundTag nbt) {
    return new SimpleCapabilityProvider<>(
        LazyOptional.of(() -> () -> CombatSlot.GRENADE),
        () -> CombatSlotProvider.CAPABILITY);
  }

  public static class Properties extends Item.Properties {

    private BiFunction<LivingEntity, Level, Grenade> grenadeEntitySupplier;
    private float throwSpeed = 1.45F;

    public Properties setGrenadeEntitySupplier(
        BiFunction<LivingEntity, Level, Grenade> grenadeEntitySupplier) {
      this.grenadeEntitySupplier = grenadeEntitySupplier;
      return this;
    }

    public Properties setThrowSpeed(float throwSpeed) {
      this.throwSpeed = throwSpeed;
      return this;
    }
  }
}
