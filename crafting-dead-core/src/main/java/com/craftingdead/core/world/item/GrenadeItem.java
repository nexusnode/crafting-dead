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
import java.util.function.BiFunction;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.entity.grenade.Grenade;
import com.craftingdead.core.world.item.combatslot.CombatSlot;
import com.craftingdead.core.world.item.combatslot.CombatSlotProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
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
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class GrenadeItem extends Item {

  public static final GrenadeDispenserBehaviour dispenserBehaviour =
      new GrenadeDispenserBehaviour();
  private final BiFunction<@Nullable LivingEntity, Level, Grenade> grenadeEntitySupplier;
  private final float throwSpeed;
  private final Supplier<Boolean> enabled;
  private final boolean isSticky;

  public GrenadeItem(Properties properties) {
    super(properties);
    this.grenadeEntitySupplier = properties.grenadeEntitySupplier;
    this.throwSpeed = properties.throwSpeed;
    this.enabled = properties.enabled;
    this.isSticky = properties.isSticky;
    // Should be safe to be called here, minecraft does the same with some items
    DispenserBlock.registerBehavior(this, GrenadeItem.dispenserBehaviour);
  }

  @Override
  public void appendHoverText(ItemStack item, @Nullable Level level,
      List<Component> lines, TooltipFlag tooltipFlag) {
    lines.add(new TranslatableComponent("grenade.information").withStyle(ChatFormatting.GRAY));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    final var itemStack = player.getItemInHand(hand);

    if (!this.enabled.get()) {
      return InteractionResultHolder.pass(itemStack);
    }

    final var extension = PlayerExtension.getOrThrow(player);
    if (extension.isHandcuffed()) {
      return InteractionResultHolder.fail(itemStack);
    }

    level.playSound(null, player.getX(), player.getY(), player.getZ(),
        SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F,
        0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
    if (!level.isClientSide()) {
      var grenadeEntity = this.grenadeEntitySupplier.apply(player, level);

      float force = player.isShiftKeyDown() ? 0.4F : this.throwSpeed;
      grenadeEntity.teleportTo(player.getX(),
          player.getY() + player.getEyeHeight(),
          player.getZ());
      grenadeEntity.shootFromEntity(player, player.getXRot(), player.getYRot(), 0,
          force, 1.0F);
      grenadeEntity.setSticky(this.isSticky);
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
    return CapabilityUtil.provider(() -> CombatSlot.GRENADE, CombatSlotProvider.CAPABILITY);
  }

  public static class Properties extends Item.Properties {

    private BiFunction<@Nullable LivingEntity, Level, Grenade> grenadeEntitySupplier;
    private float throwSpeed = 1.45F;
    private Supplier<Boolean> enabled = () -> true;
    private boolean isSticky;

    public Properties setGrenadeEntitySupplier(
        BiFunction<@Nullable LivingEntity, Level, Grenade> grenadeEntitySupplier) {
      this.grenadeEntitySupplier = grenadeEntitySupplier;
      return this;
    }

    public Properties setThrowSpeed(float throwSpeed) {
      this.throwSpeed = throwSpeed;
      return this;
    }

    public Properties setEnabledSupplier(Supplier<Boolean> enabled) {
      this.enabled = enabled;
      return this;
    }

    public Properties setSticky(boolean isSticky) {
      this.isSticky = isSticky;
      return this;
    }
  }

  public static class GrenadeDispenserBehaviour extends DefaultDispenseItemBehavior {
    GrenadeDispenserBehaviour() {}

    @Override
    protected ItemStack execute(BlockSource source, ItemStack itemStack) {
      if (CraftingDead.serverConfig.explosivesDispenseGrenades.get()
          && itemStack.getItem() instanceof GrenadeItem grenadeItem) {
        var direction = source.getBlockState().getValue(DispenserBlock.FACING);
        var grenadeEntity = grenadeItem.grenadeEntitySupplier.apply(null, source.getLevel());
        var position = DispenserBlock.getDispensePosition(source);

        grenadeEntity.setPos(position.x(), position.y(), position.z());
        grenadeEntity.shoot(direction.getStepX(), direction.getStepY(), direction.getStepZ(),
            grenadeItem.throwSpeed, 1.0F);
        grenadeEntity.setSticky(grenadeItem.isSticky);
        source.getLevel().addFreshEntity(grenadeEntity);

        itemStack.shrink(1);
        return itemStack;
      } else {
        return super.execute(source, itemStack); // Drop the item instead
      }
    }

    @Override
    protected void playSound(BlockSource source) {
      source.getLevel().playSound(null, source.x(), source.y(), source.z(),
          SoundEvents.SNOWBALL_THROW, SoundSource.BLOCKS, 0.5F,
          0.4F / (source.getLevel().getRandom().nextFloat() * 0.4F + 0.8F));
    }
  }
}
