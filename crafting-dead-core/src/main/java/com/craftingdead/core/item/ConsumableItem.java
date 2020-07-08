package com.craftingdead.core.item;

import javax.annotation.Nullable;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SimpleCapabilityProvider;
import com.craftingdead.core.capability.actionprovider.IActionProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ConsumableItem extends Item {

  private final IActionProvider actionProvider;
  private final int totalDurationTicks;

  public ConsumableItem(Properties properties) {
    super(properties);
    this.actionProvider = properties.actionProvider;
    this.totalDurationTicks = properties.totalDurationTicks;
  }

  @Override
  public boolean itemInteractionForEntity(ItemStack itemStack, PlayerEntity playerEntity,
      LivingEntity targetEntity, Hand hand) {
    if (!playerEntity.getEntityWorld().isRemote()) {
      playerEntity.setActiveHand(hand);
      this.performAction(playerEntity, targetEntity);
    }
    return false;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity playerEntity,
      Hand hand) {
    if (!playerEntity.getEntityWorld().isRemote()) {
      playerEntity.curePotionEffects(playerEntity.getHeldItem(hand));
      playerEntity.setActiveHand(hand);
      this.performAction(playerEntity, null);
    }
    return new ActionResult<>(ActionResultType.CONSUME, playerEntity.getHeldItem(hand));
  }

  private void performAction(LivingEntity performerEntity, LivingEntity targetEntity) {
    if (this.actionProvider != null) {
      performerEntity.getCapability(ModCapabilities.LIVING).ifPresent(
          performer -> this.actionProvider.getEntityAction(performer,
              targetEntity == null ? null
                  : targetEntity.getCapability(ModCapabilities.LIVING).orElse(null))
              .ifPresent(action -> performer.performAction(action, false, true)));
    }
  }

  @Override
  public int getUseDuration(ItemStack itemStack) {
    int useDuration = super.getUseDuration(itemStack);
    return useDuration == 0 ? this.totalDurationTicks : useDuration;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SimpleCapabilityProvider<>(this.actionProvider,
        () -> ModCapabilities.ACTION_PROVIDER);
  }

  public static class Properties extends Item.Properties {

    private IActionProvider actionProvider;
    private int totalDurationTicks = 32;

    public Properties setActionProvider(IActionProvider actionProvider) {
      this.actionProvider = actionProvider;
      return this;
    }

    public Properties setTotalDurationTicks(int totalDurationTicks) {
      this.totalDurationTicks = totalDurationTicks;
      return this;
    }
  }
}
