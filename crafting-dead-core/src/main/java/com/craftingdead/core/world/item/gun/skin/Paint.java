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
