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

package com.craftingdead.core.world.item.gun.aimable;

import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.gun.TypedGun;
import com.craftingdead.core.world.item.gun.attachment.Attachment;
import com.craftingdead.core.world.item.scope.Scope;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;

public final class AimableGun extends TypedGun<AimableGunItem> implements Scope {

  private boolean waitingForBoltAction;

  public static AimableGun create(Function<AimableGun, AimableGunClient> clientFactory,
      ItemStack itemStack, AimableGunItem type) {
    AimableGun gun = new AimableGun(clientFactory, itemStack, type);
    gun.initialize();
    return gun;
  }

  private AimableGun(Function<AimableGun, AimableGunClient> clientFactory, ItemStack itemStack,
      AimableGunItem type) {
    super(clientFactory, itemStack, type);
  }

  @Override
  public void reset(LivingExtension<?, ?> living) {
    super.reset(living);
    this.waitingForBoltAction = false;
  }

  @Override
  public void tick(LivingExtension<?, ?> living) {
    long timeDelta = Util.getMillis() - this.lastShotMs;
    if (timeDelta >= this.getType().getFireDelayMs() && this.waitingForBoltAction) {
      this.waitingForBoltAction = false;
      if (!this.isPerformingSecondaryAction()) {
        this.setPerformingSecondaryAction(living, true, false);
      }
    }
    super.tick(living);
  }

  @Override
  protected void processShot(LivingExtension<?, ?> living, Random random) {
    super.processShot(living, random);
    if (this.isPerformingSecondaryAction() && this.getType().hasBoltAction()) {
      this.setPerformingSecondaryAction(living, false, false);
      this.waitingForBoltAction = true;
    }
  }

  @Override
  public boolean isScoping(Entity entity) {
    return this.isPerformingSecondaryAction();
  }

  @Override
  public float getZoomMultiplier(Entity entity) {
    return this.hasIronSight()
        ? 2.0F
        : this.getAttachmentMultiplier(Attachment.MultiplierType.ZOOM);
  }

  @Override
  public Optional<ResourceLocation> getOverlayTexture(Entity entity) {
    for (Attachment attachment : this.getAttachments()) {
      if (attachment.isScope()) {
        return Optional.of(new ResourceLocation(attachment.getRegistryName().getNamespace(),
            "textures/scope/" + attachment.getRegistryName().getPath() + ".png"));
      }
    }
    return Optional.empty();
  }

  @Override
  public void setPerformingSecondaryAction(LivingExtension<?, ?> living,
      boolean performingRightMouseAction, boolean sendUpdate) {
    if (!this.waitingForBoltAction) {
      super.setPerformingSecondaryAction(living, performingRightMouseAction, sendUpdate);
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
