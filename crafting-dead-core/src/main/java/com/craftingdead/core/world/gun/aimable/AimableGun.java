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

package com.craftingdead.core.world.gun.aimable;

import java.util.Optional;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.gun.AbstractGun;
import com.craftingdead.core.world.item.AttachmentItem;
import com.craftingdead.core.world.item.scope.Scope;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.concurrent.ThreadTaskExecutor;

public class AimableGun extends AbstractGun<AimableGunType, AimableGun> implements Scope {

  private boolean waitingForBoltAction;

  public AimableGun(AimableGunType properties, ItemStack gunStack) {
    super(properties, gunStack);
  }

  @Override
  public void reset(LivingExtension<?, ?> living) {
    super.reset(living);
    this.waitingForBoltAction = false;
  }

  @Override
  public void tick(LivingExtension<?, ?> living) {
    long timeDelta = Util.getMillis() - this.lastShotMs;
    if (timeDelta >= this.type.getFireDelayMs() && this.waitingForBoltAction) {
      this.waitingForBoltAction = false;
      if (!this.isPerformingRightMouseAction()) {
        this.setPerformingRightMouseAction(living, true, false);
      }
    }
    super.tick(living);
  }

  @Override
  protected void processShot(LivingExtension<?, ?> living, ThreadTaskExecutor<?> executor) {
    super.processShot(living, executor);
    executor.execute(() -> {
      if (this.isPerformingRightMouseAction() && this.type.hasBoltAction()) {
        this.setPerformingRightMouseAction(living, false, false);
        this.waitingForBoltAction = true;
      }
    });
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
  public void setPerformingRightMouseAction(LivingExtension<?, ?> living,
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
