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
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class AimableGun extends GunImpl implements IScope {

  private boolean waitingForBoltAction;

  public AimableGun(IGunProvider gunProvider, ItemStack gunStack) {
    super(gunProvider, gunStack);
  }

  @Override
  protected IGunClient createClientHandler() {
    return DistExecutor.unsafeCallWhenOn(Dist.CLIENT,
        () -> () -> new AimableGunClient(this));
  }

  @Override
  public void reset(ILiving<?, ?> living) {
    super.reset(living);
    this.waitingForBoltAction = false;
  }

  @Override
  public void tick(ILiving<?, ?> living) {
    long timeDelta = Util.getMillis() - this.lastShotMs;
    if (timeDelta >= this.gunProvider.getFireDelayMs() && this.waitingForBoltAction) {
      this.waitingForBoltAction = false;
      if (!this.isPerformingRightMouseAction()) {
        this.setPerformingRightMouseAction(living, true, false);
      }
    }
    super.tick(living);
  }

  @Override
  protected void processShot(ILiving<?, ?> living) {
    super.processShot(living);
    if (this.isPerformingRightMouseAction() && this.gunProvider.hasBoltAction()) {
      this.setPerformingRightMouseAction(living, false, false);
      this.waitingForBoltAction = true;
    }
  }

  @Override
  public boolean isAiming(Entity entity) {
    return this.isPerformingRightMouseAction();
  }

  @Override
  public float getZoomMultiplier(Entity entity) {
    return this.hasIronSight() ? 2.0F
        : this.getAttachmentMultiplier(AttachmentItem.MultiplierType.ZOOM);
  }

  @Override
  public Optional<ResourceLocation> getOverlayTexture(Entity entity) {
    for (AttachmentItem attachmentItem : this.getAttachments()) {
      if (attachmentItem.isScope()) {
        return Optional.of(new ResourceLocation(attachmentItem.getRegistryName().getNamespace(),
            "textures/scope/" + attachmentItem.getRegistryName().getPath() + ".png"));
      }
    }
    return Optional.empty();
  }

  @Override
  public void setPerformingRightMouseAction(ILiving<?, ?> living,
      boolean performingRightMouseAction, boolean sendUpdate) {
    if (!this.waitingForBoltAction) {
      super.setPerformingRightMouseAction(living, performingRightMouseAction, sendUpdate);
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
