/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.item.scope;

import java.util.Optional;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class SimpleScope implements Scope {

  private final float zoomMultiplier;
  private final Optional<ResourceLocation> overlayTexture;
  private final int overlayTextureWidth;
  private final int overlayTextureHeight;
  private final ItemStack itemStack;

  public SimpleScope(ItemStack itemStack) {
    this(1.0F, null, 0, 0, itemStack);
  }

  public SimpleScope(float zoomMultiplier, ResourceLocation overlayTexture,
      int overlayTextureWidth, int overlayTextureHeight, ItemStack itemStack) {
    this.zoomMultiplier = zoomMultiplier;
    this.overlayTexture = Optional.ofNullable(overlayTexture);
    this.overlayTextureWidth = overlayTextureWidth;
    this.overlayTextureHeight = overlayTextureHeight;
    this.itemStack = itemStack;
  }

  @Override
  public boolean isScoping(LivingExtension<?, ?> living) {
    return living.getEntity().getUseItem() == this.itemStack
        && !(living instanceof PlayerExtension<?> player && player.isHandcuffed());
  }

  @Override
  public float getZoomMultiplier(LivingExtension<?, ?> living) {
    return this.zoomMultiplier;
  }

  @Override
  public Optional<ResourceLocation> getOverlayTexture(LivingExtension<?, ?> living) {
    return this.overlayTexture;
  }

  @Override
  public int getOverlayTextureWidth() {
    return this.overlayTextureWidth;
  }

  @Override
  public int getOverlayTextureHeight() {
    return this.overlayTextureHeight;
  }
}
