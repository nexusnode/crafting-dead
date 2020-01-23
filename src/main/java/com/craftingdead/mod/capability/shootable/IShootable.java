package com.craftingdead.mod.capability.shootable;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public interface IShootable {

  void tick(ItemStack itemStack, Entity entity);

  void setTriggerPressed(ItemStack itemStack, Entity entity, boolean triggerPressed);

  boolean canReload(ItemStack itemStack, Entity entity);

  void finishReloading(ItemStack itemStack, Entity entity);

  int getReloadDuration();

  SoundEvent getReloadSound();
}
