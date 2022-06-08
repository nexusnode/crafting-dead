package sm0keysa1m0n.bliss.style.parser;

import org.apache.commons.io.LineIterator;

import java.io.Reader;

public class NumberedLineIterator extends LineIterator {

  private int lineNumber;

  public NumberedLineIterator(Reader reader) throws IllegalArgumentException {
    super(reader);
    this.lineNumber = -1;
  }

  @Override
  public String nextLine() {
    this.lineNumber++;
    return super.nextLine();
  }

  public int getLineNumber() {
    return this.lineNumber;
  }
}
