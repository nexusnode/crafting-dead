package sm0keysa1m0n.bliss;

public enum Overflow {

  VISIBLE(false), HIDDEN(true), SCROLL(true);

  private final boolean clipped;

  private Overflow(boolean clipped) {
    this.clipped = clipped;
  }

  public boolean isClipped() {
    return this.clipped;
  }
}
