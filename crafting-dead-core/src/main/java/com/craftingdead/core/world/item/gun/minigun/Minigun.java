package com.craftingdead.core.world.item.gun.minigun;

import com.craftingdead.core.world.item.GunItem;
import com.craftingdead.core.world.item.gun.AbstractGunClient;
import com.craftingdead.core.world.item.gun.TypedGun;
import net.minecraft.world.item.ItemStack;

public class Minigun extends TypedGun {

  public Minigun(ItemStack itemStack, GunItem item) {
    super(itemStack, item);
  }

  @Override
  protected AbstractGunClient<?> createClient() {
    return new MinigunClient(this);
  }
}
