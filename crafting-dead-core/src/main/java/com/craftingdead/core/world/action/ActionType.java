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

package com.craftingdead.core.world.action;

import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface ActionType<T extends Action> extends IForgeRegistryEntry<ActionType<?>> {

  void encode(T action, FriendlyByteBuf out);

  T decode(LivingExtension<?, ?> performer, FriendlyByteBuf in);

  boolean isTriggeredByClient();

  default String makeDescriptionId() {
    return Util.makeDescriptionId("action", this.getRegistryName());
  }
}
