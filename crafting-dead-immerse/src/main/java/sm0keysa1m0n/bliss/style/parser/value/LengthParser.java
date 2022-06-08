package sm0keysa1m0n.bliss.style.parser.value;

import sm0keysa1m0n.bliss.Length;
import sm0keysa1m0n.bliss.calc.CalcParser;
import sm0keysa1m0n.bliss.style.parser.StringCountUtil;

public class LengthParser implements ValueParser<Length> {

  public static final LengthParser INSTANCE = new LengthParser();

  private LengthParser() {}

  @Override
  public int validate(String style) {
    int floatLength = StringCountUtil.floatAtStart(style);

    if (floatLength == 0) {
      return 0;
    }

    if (floatLength < style.length() && style.charAt(floatLength) == '%') {
      return floatLength + 1;
    }
    return floatLength;
  }

  @Override
  public Length parse(String style) {
    if (style.startsWith("calc(")) {
      return Length.calculated(
          CalcParser.parse(style.substring(style.indexOf('(') + 1, style.lastIndexOf(')'))));
    }

    if (style.contains("%")) {
      return Length.percentage(Float.parseFloat(style.replace('%', '\0')));
    } else if (style.equals("auto")) {
      return Length.AUTO;
    } else {
      return Length.fixed(Float.parseFloat(style.replace("px", "")));
    }
  }
}
