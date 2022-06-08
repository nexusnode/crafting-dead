package sm0keysa1m0n.bliss.style;

public record StyleProperty(String name, String value) {

  @Override
  public boolean equals(Object obj) {
    return obj == this || obj instanceof StyleProperty that && this.name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return this.name.hashCode();
  }
}
