package com.craftingdead.core.item.gun.minigun;

import com.craftingdead.core.item.gun.AbstractGun;
import com.craftingdead.core.item.gun.simple.SimpleGunClient;
import com.craftingdead.core.living.ILiving;
import net.minecraft.util.math.MathHelper;

public class MinigunClient extends SimpleGunClient<AbstractGun<?, ?>> {

  private int lastBarrelRotation;
  private int barrelRotation;

  public MinigunClient(AbstractGun<?, ?> gun) {
    super(gun);
  }

  @Override
  public void handleTick(ILiving<?, ?> living) {
    super.handleTick(living);
    this.lastBarrelRotation = this.barrelRotation;
    if (this.gun.isPerformingRightMouseAction()) {
      this.barrelRotation += 50;
      if (this.barrelRotation >= 360) {
        this.barrelRotation = 0;
      }
    }
  }

  public float getBarrelRotation(float partialTicks) {
    return MathHelper.lerp(partialTicks, this.lastBarrelRotation, this.barrelRotation);
  }
}
