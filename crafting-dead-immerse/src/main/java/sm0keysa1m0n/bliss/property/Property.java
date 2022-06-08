package sm0keysa1m0n.bliss.property;

public interface Property<T> {

  /**
   * Get this property's name.
   * 
   * @return it's name
   */
  String getName();

  /**
   * Get this property's type.
   * 
   * @return it's type
   */
  Class<T> getType();

  /**
   * Retrieve this property's value.
   * 
   * @return the value
   */
  T get();

  /**
   * Set this property's value.
   * 
   * @param value to set
   */
  void set(T value);
}
