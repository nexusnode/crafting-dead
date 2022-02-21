/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.item.gun.aimable;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.gun.TypedGun;
import com.craftingdead.core.world.item.gun.attachment.Attachment;
import com.craftingdead.core.world.item.scope.Scope;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

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
  protected void processShot(LivingExtension<?, ?> living) {
    super.processShot(living);
    if (this.isPerformingSecondaryAction() && this.getType().hasBoltAction()) {
      this.setPerformingSecondaryAction(living, false, false);
      this.waitingForBoltAction = true;
    }
  }

  @Override
  public boolean isScoping(LivingExtension<?, ?> living) {
    return this.isPerformingSecondaryAction();
  }

  @Override
  public float getZoomMultiplier(LivingExtension<?, ?> living) {
    return this.hasIronSight()
        ? 2.0F
        : this.getAttachmentMultiplier(Attachment.MultiplierType.ZOOM) + CraftingDead.serverConfig.scopeZoomMultiplier.get().floatValue();
  }

  @Override
  public float getAccuracy(LivingExtension<?, ?> living, Random random) {
    return super.getAccuracy(living, random) * (isScoping(living) ? CraftingDead.serverConfig.scopeZoomAccuracyMultiplier.get().floatValue() : 1F);
  }

  @Override
  public Optional<ResourceLocation> getOverlayTexture(LivingExtension<?, ?> living) {
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
