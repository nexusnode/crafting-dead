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

package com.craftingdead.immerse.client.gui.component;

import javax.annotation.Nullable;
import com.craftingdead.immerse.client.util.RenderUtil;

public class Colour {

  public static final Colour BLACK = new Colour(0, 0, 0);
  public static final Colour DARK_BLUE = new Colour(0, 0, 170);
  public static final Colour DARK_GREEN = new Colour(0, 170, 0);
  public static final Colour DARK_AQUA = new Colour(0, 170, 170);
  public static final Colour DARK_RED = new Colour(170, 0, 0);
  public static final Colour DARK_PURPLE = new Colour(170, 0, 170);
  public static final Colour GOLD = new Colour(255, 170, 0);
  public static final Colour GRAY = new Colour(170, 170, 170);
  public static final Colour DARK_GRAY = new Colour(85, 85, 85);
  public static final Colour BLUE = new Colour(85, 85, 255);
  public static final Colour GREEN = new Colour(85, 255, 85);
  public static final Colour AQUA = new Colour(85, 255, 255);
  public static final Colour RED = new Colour(255, 85, 85);
  public static final Colour LIGHT_PURPLE = new Colour(255, 85, 255);
  public static final Colour YELLOW = new Colour(255, 255, 85);
  public static final Colour WHITE = new Colour(255, 255, 255);
  public static final Colour BLUE_C = new Colour(170, 220, 240);
  public static final Colour GRAY_224 = new Colour(224, 224, 224);

  private static final Colour[] VANILLA_COLORS =
      new Colour[] {BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY,
          DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE};

  private long hexColour;
  private int[] colour4i = new int[4];
  private float[] colour4f = new float[4];

  public Colour() {
    this(0xFFFFFFFF);
  }

  public Colour(Colour colour) {
    this(colour.hexColour);
  }

  public Colour(long value) {
    this.setHexColour(value);
  }

  public Colour(int r, int g, int b) {
    this(r, g, b, 255);
  }

  public Colour(int r, int g, int b, int a) {
    this(new int[] {r, g, b, a});
  }

  public Colour(int[] value4i) {
    this.setColour4i(value4i);
  }

  public Colour(float r, float g, float b, float a) {
    this(new float[] {r, g, b, a});
  }

  public Colour(float[] value4f) {
    this.setColour4f(value4f);
  }

  public long getHexColour() {
    return this.hexColour;
  }

  public void setHexColour(long hex) {
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

  @Nullable
  public static Colour getFormattingColor(int code) {
    if (code >= 0 && code <= 15) {
      return VANILLA_COLORS[code];
    }
    return null;
  }
}
