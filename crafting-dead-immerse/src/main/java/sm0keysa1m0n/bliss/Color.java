package sm0keysa1m0n.bliss;

import java.util.Objects;
import org.jdesktop.core.animation.timing.Evaluator;
import org.jdesktop.core.animation.timing.evaluators.KnownEvaluators;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import com.google.common.base.Preconditions;
import net.minecraft.util.Mth;

public record Color(float[] value4f, int[] value4i, int valueHex) {

  static {
    KnownEvaluators.getInstance().register(new Evaluator<Color>() {

      @Override
      public Color evaluate(Color v0, Color v1, double fraction) {
        return Color.create(lerp((float) fraction, v0.value4f(), v1.value4f()));
      }

      @Override
      public Class<Color> getEvaluatorClass() {
        return Color.class;
      }
    });
  }

  public static final Color TRANSPARENT = create(0, 0, 0, 0);

  public static final Color BLACK = create(0, 0, 0);
  public static final Color DARK_BLUE = create(0, 0, 170);
  public static final Color DARK_GREEN = create(0, 170, 0);
  public static final Color DARK_AQUA = create(0, 170, 170);
  public static final Color DARK_RED = create(170, 0, 0);
  public static final Color DARK_PURPLE = create(170, 0, 170);
  public static final Color GOLD = create(255, 170, 0);
  public static final Color GRAY = create(170, 170, 170);
  public static final Color DARK_GRAY = create(85, 85, 85);
  public static final Color BLUE = create(85, 85, 255);
  public static final Color GREEN = create(85, 255, 85);
  public static final Color AQUA = create(85, 255, 255);
  public static final Color RED = create(255, 85, 85);
  public static final Color LIGHT_PURPLE = create(255, 85, 255);
  public static final Color YELLOW = create(255, 255, 85);
  public static final Color WHITE = create(255, 255, 255);
  public static final Color BLUE_C = create(170, 220, 240);
  public static final Color GRAY_224 = create(224, 224, 224);

  private static final Color[] VANILLA_COLORS =
      new Color[] {BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY,
          DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE};

  @ApiStatus.Internal
  public Color(float[] value4f, int[] value4i, int valueHex) {
    this.value4f = value4f;
    this.value4i = value4i;
    this.valueHex = valueHex;
  }

  public static Color parseWithFullAlpha(String hex) {
    return parseWithAlpha(hex, 1.0F);
  }

  public static Color parseWithAlpha(String hex, float alpha) {
    int padding = hex.startsWith("#") ? 1 : 0;
    var red = Integer.parseInt(hex.substring(padding, 2 + padding), 16) / 255.0F;
    var green = Integer.parseInt(hex.substring(2 + padding, 4 + padding), 16) / 255.0F;
    var blue = Integer.parseInt(hex.substring(4 + padding, 6 + padding), 16) / 255.0F;
    return create(red, green, blue, alpha);
  }

  public static Color createWithFullAlpha(int hex) {
    return createWithAlpha(hex, 255);
  }

  public static Color createWithAlpha(int hex, int alpha) {
    return create(hex + (alpha << 24));
  }

  public static Color create(int color) {
    var color4i = getColor4i(color);
    return new Color(getColor4f(color4i), color4i, color);
  }

  public static Color create(int red, int green, int blue) {
    return create(red, green, blue, 255);
  }

  public static Color create(int red, int green, int blue, int alpha) {
    return create(new int[] {red, green, blue, alpha});
  }

  public static Color create(int[] color) {
    Preconditions.checkArgument(color.length == 4, "color must have 4 components.");
    return new Color(getColor4f(color), color, getColorHex(color));
  }

  public static Color create(float red, float green, float blue) {
    return create(red, green, blue, 1.0F);
  }

  public static Color create(float red, float green, float blue, float alpha) {
    return create(new float[] {red, green, blue, alpha});
  }

  public static Color create(float[] color) {
    Preconditions.checkArgument(color.length == 4, "color must have 4 components.");
    var color4i = getColor4i(color);
    return new Color(color, color4i, getColorHex(color4i));
  }

  public int red() {
    return this.value4i[0];
  }

  public int green() {
    return this.value4i[1];
  }

  public int blue() {
    return this.value4i[2];
  }

  public int alpha() {
    return this.value4i[3];
  }

  @Override
  public float[] value4f() {
    float[] result = new float[4];
    System.arraycopy(this.value4f, 0, result, 0, 4);
    return result;
  }

  @Override
  public int[] value4i() {
    int[] result = new int[4];
    System.arraycopy(this.value4i, 0, result, 0, 4);
    return result;
  }

  @Override
  public int valueHex() {
    return this.valueHex;
  }

  public int multiplied(float alpha) {
    return multiplyAlpha(this.valueHex, alpha);
  }

  public boolean transparent() {
    return this.alpha() == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.valueHex);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Color other && other.valueHex == this.valueHex;
  }

  @Nullable
  public static Color getFormattingColor(int code) {
    if (code >= 0 && code <= 15) {
      return VANILLA_COLORS[code];
    }
    return null;
  }

  public static float lerp(float percent, float start, float end) {
    return start + percent * (end - start);
  }

  public static long lerp(float percent, int colour1, int colour2) {
    return getColorHex(lerp(percent, getColor4i(colour1), getColor4i(colour2)));
  }

  public static int[] lerp(float percent, int[] colour1, int[] colour2) {
    var rgba = new int[4];
    for (int i = 0; i < 4; i++) {
      rgba[i] = (int) lerp(percent, colour1[i], colour2[i]);
    }
    return rgba;
  }

  public static float[] lerp(float percent, float[] colour1, float[] colour2) {
    var rgba = new float[4];
    for (int i = 0; i < 4; i++) {
      rgba[i] = Mth.lerp(percent, colour1[i], colour2[i]);
    }
    return rgba;
  }

  public static int multiplyAlpha(int color, float alpha) {
    var newAlpha = (int) (((color >> 24) & 0xFF) * alpha);
    var rgb = color & 0x00FFFFFF;
    return ((newAlpha << 24) | rgb);
  }

  public static float[] getColor4f(int[] colour4i) {
    return new float[] {
        colour4i[0] / 255.0F,
        colour4i[1] / 255.0F,
        colour4i[2] / 255.0F,
        colour4i[3] / 255.0F};
  }

  public static int[] getColor4i(int color) {
    var rgba = new int[4];
    rgba[0] = (color >> 16) & 0xFF;
    rgba[1] = (color >> 8) & 0xFF;
    rgba[2] = (color >> 0) & 0xFF;
    rgba[3] = (color >> 24) & 0xFF;
    return rgba;
  }

  public static int[] getColor4i(float[] color4f) {
    return new int[] {
        (int) (color4f[0] * 255),
        (int) (color4f[1] * 255),
        (int) (color4f[2] * 255),
        (int) (color4f[3] * 255)};
  }

  public static int getColorHex(int[] color4i) {
    return ((color4i[3] & 0xFF) << 24)
        | ((color4i[0] & 0xFF) << 16)
        | ((color4i[1] & 0xFF) << 8)
        | ((color4i[2] & 0xFF) << 0);
  }
}
