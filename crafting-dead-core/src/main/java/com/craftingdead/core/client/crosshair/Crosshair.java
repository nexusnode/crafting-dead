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

package com.craftingdead.core.client.crosshair;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ResourceLocation;

public class Crosshair {

  public static final Codec<Crosshair> CODEC = RecordCodecBuilder.create(instance -> instance
      .group(
          ResourceLocation.CODEC.fieldOf("name").forGetter(Crosshair::getName),
          ResourceLocation.CODEC.fieldOf("top").forGetter(Crosshair::getTop),
          ResourceLocation.CODEC.fieldOf("bottom").forGetter(Crosshair::getBottom),
          ResourceLocation.CODEC.fieldOf("left").forGetter(Crosshair::getLeft),
          ResourceLocation.CODEC.fieldOf("right").forGetter(Crosshair::getRight),
          ResourceLocation.CODEC.fieldOf("middle").forGetter(Crosshair::getMiddle))
      .apply(instance, Crosshair::new));

  private final ResourceLocation name;

  private final ResourceLocation top;
  private final ResourceLocation bottom;
  private final ResourceLocation left;
  private final ResourceLocation right;
  private final ResourceLocation middle;

  public Crosshair(ResourceLocation name, ResourceLocation top,
      ResourceLocation bottom, ResourceLocation left, ResourceLocation right,
      ResourceLocation middle) {
    this.name = name;
    this.top = top;
    this.bottom = bottom;
    this.left = left;
    this.right = right;
    this.middle = middle;
  }

  public ResourceLocation getName() {
    return this.name;
  }

  public ResourceLocation getTop() {
    return this.top;
  }

  public ResourceLocation getBottom() {
    return this.bottom;
  }

  public ResourceLocation getLeft() {
    return this.left;
  }

  public ResourceLocation getRight() {
    return this.right;
  }

  public ResourceLocation getMiddle() {
    return this.middle;
  }
}
