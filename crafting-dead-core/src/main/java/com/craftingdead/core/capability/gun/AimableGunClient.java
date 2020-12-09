package com.craftingdead.core.capability.gun;

import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.item.ItemStack;

public class AimableGunClient extends DefaultGunClient<AimableGun> {

  public AimableGunClient(AimableGun gun) {
    super(gun);
  }

  @Override
  protected boolean canFlash(ILiving<?, ?> living, ItemStack itemStack) {
    return super.canFlash(living, itemStack)
        && !this.gun.isAiming(living.getEntity(), itemStack);
  }
}
