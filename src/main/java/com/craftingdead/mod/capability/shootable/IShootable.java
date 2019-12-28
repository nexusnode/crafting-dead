package com.craftingdead.mod.capability.shootable;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface IShootable {

  void tick(ItemStack itemStack, Entity entity);

  void setTriggerPressed(ItemStack itemStack, Entity entity, boolean triggerPressed);

  void reload(ItemStack itemStack, Entity entity);
}
