package sm0keysa1m0n.bliss.style.parser.value;

import java.util.Map;
import org.jetbrains.annotations.Nullable;
import com.google.common.collect.ImmutableMap;
import sm0keysa1m0n.bliss.Color;

public class NamedColors {

  private static final Map<String, Color> colors;

  static {
    var builder = ImmutableMap.<String, Color>builder();

    builder.put("transparent", Color.TRANSPARENT);
    builder.put("indianred", Color.createWithFullAlpha(0xCD5C5C));
    builder.put("lightcoral", Color.createWithFullAlpha(0xF08080));
    builder.put("salmon", Color.createWithFullAlpha(0xFA8072));
    builder.put("darksalmon", Color.createWithFullAlpha(0xE9967A));
    builder.put("lightsalmon", Color.createWithFullAlpha(0xFFA07A));
    builder.put("crimson", Color.createWithFullAlpha(0xDC143C));
    builder.put("red", Color.createWithFullAlpha(0xFF0000));
    builder.put("firebrick", Color.createWithFullAlpha(0xB22222));
    builder.put("darkred", Color.createWithFullAlpha(0x8B0000));
    builder.put("pink", Color.createWithFullAlpha(0xFFC0CB));
    builder.put("lightpink", Color.createWithFullAlpha(0xFFB6C1));
    builder.put("hotpink", Color.createWithFullAlpha(0xFF69B4));
    builder.put("deeppink", Color.createWithFullAlpha(0xFF1493));
    builder.put("mediumvioletred", Color.createWithFullAlpha(0xC71585));
    builder.put("palevioletred", Color.createWithFullAlpha(0xDB7093));
    builder.put("coral", Color.createWithFullAlpha(0xFF7F50));
    builder.put("tomato", Color.createWithFullAlpha(0xFF6347));
    builder.put("orangered", Color.createWithFullAlpha(0xFF4500));
    builder.put("darkorange", Color.createWithFullAlpha(0xFF8C00));
    builder.put("orange", Color.createWithFullAlpha(0xFFA500));
    builder.put("gold", Color.createWithFullAlpha(0xFFD700));
    builder.put("yellow", Color.createWithFullAlpha(0xFFFF00));
    builder.put("lightyellow", Color.createWithFullAlpha(0xFFFFE0));
    builder.put("lemonchiffon", Color.createWithFullAlpha(0xFFFACD));
    builder.put("lightgoldenrodyellow", Color.createWithFullAlpha(0xFAFAD2));
    builder.put("papayawhip", Color.createWithFullAlpha(0xFFEFD5));
    builder.put("moccasin", Color.createWithFullAlpha(0xFFE4B5));
    builder.put("peachpuff", Color.createWithFullAlpha(0xFFDAB9));
    builder.put("palegoldenrod", Color.createWithFullAlpha(0xEEE8AA));
    builder.put("khaki", Color.createWithFullAlpha(0xF0E68C));
    builder.put("darkkhaki", Color.createWithFullAlpha(0xBDB76B));
    builder.put("lavender", Color.createWithFullAlpha(0xE6E6FA));
    builder.put("thistle", Color.createWithFullAlpha(0xD8BFD8));
    builder.put("plum", Color.createWithFullAlpha(0xDDA0DD));
    builder.put("violet", Color.createWithFullAlpha(0xEE82EE));
    builder.put("orchid", Color.createWithFullAlpha(0xDA70D6));
    builder.put("fuchsia", Color.createWithFullAlpha(0xFF00FF));
    builder.put("magenta", Color.createWithFullAlpha(0xFF00FF));
    builder.put("mediumorchid", Color.createWithFullAlpha(0xBA55D3));
    builder.put("mediumpurple", Color.createWithFullAlpha(0x9370DB));
    builder.put("amethyst", Color.createWithFullAlpha(0x9966CC));
    builder.put("blueviolet", Color.createWithFullAlpha(0x8A2BE2));
    builder.put("darkviolet", Color.createWithFullAlpha(0x9400D3));
    builder.put("darkorchid", Color.createWithFullAlpha(0x9932CC));
    builder.put("darkmagenta", Color.createWithFullAlpha(0x8B008B));
    builder.put("purple", Color.createWithFullAlpha(0x800080));
    builder.put("indigo", Color.createWithFullAlpha(0x4B0082));
    builder.put("slateblue", Color.createWithFullAlpha(0x6A5ACD));
    builder.put("darkslateblue", Color.createWithFullAlpha(0x483D8B));
    builder.put("mediumslateblue", Color.createWithFullAlpha(0x7B68EE));
    builder.put("greenyellow", Color.createWithFullAlpha(0xADFF2F));
    builder.put("chartreuse", Color.createWithFullAlpha(0x7FFF00));
    builder.put("lawngreen", Color.createWithFullAlpha(0x7CFC00));
    builder.put("lime", Color.createWithFullAlpha(0x00FF00));
    builder.put("limegreen", Color.createWithFullAlpha(0x32CD32));
    builder.put("palegreen", Color.createWithFullAlpha(0x98FB98));
    builder.put("lightgreen", Color.createWithFullAlpha(0x90EE90));
    builder.put("mediumspringgreen", Color.createWithFullAlpha(0x00FA9A));
    builder.put("springgreen", Color.createWithFullAlpha(0x00FF7F));
    builder.put("mediumseagreen", Color.createWithFullAlpha(0x3CB371));
    builder.put("seagreen", Color.createWithFullAlpha(0x2E8B57));
    builder.put("forestgreen", Color.createWithFullAlpha(0x228B22));
    builder.put("green", Color.createWithFullAlpha(0x008000));
    builder.put("darkgreen", Color.createWithFullAlpha(0x006400));
    builder.put("yellowgreen", Color.createWithFullAlpha(0x9ACD32));
    builder.put("olivedrab", Color.createWithFullAlpha(0x6B8E23));
    builder.put("olive", Color.createWithFullAlpha(0x808000));
    builder.put("darkolivegreen", Color.createWithFullAlpha(0x556B2F));
    builder.put("mediumaquamarine", Color.createWithFullAlpha(0x66CDAA));
    builder.put("darkseagreen", Color.createWithFullAlpha(0x8FBC8F));
    builder.put("lightseagreen", Color.createWithFullAlpha(0x20B2AA));
    builder.put("darkcyan", Color.createWithFullAlpha(0x008B8B));
    builder.put("teal", Color.createWithFullAlpha(0x008080));
    builder.put("aqua", Color.createWithFullAlpha(0x00FFFF));
    builder.put("cyan", Color.createWithFullAlpha(0x00FFFF));
    builder.put("lightcyan", Color.createWithFullAlpha(0xE0FFFF));
    builder.put("paleturquoise", Color.createWithFullAlpha(0xAFEEEE));
    builder.put("aquamarine", Color.createWithFullAlpha(0x7FFFD4));
    builder.put("turquoise", Color.createWithFullAlpha(0x40E0D0));
    builder.put("mediumturquoise", Color.createWithFullAlpha(0x48D1CC));
    builder.put("darkturquoise", Color.createWithFullAlpha(0x00CED1));
    builder.put("cadetblue", Color.createWithFullAlpha(0x5F9EA0));
    builder.put("steelblue", Color.createWithFullAlpha(0x4682B4));
    builder.put("lightsteelblue", Color.createWithFullAlpha(0xB0C4DE));
    builder.put("powderblue", Color.createWithFullAlpha(0xB0E0E6));
    builder.put("lightblue", Color.createWithFullAlpha(0xADD8E6));
    builder.put("skyblue", Color.createWithFullAlpha(0x87CEEB));
    builder.put("lightskyblue", Color.createWithFullAlpha(0x87CEFA));
    builder.put("deepskyblue", Color.createWithFullAlpha(0x00BFFF));
    builder.put("dodgerblue", Color.createWithFullAlpha(0x1E90FF));
    builder.put("cornflowerblue", Color.createWithFullAlpha(0x6495ED));
    builder.put("royalblue", Color.createWithFullAlpha(0x4169E1));
    builder.put("blue", Color.createWithFullAlpha(0x0000FF));
    builder.put("mediumblue", Color.createWithFullAlpha(0x0000CD));
    builder.put("darkblue", Color.createWithFullAlpha(0x00008B));
    builder.put("navy", Color.createWithFullAlpha(0x000080));
    builder.put("midnightblue", Color.createWithFullAlpha(0x191970));
    builder.put("cornsilk", Color.createWithFullAlpha(0xFFF8DC));
    builder.put("blanchedalmond", Color.createWithFullAlpha(0xFFEBCD));
    builder.put("bisque", Color.createWithFullAlpha(0xFFE4C4));
    builder.put("navajowhite", Color.createWithFullAlpha(0xFFDEAD));
    builder.put("wheat", Color.createWithFullAlpha(0xF5DEB3));
    builder.put("burlywood", Color.createWithFullAlpha(0xDEB887));
    builder.put("tan", Color.createWithFullAlpha(0xD2B48C));
    builder.put("rosybrown", Color.createWithFullAlpha(0xBC8F8F));
    builder.put("sandybrown", Color.createWithFullAlpha(0xF4A460));
    builder.put("goldenrod", Color.createWithFullAlpha(0xDAA520));
    builder.put("darkgoldenrod", Color.createWithFullAlpha(0xB8860B));
    builder.put("peru", Color.createWithFullAlpha(0xCD853F));
    builder.put("chocolate", Color.createWithFullAlpha(0xD2691E));
    builder.put("saddlebrown", Color.createWithFullAlpha(0x8B4513));
    builder.put("sienna", Color.createWithFullAlpha(0xA0522D));
    builder.put("brown", Color.createWithFullAlpha(0xA52A2A));
    builder.put("maroon", Color.createWithFullAlpha(0x800000));
    builder.put("white", Color.WHITE);
    builder.put("snow", Color.createWithFullAlpha(0xFFFAFA));
    builder.put("honeydew", Color.createWithFullAlpha(0xF0FFF0));
    builder.put("mintcream", Color.createWithFullAlpha(0xF5FFFA));
    builder.put("azure", Color.createWithFullAlpha(0xF0FFFF));
    builder.put("aliceblue", Color.createWithFullAlpha(0xF0F8FF));
    builder.put("ghostwhite", Color.createWithFullAlpha(0xF8F8FF));
    builder.put("whitesmoke", Color.createWithFullAlpha(0xF5F5F5));
    builder.put("seashell", Color.createWithFullAlpha(0xFFF5EE));
    builder.put("beige", Color.createWithFullAlpha(0xF5F5DC));
    builder.put("oldlace", Color.createWithFullAlpha(0xFDF5E6));
    builder.put("floralwhite", Color.createWithFullAlpha(0xFFFAF0));
    builder.put("ivory", Color.createWithFullAlpha(0xFFFFF0));
    builder.put("antiquewhite", Color.createWithFullAlpha(0xFAEBD7));
    builder.put("linen", Color.createWithFullAlpha(0xFAF0E6));
    builder.put("lavenderblush", Color.createWithFullAlpha(0xFFF0F5));
    builder.put("mistyrose", Color.createWithFullAlpha(0xFFE4E1));
    builder.put("gainsboro", Color.createWithFullAlpha(0xDCDCDC));
    builder.put("lightgrey", Color.createWithFullAlpha(0xD3D3D3));
    builder.put("silver", Color.createWithFullAlpha(0xC0C0C0));
    builder.put("darkgray", Color.createWithFullAlpha(0xA9A9A9));
    builder.put("gray", Color.createWithFullAlpha(0x808080));
    builder.put("dimgray", Color.createWithFullAlpha(0x696969));
    builder.put("lightslategray", Color.createWithFullAlpha(0x778899));
    builder.put("slategray", Color.createWithFullAlpha(0x708090));
    builder.put("darkslategray", Color.createWithFullAlpha(0x2F4F4F));
    builder.put("black", Color.BLACK);

    colors = builder.buildOrThrow();
  }

  @Nullable
  public static Color getColor(String name) {
    return colors.get(name.toLowerCase());
  }
}
