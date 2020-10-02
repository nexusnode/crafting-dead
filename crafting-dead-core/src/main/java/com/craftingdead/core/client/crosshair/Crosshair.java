/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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

import net.minecraft.util.ResourceLocation;

public class Crosshair {

  private final ResourceLocation name;

  private final boolean isStatic;

  private final ResourceLocation top;
  private final ResourceLocation bottom;
  private final ResourceLocation left;
  private final ResourceLocation right;
  private final ResourceLocation middle;

  public Crosshair(ResourceLocation name, boolean isStatic, ResourceLocation top,
      ResourceLocation bottom, ResourceLocation left, ResourceLocation right,
      ResourceLocation middle) {
    this.name = name;
    this.isStatic = isStatic;
    this.top = top;
    this.bottom = bottom;
    this.left = left;
    this.right = right;
    this.middle = middle;
  }

  public ResourceLocation getName() {
    return name;
  }

  public boolean isStatic() {
    return isStatic;
  }

  public ResourceLocation getTop() {
    return top;
  }

  public ResourceLocation getBottom() {
    return bottom;
  }

  public ResourceLocation getLeft() {
    return left;
  }

  public ResourceLocation getRight() {
    return right;
  }

  public ResourceLocation getMiddle() {
    return middle;
  }
}
