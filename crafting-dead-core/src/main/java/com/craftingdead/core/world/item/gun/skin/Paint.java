/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.core.world.item.gun.skin;

import java.util.OptionalInt;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public interface Paint {

  Capability<Paint> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

  ResourceKey<Skin> getSkin();

  default OptionalInt getColor() {
    return OptionalInt.empty();
  }

  static Paint of(ResourceKey<Skin> skin) {
    return () -> skin;
  }

  static Paint of(ResourceKey<Skin> skin, int color) {
    return of(skin, OptionalInt.of(color));
  }

  static Paint of(ResourceKey<Skin> skin, OptionalInt color) {
    return new Paint() {

      @Override
      public OptionalInt getColor() {
        return color;
      }

      @Override
      public ResourceKey<Skin> getSkin() {
        return skin;
      }
    };
  }

  static boolean isValid(ItemStack gunStack, ItemStack itemStack) {
    return isValid(gunStack.getItem().getRegistryName(), itemStack);
  }

  static boolean isValid(ResourceLocation gunName, ItemStack itemStack) {
    return itemStack.getCapability(Paint.CAPABILITY)
        .map(Paint::getSkin)
        .map(Skins.REGISTRY::get)
        .filter(skin -> skin.getAcceptedGuns().contains(gunName))
        .isPresent();
  }
}
