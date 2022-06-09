package sm0keysa1m0n.bliss.style.parser;

import org.jetbrains.annotations.Nullable;

public class StyleReader {

  private static final char SYNTAX_ESCAPE = '\\';
  private static final char SYNTAX_DOUBLE_QUOTE = '"';
  private static final char SYNTAX_SINGLE_QUOTE = '\'';

  private final String string;
  private int cursor;

  public StyleReader(StyleReader other) {
    this.string = other.string;
    this.cursor = other.cursor;
  }

  public StyleReader(String string) {
    this.string = string;
  }

  public String getString() {
    return this.string;
  }

  public void setCursor(int cursor) {
    this.cursor = cursor;
  }

  public int getRemainingLength() {
    return this.string.length() - this.cursor;
  }

  public int getTotalLength() {
    return this.string.length();
  }

  public int getCursor() {
    return this.cursor;
  }

  public String getRead() {
    return this.string.substring(0, this.cursor);
  }

  public String getRemaining() {
    return this.string.substring(this.cursor);
  }

  public boolean canRead(int length) {
    return this.cursor + length <= this.string.length();
  }

  public boolean canRead() {
    return this.canRead(1);
  }

  public char peek() {
    return this.string.charAt(this.cursor);
  }

  public char peek(int offset) {
    return this.string.charAt(this.cursor + offset);
  }

  public char read() {
    return this.string.charAt(this.cursor++);
  }

  public void skip() {
    this.cursor++;
  }

  public void skipWhitespace() {
    while (this.canRead() && Character.isWhitespace(this.peek())) {
      this.skip();
    }
  }

  @Nullable
  public Integer readInteger() throws ParserException {
    var start = this.cursor;
    while (this.canRead() && isAllowedNumber(this.peek())) {
      this.skip();
    }
    if (start == this.cursor) {
      return null;
    }
    var number = this.string.substring(start, this.cursor);
    try {
      return Integer.valueOf(number);
    } catch (NumberFormatException e) {
      this.cursor = start;
      throw new ParserException("Invalid integer \"" + number + "\" at index " + this.cursor);
    }
  }

  @Nullable
  public Long readLong() throws ParserException {
    var start = this.cursor;
    while (this.canRead() && isAllowedNumber(this.peek())) {
      this.skip();
    }
    if (start == this.cursor) {
      return null;
    }
    var number = this.string.substring(start, this.cursor);
    try {
      return Long.valueOf(number);
    } catch (NumberFormatException e) {
      this.cursor = start;
      throw new ParserException("Invalid long \"" + number + "\" at index " + this.cursor);
    }
  }

  @Nullable
  public Double readDouble() throws ParserException {
    var start = this.cursor;
    while (this.canRead() && isAllowedNumber(this.peek())) {
      this.skip();
    }
    if (start == this.cursor) {
      return null;
    }
    var number = this.string.substring(start, this.cursor);
    try {
      return Double.valueOf(number);
    } catch (NumberFormatException e) {
      this.cursor = start;
      throw new ParserException("Invalid double \"" + number + "\" at index " + this.cursor);
    }
  }

  @Nullable
  public Float readFloat() throws ParserException {
    var start = this.cursor;
    while (this.canRead() && isAllowedNumber(this.peek())) {
      this.skip();
    }
    if (start == this.cursor) {
      return null;
    }
    var number = this.string.substring(start, this.cursor);
    try {
      return Float.valueOf(number);
    } catch (NumberFormatException e) {
      this.cursor = start;
      throw new ParserException("Invalid float \"" + number + "\" at index " + this.cursor);
    }
  }

  @Nullable
  public String readUnquotedString() {
    var start = this.cursor;
    while (this.canRead() && isAllowedInUnquotedString(this.peek())) {
      this.skip();
    }
    return start == this.cursor ? null : this.string.substring(start, this.cursor);
  }

  @Nullable
  public ParsedFunction readFunction() throws ParserException {
    var start = this.cursor;
    while (this.canRead() && isAllowedInUnquotedString(this.peek())) {
      this.skip();
    }

    if (start == this.cursor) {
      return null;
    }

    if (!this.canRead() || this.peek() != '(') {
      this.setCursor(start);
      return null;
    }

    var name = this.string.substring(start, this.cursor);

    this.skip();

    var argumentsStart = this.cursor;

    var bracketLevel = 1;
    while (this.canRead() && bracketLevel > 0) {
      var ch = this.peek();
      if (ch == '(') {
        bracketLevel++;
      } else if (ch == ')') {
        bracketLevel--;
      }
      this.skip();
    }

    if (bracketLevel != 0) {
      this.setCursor(start);
      throw new ParserException("Invalid function starting at index " + start);
    }

    var arguments = this.string.substring(argumentsStart, this.cursor - 1);
    return new ParsedFunction(name, arguments);
  }

  @Nullable
  public String readQuotedString() throws ParserException {
    if (!this.canRead()) {
      return null;
    }
    var next = this.peek();
    if (!isQuotedStringStart(next)) {
      throw new ParserException("Expected start of quote at index " + this.cursor);
    }
    this.skip();
    return this.readStringUntil(next);
  }

  public String readStringUntil(char terminator) throws ParserException {
    var result = new StringBuilder();
    var escaped = false;
    while (this.canRead()) {
      var ch = this.read();
      if (escaped) {
        if (ch == terminator || ch == SYNTAX_ESCAPE) {
          result.append(ch);
          escaped = false;
        } else {
          this.setCursor(this.getCursor() - 1);
          throw new ParserException(
              "Unexpected escape character \"" + ch + "\" at index " + this.cursor);
        }
      } else if (ch == SYNTAX_ESCAPE) {
        escaped = true;
      } else if (ch == terminator) {
        return result.toString();
      } else {
        result.append(ch);
      }
    }

    throw new ParserException("Expected string termination at " + this.cursor);
  }

  @Nullable
  public String readString() throws ParserException {
    if (!this.canRead()) {
      return null;
    }
    var next = this.peek();
    if (isQuotedStringStart(next)) {
      this.skip();
      return this.readStringUntil(next);
    }
    return this.readUnquotedString();
  }

  @Nullable
  public Boolean readBoolean() throws ParserException {
    int start = this.cursor;
    var value = this.readString();
    if (value == null) {
      throw null;
    }

    if (value.equals("true")) {
      return true;
    } else if (value.equals("false")) {
      return false;
    } else {
      this.cursor = start;
      throw new ParserException("Invalid boolean at index " + start);
    }
  }

  public void expect(char ch) throws ParserException {
    if (!this.canRead() || this.peek() != ch) {
      throw new ParserException(
          "Expected \"" + ch + "\" at index " + this.cursor + " but got \"" + this.peek() + "\"");
    }
    this.skip();
  }

  public static boolean isAllowedInUnquotedString(char ch) {
    return ch >= '0' && ch <= '9'
        || ch >= 'A' && ch <= 'Z'
        || ch >= 'a' && ch <= 'z'
        || ch == '_' || ch == '-'
        || ch == '.' || ch == '+'
        || ch == '%';
  }

  public static boolean isAllowedNumber(char ch) {
    return ch >= '0' && ch <= '9' || ch == '.' || ch == '-';
  }

  public static boolean isQuotedStringStart(char ch) {
    return ch == SYNTAX_DOUBLE_QUOTE || ch == SYNTAX_SINGLE_QUOTE;
  }
}
