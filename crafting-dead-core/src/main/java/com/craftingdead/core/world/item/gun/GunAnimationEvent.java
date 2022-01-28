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

package com.craftingdead.core.world.item.gun;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum GunAnimationEvent implements StringRepresentable {

  SHOOT("shoot"), RELOAD("reload"), INSPECT("inspect");

  public static final Codec<GunAnimationEvent> CODEC =
      StringRepresentable.fromEnum(GunAnimationEvent::values, GunAnimationEvent::byName);
  private static final Map<String, GunAnimationEvent> BY_NAME = Arrays.stream(values())
      .collect(Collectors.toMap(GunAnimationEvent::getSerializedName, Function.identity()));

  private final String name;

  private GunAnimationEvent(String name) {
    this.name = name;
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }

  public static GunAnimationEvent byName(String name) {
    return BY_NAME.get(name);
  }
}
