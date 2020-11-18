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
package com.craftingdead.immerse.client.shader;

import org.lwjgl.opengl.GL20;

public class RoundedRectShader extends ShaderProgram {

  public static final RoundedRectShader INSTANCE = new RoundedRectShader("rect", "rounded_rect");

  private RoundedRectShader(String vert, String frag) {
    super(vert, frag);
  }

  public void setRadius(float radius) {
    GL20.glUniform1f(0, radius);
  }

  public void setInnerRect(float left, float top, float right, float bottom) {
    GL20.glUniform4f(1, left, top, right, bottom);
  }
}
