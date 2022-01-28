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

package com.craftingdead.immerse.client.shader;

import com.craftingdead.immerse.CraftingDeadImmerse;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.ResourceLocation;

public class ShaderPrograms {

  public static final ShaderProgram RECT = new ShaderProgram("rect",
      ImmutableMap.of(
          Shader.Type.VERTEX, createLocation("rect.vert"),
          Shader.Type.FRAGMENT, createLocation("rect.frag")));

  public static final ShaderProgram ROUNDED_RECT = new ShaderProgram("rounded_rect",
      ImmutableMap.of(
          Shader.Type.VERTEX, createLocation("rect.vert"),
          Shader.Type.FRAGMENT, createLocation("rounded_rect.frag")));

  public static final ShaderProgram ROUNDED_TEX = new ShaderProgram("rounded_tex",
      ImmutableMap.of(
          Shader.Type.VERTEX, createLocation("tex.vert"),
          Shader.Type.FRAGMENT, createLocation("rounded_tex.frag")));

  private static ResourceLocation createLocation(String name) {
    return new ResourceLocation(CraftingDeadImmerse.ID, "shaders/" + name);
  }
}
