package com.craftingdead.mod.capability.shootable;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class DefaultShootable implements IShootable {

  @Override
  public void tick(ItemStack itemStack, Entity entity) {}

  @Override
  public void setTriggerPressed(ItemStack itemStack, Entity entity, boolean triggerPressed) {}

  @Override
  public void reload(ItemStack itemStack, Entity entity) {}
}
