package sm0keysa1m0n.bliss;

public enum Overflow {

  VISIBLE(false), HIDDEN(true), SCROLL(true);

  private final boolean scissor;

  private Overflow(boolean scissor) {
    this.scissor = scissor;
  }

  public boolean shouldScissor() {
    return this.scissor;
  }
}
