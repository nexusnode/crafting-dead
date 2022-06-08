package sm0keysa1m0n.bliss.style.parser.value;

import java.util.function.Function;

public interface ValueParser<T> {

  /**
   * Validate a css string and return the consumed length. return 0 for an invalid css String.
   *
   * @param style css string
   * @return the consumed chars count
   */
  int validate(String style);

  /**
   * Parse a css string and return the value.
   *
   * @param style css string
   * @return the decoded value
   */
  T parse(String style);

  static <T> ValueParser<T> create(Function<String, Integer> validator,
      Function<String, T> parser) {
    return new ValueParser<>() {

      @Override
      public int validate(String style) {
        return validator.apply(style);
      }

      @Override
      public T parse(String style) {
        return parser.apply(style);
      }
    };
  }
}
