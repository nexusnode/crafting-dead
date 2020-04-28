package com.craftingdead.mod.item;

import java.util.Optional;
import java.util.Random;
import net.minecraft.nbt.CompoundNBT;

/**
 * An immutable ARGB color.
 */
public class Color {

  private final int alpha;
  private final int red;
  private final int green;
  private final int blue;

  private final int mergedValues;

  public Color(int alpha, int red, int green, int blue) {
    this.alpha = alpha;
    this.red = red;
    this.green = green;
    this.blue = blue;

    this.mergedValues = (this.alpha & 0xFF) << 24
        | (this.red & 0xFF) << 16
        | (this.green & 0xFF) << 8
        | (this.blue & 0xFF);
  }

  public int getAlpha() {
    return this.alpha;
  }

  public int getRed() {
    return this.red;
  }

  public int getGreen() {
    return this.green;
  }

  public int getBlue() {
    return this.blue;
  }

  /**
   * Gets the merged alpha, red, green and blue, respectively, as a single <code>int</code>.
   */
  public int getMergedValues() {
    return this.mergedValues;
  }

  @Override
  public String toString() {
    return "ItemColor [alpha=" + this.alpha + ", red=" + this.red + ", green=" + this.green
        + ", blue=" + this.blue + "]";
  }

  public CompoundNBT write(CompoundNBT compound) {
    CompoundNBT paintColorCompound = new CompoundNBT();
    paintColorCompound.putByte("a", (byte) (this.alpha & 0xFF));
    paintColorCompound.putByte("r", (byte) (this.red & 0xFF));
    paintColorCompound.putByte("g", (byte) (this.green & 0xFF));
    paintColorCompound.putByte("b", (byte) (this.blue & 0xFF));
    compound.put("paint_color", paintColorCompound);
    return compound;
  }

  public static Optional<Color> fromCompound(CompoundNBT compound) {
    if (compound.contains("paint_color")) {
      CompoundNBT paintColorCompound = compound.getCompound("paint_color");
      return Optional.of(new Color(paintColorCompound.getByte("a") & 0xFF,
          paintColorCompound.getByte("r") & 0xFF,
          paintColorCompound.getByte("g") & 0xFF,
          paintColorCompound.getByte("b") & 0xFF));
    }
    return Optional.empty();
  }

  public static Color createRandom() {
    Random random = new Random();

    return new Color((255 & 0xFF),
        (random.nextInt() & 0xFF),
        (random.nextInt() & 0xFF),
        (random.nextInt() & 0xFF));
  }
}
