package com.craftingdead.mod.capability.shootable;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public class DefaultShootable implements IShootable {

  @Override
  public void tick(ItemStack itemStack, Entity entity) {}

  @Override
  public void setTriggerPressed(ItemStack itemStack, Entity entity, boolean triggerPressed) {}

  @Override
  public boolean canReload(ItemStack itemStack, Entity entity) {
    return false;
  }

  @Override
  public void reload(ItemStack itemStack, Entity entity) {}

  @Override
  public int getReloadDuration() {
    return 0;
  }

  @Override
  public SoundEvent getReloadSound() {
    return null;
  }
}
