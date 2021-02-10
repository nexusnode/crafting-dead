/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.capability.gun;

import java.util.Optional;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.capability.scope.IScope;
import com.craftingdead.core.item.AttachmentItem;
import com.craftingdead.core.item.GunItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class AimableGun extends DefaultGun implements IScope {

  private boolean waitingForBoltAction;

  public AimableGun(GunItem gunItem) {
    super(gunItem);
  }

  @Override
  protected IGunClient createClientHandler() {
    return DistExecutor.unsafeCallWhenOn(Dist.CLIENT,
        () -> () -> new AimableGunClient(this));
  }

  @Override
  public void reset(ILiving<?, ?> living, ItemStack itemStack) {
    super.reset(living, itemStack);
    this.waitingForBoltAction = false;
  }

  @Override
  public void tick(ILiving<?, ?> living, ItemStack itemStack) {
    long timeDelta = Util.milliTime() - super.getLastShotMs();
    if (timeDelta >= this.gunItem.getFireRateMs() && this.waitingForBoltAction) {
      this.waitingForBoltAction = false;
      if (!this.isPerformingRightMouseAction()) {
        this.toggleRightMouseAction(living, false);
      }
    }
    super.tick(living, itemStack);
  }

  @Override
  protected void processShot(ILiving<?, ?> living, ItemStack itemStack) {
    super.processShot(living, itemStack);
    if (this.isPerformingRightMouseAction() && this.gunItem.hasBoltAction()) {
      this.toggleRightMouseAction(living, false);
      this.waitingForBoltAction = true;
    }
  }

  @Override
  public boolean isAiming(Entity entity, ItemStack itemStack) {
    return this.isPerformingRightMouseAction();
  }

  @Override
  public float getZoomMultiplier(Entity entity, ItemStack itemStack) {
    return this.hasIronSight() ? 2.0F
        : this.getAttachmentMultiplier(AttachmentItem.MultiplierType.ZOOM);
  }

  @Override
  public Optional<ResourceLocation> getOverlayTexture(Entity entity, ItemStack itemStack) {
    for (AttachmentItem attachmentItem : this.getAttachments()) {
      if (attachmentItem.isScope()) {
        return Optional.of(new ResourceLocation(attachmentItem.getRegistryName().getNamespace(),
            "textures/scope/" + attachmentItem.getRegistryName().getPath() + ".png"));
      }
    }
    return Optional.empty();
  }

  @Override
  public void toggleRightMouseAction(ILiving<?, ?> living, boolean sendUpdate) {
    if (!this.waitingForBoltAction) {
      super.toggleRightMouseAction(living, sendUpdate);
    }
  }

  @Override
  public int getOverlayTextureWidth() {
    return 2048;
  }

  @Override
  public int getOverlayTextureHeight() {
    return 512;
  }
}
