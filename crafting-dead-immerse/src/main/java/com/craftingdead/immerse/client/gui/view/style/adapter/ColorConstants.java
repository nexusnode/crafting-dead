/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.view.style.adapter;

import java.util.Map;
import com.craftingdead.immerse.client.gui.view.Color;
import com.google.common.collect.ImmutableMap;

public class ColorConstants {

  private static final Map<String, Color> colors;

  static {
    var builder = ImmutableMap.<String, Color>builder();

    builder.put("transparent", Color.TRANSPARENT);
    builder.put("indianred", Color.fromHex("CD5C5C"));
    builder.put("lightcoral", Color.fromHex("F08080"));
    builder.put("salmon", Color.fromHex("FA8072"));
    builder.put("darksalmon", Color.fromHex("E9967A"));
    builder.put("lightsalmon", Color.fromHex("FFA07A"));
    builder.put("crimson", Color.fromHex("DC143C"));
    builder.put("red", Color.fromHex("FF0000"));
    builder.put("firebrick", Color.fromHex("B22222"));
    builder.put("darkred", Color.fromHex("8B0000"));
    builder.put("pink", Color.fromHex("FFC0CB"));
    builder.put("lightpink", Color.fromHex("FFB6C1"));
    builder.put("hotpink", Color.fromHex("FF69B4"));
    builder.put("deeppink", Color.fromHex("FF1493"));
    builder.put("mediumvioletred", Color.fromHex("C71585"));
    builder.put("palevioletred", Color.fromHex("DB7093"));
    builder.put("coral", Color.fromHex("FF7F50"));
    builder.put("tomato", Color.fromHex("FF6347"));
    builder.put("orangered", Color.fromHex("FF4500"));
    builder.put("darkorange", Color.fromHex("FF8C00"));
    builder.put("orange", Color.fromHex("FFA500"));
    builder.put("gold", Color.fromHex("FFD700"));
    builder.put("yellow", Color.fromHex("FFFF00"));
    builder.put("lightyellow", Color.fromHex("FFFFE0"));
    builder.put("lemonchiffon", Color.fromHex("FFFACD"));
    builder.put("lightgoldenrodyellow", Color.fromHex("FAFAD2"));
    builder.put("papayawhip", Color.fromHex("FFEFD5"));
    builder.put("moccasin", Color.fromHex("FFE4B5"));
    builder.put("peachpuff", Color.fromHex("FFDAB9"));
    builder.put("palegoldenrod", Color.fromHex("EEE8AA"));
    builder.put("khaki", Color.fromHex("F0E68C"));
    builder.put("darkkhaki", Color.fromHex("BDB76B"));
    builder.put("lavender", Color.fromHex("E6E6FA"));
    builder.put("thistle", Color.fromHex("D8BFD8"));
    builder.put("plum", Color.fromHex("DDA0DD"));
    builder.put("violet", Color.fromHex("EE82EE"));
    builder.put("orchid", Color.fromHex("DA70D6"));
    builder.put("fuchsia", Color.fromHex("FF00FF"));
    builder.put("magenta", Color.fromHex("FF00FF"));
    builder.put("mediumorchid", Color.fromHex("BA55D3"));
    builder.put("mediumpurple", Color.fromHex("9370DB"));
    builder.put("amethyst", Color.fromHex("9966CC"));
    builder.put("blueviolet", Color.fromHex("8A2BE2"));
    builder.put("darkviolet", Color.fromHex("9400D3"));
    builder.put("darkorchid", Color.fromHex("9932CC"));
    builder.put("darkmagenta", Color.fromHex("8B008B"));
    builder.put("purple", Color.fromHex("800080"));
    builder.put("indigo", Color.fromHex("4B0082"));
    builder.put("slateblue", Color.fromHex("6A5ACD"));
    builder.put("darkslateblue", Color.fromHex("483D8B"));
    builder.put("mediumslateblue", Color.fromHex("7B68EE"));
    builder.put("greenyellow", Color.fromHex("ADFF2F"));
    builder.put("chartreuse", Color.fromHex("7FFF00"));
    builder.put("lawngreen", Color.fromHex("7CFC00"));
    builder.put("lime", Color.fromHex("00FF00"));
    builder.put("limegreen", Color.fromHex("32CD32"));
    builder.put("palegreen", Color.fromHex("98FB98"));
    builder.put("lightgreen", Color.fromHex("90EE90"));
    builder.put("mediumspringgreen", Color.fromHex("00FA9A"));
    builder.put("springgreen", Color.fromHex("00FF7F"));
    builder.put("mediumseagreen", Color.fromHex("3CB371"));
    builder.put("seagreen", Color.fromHex("2E8B57"));
    builder.put("forestgreen", Color.fromHex("228B22"));
    builder.put("green", Color.fromHex("008000"));
    builder.put("darkgreen", Color.fromHex("006400"));
    builder.put("yellowgreen", Color.fromHex("9ACD32"));
    builder.put("olivedrab", Color.fromHex("6B8E23"));
    builder.put("olive", Color.fromHex("808000"));
    builder.put("darkolivegreen", Color.fromHex("556B2F"));
    builder.put("mediumaquamarine", Color.fromHex("66CDAA"));
    builder.put("darkseagreen", Color.fromHex("8FBC8F"));
    builder.put("lightseagreen", Color.fromHex("20B2AA"));
    builder.put("darkcyan", Color.fromHex("008B8B"));
    builder.put("teal", Color.fromHex("008080"));
    builder.put("aqua", Color.fromHex("00FFFF"));
    builder.put("cyan", Color.fromHex("00FFFF"));
    builder.put("lightcyan", Color.fromHex("E0FFFF"));
    builder.put("paleturquoise", Color.fromHex("AFEEEE"));
    builder.put("aquamarine", Color.fromHex("7FFFD4"));
    builder.put("turquoise", Color.fromHex("40E0D0"));
    builder.put("mediumturquoise", Color.fromHex("48D1CC"));
    builder.put("darkturquoise", Color.fromHex("00CED1"));
    builder.put("cadetblue", Color.fromHex("5F9EA0"));
    builder.put("steelblue", Color.fromHex("4682B4"));
    builder.put("lightsteelblue", Color.fromHex("B0C4DE"));
    builder.put("powderblue", Color.fromHex("B0E0E6"));
    builder.put("lightblue", Color.fromHex("ADD8E6"));
    builder.put("skyblue", Color.fromHex("87CEEB"));
    builder.put("lightskyblue", Color.fromHex("87CEFA"));
    builder.put("deepskyblue", Color.fromHex("00BFFF"));
    builder.put("dodgerblue", Color.fromHex("1E90FF"));
    builder.put("cornflowerblue", Color.fromHex("6495ED"));
    builder.put("royalblue", Color.fromHex("4169E1"));
    builder.put("blue", Color.fromHex("0000FF"));
    builder.put("mediumblue", Color.fromHex("0000CD"));
    builder.put("darkblue", Color.fromHex("00008B"));
    builder.put("navy", Color.fromHex("000080"));
    builder.put("midnightblue", Color.fromHex("191970"));
    builder.put("cornsilk", Color.fromHex("FFF8DC"));
    builder.put("blanchedalmond", Color.fromHex("FFEBCD"));
    builder.put("bisque", Color.fromHex("FFE4C4"));
    builder.put("navajowhite", Color.fromHex("FFDEAD"));
    builder.put("wheat", Color.fromHex("F5DEB3"));
    builder.put("burlywood", Color.fromHex("DEB887"));
    builder.put("tan", Color.fromHex("D2B48C"));
    builder.put("rosybrown", Color.fromHex("BC8F8F"));
    builder.put("sandybrown", Color.fromHex("F4A460"));
    builder.put("goldenrod", Color.fromHex("DAA520"));
    builder.put("darkgoldenrod", Color.fromHex("B8860B"));
    builder.put("peru", Color.fromHex("CD853F"));
    builder.put("chocolate", Color.fromHex("D2691E"));
    builder.put("saddlebrown", Color.fromHex("8B4513"));
    builder.put("sienna", Color.fromHex("A0522D"));
    builder.put("brown", Color.fromHex("A52A2A"));
    builder.put("maroon", Color.fromHex("800000"));
    builder.put("white", Color.fromHex("FFFFFF"));
    builder.put("snow", Color.fromHex("FFFAFA"));
    builder.put("honeydew", Color.fromHex("F0FFF0"));
    builder.put("mintcream", Color.fromHex("F5FFFA"));
    builder.put("azure", Color.fromHex("F0FFFF"));
    builder.put("aliceblue", Color.fromHex("F0F8FF"));
    builder.put("ghostwhite", Color.fromHex("F8F8FF"));
    builder.put("whitesmoke", Color.fromHex("F5F5F5"));
    builder.put("seashell", Color.fromHex("FFF5EE"));
    builder.put("beige", Color.fromHex("F5F5DC"));
    builder.put("oldlace", Color.fromHex("FDF5E6"));
    builder.put("floralwhite", Color.fromHex("FFFAF0"));
    builder.put("ivory", Color.fromHex("FFFFF0"));
    builder.put("antiquewhite", Color.fromHex("FAEBD7"));
    builder.put("linen", Color.fromHex("FAF0E6"));
    builder.put("lavenderblush", Color.fromHex("FFF0F5"));
    builder.put("mistyrose", Color.fromHex("FFE4E1"));
    builder.put("gainsboro", Color.fromHex("DCDCDC"));
    builder.put("lightgrey", Color.fromHex("D3D3D3"));
    builder.put("silver", Color.fromHex("C0C0C0"));
    builder.put("darkgray", Color.fromHex("A9A9A9"));
    builder.put("gray", Color.fromHex("808080"));
    builder.put("dimgray", Color.fromHex("696969"));
    builder.put("lightslategray", Color.fromHex("778899"));
    builder.put("slategray", Color.fromHex("708090"));
    builder.put("darkslategray", Color.fromHex("2F4F4F"));
    builder.put("black", Color.fromHex("000000"));

    colors = builder.buildOrThrow();
  }

  public static Color getColor(String name) {
    return colors.get(name.toLowerCase());
  }

  public static boolean hasConstant(String name) {
    return colors.containsKey(name.toLowerCase());
  }
}
