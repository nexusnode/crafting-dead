package sm0keysa1m0n.bliss.property;

import org.jetbrains.annotations.Nullable;

public interface AnimatableProperty<T> extends Property<T> {

  /**
   * If this property's value is currently being overridden/animated.
   * 
   * @return <code>true</code> if animated, <code>false</code> otherwise
   */
  boolean isBeingAnimated();

  /**
   * Overrides the property's current value (generally used for animations).
   * 
   * @param overrideValue - the override value
   */
  void setAnimatedValue(@Nullable T overrideValue);

  /**
   * Shorthand for {@code setAnimatedValue(null)}.
   */
  default void clearAnimatedValue() {
    this.setAnimatedValue(null);
  }
}
