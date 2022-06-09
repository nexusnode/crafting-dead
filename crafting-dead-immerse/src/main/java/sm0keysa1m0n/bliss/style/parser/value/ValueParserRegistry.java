package sm0keysa1m0n.bliss.style.parser.value;

import java.util.IdentityHashMap;
import java.util.Map;
import org.jdesktop.core.animation.timing.Interpolator;
import io.github.humbleui.skija.paragraph.Alignment;
import io.github.humbleui.skija.paragraph.Shadow;
import net.minecraft.resources.ResourceLocation;
import sm0keysa1m0n.bliss.BoxShadow;
import sm0keysa1m0n.bliss.BoxSizing;
import sm0keysa1m0n.bliss.Color;
import sm0keysa1m0n.bliss.Display;
import sm0keysa1m0n.bliss.Filter;
import sm0keysa1m0n.bliss.ImageRendering;
import sm0keysa1m0n.bliss.Length;
import sm0keysa1m0n.bliss.ObjectFit;
import sm0keysa1m0n.bliss.Overflow;
import sm0keysa1m0n.bliss.PointerEvents;
import sm0keysa1m0n.bliss.Visibility;
import sm0keysa1m0n.bliss.layout.Align;
import sm0keysa1m0n.bliss.layout.FlexDirection;
import sm0keysa1m0n.bliss.layout.Justify;
import sm0keysa1m0n.bliss.layout.PositionType;
import sm0keysa1m0n.bliss.layout.Wrap;
import sm0keysa1m0n.bliss.style.Percentage;
import sm0keysa1m0n.bliss.style.parser.StyleReader;

public class ValueParserRegistry {

  private static ValueParserRegistry instance;

  public static ValueParserRegistry getInstance() {
    if (instance == null) {
      instance = new ValueParserRegistry();
    }
    return instance;
  }

  private final Map<Class<?>, ValueParser<?>> styleParsers = new IdentityHashMap<>();

  private ValueParserRegistry() {
    this.registerBuiltins();
  }

  public <T> void registerParser(Class<T> valueClass, ValueParser<T> parser) {
    this.styleParsers.put(valueClass, parser);
  }

  @SuppressWarnings("unchecked")
  public <T> ValueParser<T> getParser(Class<T> valueClass) {
    var parser = (ValueParser<T>) this.styleParsers.get(valueClass);
    if (parser == null) {
      throw new IllegalStateException("No parser registered for: " + valueClass.getSimpleName());
    }
    return parser;
  }

  private void registerBuiltins() {
    this.registerParser(String[].class,
        new ArrayParser<>(String.class, StyleReader::readString, ','));
    this.registerParser(Shadow[].class,
        new ArrayParser<>(Shadow.class, ShadowParser::parse, ','));
    this.registerParser(Filter[].class,
        new ArrayParser<>(Filter.class, FilterParser::parse, ' '));
    this.registerParser(BoxShadow[].class,
        new ArrayParser<>(BoxShadow.class, BoxShadowParser::parse, ','));

    this.registerParser(String.class, StyleReader::readString);
    this.registerParser(Boolean.class, StyleReader::readBoolean);
    this.registerParser(Integer.class, StyleReader::readInteger);
    this.registerParser(Long.class, StyleReader::readLong);
    this.registerParser(Float.class, StyleReader::readFloat);
    this.registerParser(Double.class, StyleReader::readDouble);

    this.registerParser(Percentage.class, PercentageParser::parse);
    this.registerParser(Length.class, LengthParser::parse);
    this.registerParser(Color.class, ColorParser::parse);
    this.registerParser(ResourceLocation.class, ResourceLocationParser::parse);
    this.registerParser(Interpolator.class, InterpolatorParser::parse);

    this.registerParser(Align.class, new EnumParser<>(Align.class));
    this.registerParser(FlexDirection.class, new EnumParser<>(FlexDirection.class));
    this.registerParser(Justify.class, new EnumParser<>(Justify.class));
    this.registerParser(PositionType.class, new EnumParser<>(PositionType.class));
    this.registerParser(Display.class, new EnumParser<>(Display.class));
    this.registerParser(Overflow.class, new EnumParser<>(Overflow.class));
    this.registerParser(ObjectFit.class, new EnumParser<>(ObjectFit.class));
    this.registerParser(Wrap.class, new EnumParser<>(Wrap.class));
    this.registerParser(Alignment.class, new EnumParser<>(Alignment.class));
    this.registerParser(Visibility.class, new EnumParser<>(Visibility.class));
    this.registerParser(BoxSizing.class, new EnumParser<>(BoxSizing.class));
    this.registerParser(PointerEvents.class, new EnumParser<>(PointerEvents.class));
    this.registerParser(ImageRendering.class, new EnumParser<>(ImageRendering.class));
  }
}
