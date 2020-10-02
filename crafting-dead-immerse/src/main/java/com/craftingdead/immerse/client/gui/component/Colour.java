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
package com.craftingdead.immerse.client.gui.component;

import com.craftingdead.immerse.client.util.RenderUtil;

public class Colour {

  private int hexColour;
  private int[] colour4i = new int[4];
  private float[] colour4f = new float[4];

  public Colour() {
    this(0xFFFFFFFF);
  }

  public Colour(int value) {
    this.setHexColour(value);
  }

  public Colour(int[] value4i) {
    this.setColour4i(value4i);
  }

  public Colour(float[] value4f) {
    this.setColour4f(value4f);
  }

  public int getHexColour() {
    return this.hexColour;
  }

  public void setHexColour(int hex) {
    this.hexColour = hex;
    this.colour4i = RenderUtil.getColour4i(hex);
    this.colour4f = RenderUtil.getColour4f(this.colour4i);
  }

  public int[] getColour4i() {
    return this.colour4i;
  }

  public void setColour4i(int[] value4i) {
    System.arraycopy(value4i, 0, this.colour4i, 0, 4);
    this.hexColour = RenderUtil.getColour(value4i);
    this.colour4f = RenderUtil.getColour4f(this.colour4i);
  }

  public float[] getColour4f() {
    return this.colour4f;
  }

  public void setColour4f(float[] value4f) {
    System.arraycopy(value4f, 0, this.colour4f, 0, 4);
    this.colour4i = RenderUtil.getColour4i(this.colour4f);
    this.hexColour = RenderUtil.getColour(this.colour4i);
  }
}
