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
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public interface Scope {

  Capability<Scope> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

  boolean isScoping(LivingExtension<?, ?> living);

  float getZoomMultiplier(LivingExtension<?, ?> living);

  Optional<ResourceLocation> getOverlayTexture(LivingExtension<?, ?> living);

  int getOverlayTextureWidth();

  int getOverlayTextureHeight();

  default boolean shouldReduceMouseSensitivity(LivingExtension<?, ?> living) {
    return this.getZoomMultiplier(living) >= 5.0F;
  }
}
