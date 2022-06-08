package sm0keysa1m0n.bliss.style.parser.value;

import sm0keysa1m0n.bliss.Filter;

public class FilterParser implements ValueParser<Filter> {

  public static final FilterParser INSTANCE = new FilterParser();

  private FilterParser() {}

  @Override
  public int validate(String style) {
    return style.indexOf(')') + 1;
  }

  @Override
  public Filter parse(String style) {
    var leftParenthesisIndex = style.indexOf('(');
    var name = style.substring(0, leftParenthesisIndex);
    var arguments = style.substring(leftParenthesisIndex + 1, style.indexOf(')'));

    var filter = switch (name) {
      case "blur" -> Filter.blur(Float.valueOf(arguments.strip().replace("px", "")));
      default -> throw new IllegalArgumentException("Unknown filter: " + name);
    };

    return filter;
  }
}
