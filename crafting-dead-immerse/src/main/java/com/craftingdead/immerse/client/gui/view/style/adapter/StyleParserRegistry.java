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

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;
import org.jdesktop.core.animation.timing.Interpolator;
import com.craftingdead.immerse.client.gui.view.Color;
import com.craftingdead.immerse.client.gui.view.Display;
import com.craftingdead.immerse.client.gui.view.Overflow;
import com.craftingdead.immerse.client.gui.view.Point;
import com.craftingdead.immerse.client.gui.view.layout.Align;
import com.craftingdead.immerse.client.gui.view.layout.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.Justify;
import com.craftingdead.immerse.client.gui.view.layout.PositionType;
import com.craftingdead.immerse.client.gui.view.layout.Wrap;
import com.craftingdead.immerse.client.gui.view.style.Percentage;
import com.craftingdead.immerse.client.util.FitType;
import com.craftingdead.immerse.util.StringCountUtil;
import io.github.humbleui.skija.paragraph.Alignment;
import io.github.humbleui.skija.paragraph.Shadow;
import net.minecraft.resources.ResourceLocation;

public class StyleParserRegistry {

  private static StyleParserRegistry instance;

  public static StyleParserRegistry getInstance() {
    if (instance == null) {
      instance = new StyleParserRegistry();
    }
    return instance;
  }

  private static final StyleParser<String> STRING_PARSER =
      StyleParser.create(String::length, Function.identity());
  private static final StyleParser<Boolean> BOOLEAN_PARSER =
      StyleParser.create(StringCountUtil::boolAtStart, Boolean::valueOf);
  private static final StyleParser<Integer> INTEGER_PARSER =
      StyleParser.create(StringCountUtil::integerAtStart, Integer::parseInt);
  private static final StyleParser<Long> LONG_PARSER =
      StyleParser.create(StringCountUtil::integerAtStart, Long::valueOf);
  private static final StyleParser<Float> FLOAT_PARSER =
      StyleParser.create(StringCountUtil::floatAtStart, Float::valueOf);
  private static final StyleParser<Double> DOUBLE_PARSER =
      StyleParser.create(StringCountUtil::floatAtStart, Double::valueOf);

  private final Map<Class<?>, StyleParser<?>> styleParsers = new IdentityHashMap<>();

  private StyleParserRegistry() {
    this.registerBuiltins();
  }

  public <T> void registerParser(Class<T> valueClass, StyleParser<T> parser) {
    this.styleParsers.put(valueClass, parser);
  }

  @SuppressWarnings("unchecked")
  public <T> StyleParser<T> getParser(Class<T> valueClass) {
    var parser = (StyleParser<T>) this.styleParsers.get(valueClass);
    if (parser == null) {
      throw new IllegalStateException("No parser registered for: " + valueClass.getSimpleName());
    }
    return parser;
  }

  private void registerBuiltins() {
    this.registerParser(String[].class, new ArrayParser<>(String.class, STRING_PARSER));
    this.registerParser(Shadow[].class, new ArrayParser<>(Shadow.class, ShadowParser.INSTANCE));

    this.registerParser(String.class, STRING_PARSER);
    this.registerParser(Boolean.class, BOOLEAN_PARSER);
    this.registerParser(Integer.class, INTEGER_PARSER);
    this.registerParser(Long.class, LONG_PARSER);
    this.registerParser(Float.class, FLOAT_PARSER);
    this.registerParser(Double.class, DOUBLE_PARSER);

    this.registerParser(Percentage.class, PercentageParser.INSTANCE);
    this.registerParser(Point.class, PointParser.INSTANCE);
    this.registerParser(Color.class, ColorParser.INSTANCE);
    this.registerParser(ResourceLocation.class, ResourceLocationParser.INSTANCE);
    this.registerParser(Interpolator.class, InterpolatorParser.INSTANCE);

    this.registerParser(Align.class, new EnumParser<>(Align.class));
    this.registerParser(FlexDirection.class, new EnumParser<>(FlexDirection.class));
    this.registerParser(Justify.class, new EnumParser<>(Justify.class));
    this.registerParser(PositionType.class, new EnumParser<>(PositionType.class));
    this.registerParser(Display.class, new EnumParser<>(Display.class));
    this.registerParser(Overflow.class, new EnumParser<>(Overflow.class));
    this.registerParser(FitType.class, new EnumParser<>(FitType.class));
    this.registerParser(Wrap.class, new EnumParser<>(Wrap.class));
    this.registerParser(Alignment.class, new EnumParser<>(Alignment.class));
  }
}
