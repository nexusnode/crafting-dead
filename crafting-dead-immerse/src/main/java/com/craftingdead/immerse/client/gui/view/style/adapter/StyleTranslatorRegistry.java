package com.craftingdead.immerse.client.gui.view.style.adapter;

import java.util.IdentityHashMap;
import java.util.Map;
import com.craftingdead.immerse.client.gui.view.Color;
import com.craftingdead.immerse.client.gui.view.Display;
import com.craftingdead.immerse.client.gui.view.Overflow;
import com.craftingdead.immerse.client.gui.view.Point;
import com.craftingdead.immerse.client.gui.view.PointTranslator;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaAlign;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaFlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaJustify;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaPositionType;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaWrap;
import com.craftingdead.immerse.client.util.FitType;
import com.craftingdead.immerse.util.StringCountUtil;
import net.minecraft.resources.ResourceLocation;

public class StyleTranslatorRegistry {

  private static StyleTranslatorRegistry instance;

  public static StyleTranslatorRegistry getInstance() {
    if (instance == null) {
      instance = new StyleTranslatorRegistry();
    }
    return instance;
  }

  private final Map<Class<?>, StyleDecoder<?>> styleDecoders = new IdentityHashMap<>();
  private final Map<Class<?>, StyleEncoder<?>> styleEncoders = new IdentityHashMap<>();
  private final Map<Class<?>, StyleValidator<?>> styleValidators = new IdentityHashMap<>();

  private StyleTranslatorRegistry() {
    this.registerBuiltins();
  }

  public <T> void registerTranslator(Class<T> valueClass, StyleDecoder<T> decoder,
      StyleEncoder<T> encoder, StyleValidator<T> validator) {
    this.styleDecoders.put(valueClass, decoder);
    this.styleEncoders.put(valueClass, encoder);
    this.styleValidators.put(valueClass, validator);
  }

  public <T, V extends StyleDecoder<T> & StyleEncoder<T> & StyleValidator<T>> void registerTranslator(
      Class<T> valueClass, V translator) {
    this.registerTranslator(valueClass, translator, translator, translator);
  }

  @SuppressWarnings("unchecked")
  public <T> StyleDecoder<T> getDecoder(Class<T> valueClass) {
    return (StyleDecoder<T>) this.styleDecoders.get(valueClass);
  }

  @SuppressWarnings("unchecked")
  public <T> StyleEncoder<T> getEncoder(Class<T> valueClass) {
    return (StyleEncoder<T>) this.styleEncoders.get(valueClass);
  }

  @SuppressWarnings("unchecked")
  public <T> StyleValidator<T> getValidator(Class<T> valueClass) {
    return (StyleValidator<T>) this.styleValidators.get(valueClass);
  }

  @SuppressWarnings("unchecked")
  public <T> T decode(String cssString, Class<T> valueClass) {
    var decoder = (StyleDecoder<T>) this.styleDecoders.get(valueClass);
    if (decoder == null) {
      throw new IllegalStateException("No decoder registered for " + valueClass.getName());
    }
    return decoder.decode(cssString);
  }

  @SuppressWarnings("unchecked")
  public <T> String encode(Object value, Class<T> valueClass, boolean prettyPrint) {
    return ((StyleEncoder<T>) this.styleEncoders.get(valueClass)).encode((T) value, prettyPrint);
  }

  public int validate(String cssString, Class<?> valueClass) {
    return this.styleValidators.get(valueClass).validate(cssString);
  }

  private void registerBuiltins() {
    this.registerTranslator(Integer.class, Integer::parseInt,
        (cssString, pretty) -> String.valueOf(cssString),
        StringCountUtil::integerAtStart);

    this.registerTranslator(Long.class, Long::parseLong,
        (cssString, pretty) -> String.valueOf(cssString),
        StringCountUtil::integerAtStart);

    this.registerTranslator(Float.class, new FloatTranslator());
    this.registerTranslator(Double.class, new DoubleTranslator());

    this.registerTranslator(String.class, String::valueOf,
        (cssString, pretty) -> cssString,
        cssString -> cssString.indexOf(" "));

    this.registerTranslator(Boolean.class, Boolean::parseBoolean,
        (cssString, pretty) -> String.valueOf(cssString),
        StringCountUtil::boolAtStart);

    this.registerTranslator(Point.class, new PointTranslator());
    this.registerTranslator(Color.class, new ColorTranslator());
    this.registerTranslator(YogaAlign.class, new EnumTranslator<>(YogaAlign.class));
    this.registerTranslator(YogaFlexDirection.class, new EnumTranslator<>(YogaFlexDirection.class));
    this.registerTranslator(YogaJustify.class, new EnumTranslator<>(YogaJustify.class));
    this.registerTranslator(YogaPositionType.class, new EnumTranslator<>(YogaPositionType.class));
    this.registerTranslator(Display.class, new EnumTranslator<>(Display.class));
    this.registerTranslator(Overflow.class, new EnumTranslator<>(Overflow.class));
    this.registerTranslator(FitType.class, new EnumTranslator<>(FitType.class));
    this.registerTranslator(ResourceLocation.class, new ResourceLocationTranslator());
    this.registerTranslator(YogaWrap.class, new EnumTranslator<>(YogaWrap.class));
  }
}
