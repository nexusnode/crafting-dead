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

import java.util.Optional;
import com.craftingdead.core.capability.Capabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;

public interface Paint {

  Optional<Integer> getColor();

  RegistryKey<Skin> getSkin();

  static boolean isValid(ItemStack gunStack, ItemStack itemStack) {
    return isValid(gunStack.getItem().getRegistryName(), itemStack);
  }

  static boolean isValid(ResourceLocation gunName, ItemStack itemStack) {
    return itemStack.getCapability(Capabilities.PAINT)
        .map(Paint::getSkin)
        .map(Skins.REGISTRY::get)
        .filter(skin -> skin.getAcceptedGuns().contains(gunName))
        .isPresent();
  }
}
