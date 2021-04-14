package com.craftingdead.core.item.gun.simple;

import com.craftingdead.core.item.gun.AbstractGun;
import net.minecraft.item.ItemStack;

public class SimpleGun extends AbstractGun<SimpleGunType, SimpleGun> {

  public SimpleGun(SimpleGunType type, ItemStack gunStack) {
    super(type, gunStack);
  }
}
