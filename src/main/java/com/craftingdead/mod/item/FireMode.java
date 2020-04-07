package com.craftingdead.mod.item;

import java.util.Optional;

public enum FireMode {

  AUTO("message.fire_mode.auto"),
  BURST("message.fire_mode.burst", 3),
  SEMI("message.fire_mode.semi", 1);

  private final String translationKey;
  private final Optional<Integer> maxShotsOptional;

  private FireMode(String translationKey) {
    this(translationKey, null);
  }

  private FireMode(String translationKey, Integer maxShots) {
    this.translationKey = translationKey;
    this.maxShotsOptional = Optional.ofNullable(maxShots);
  }

  public String getTranslationKey() {
    return this.translationKey;
  }

  /**
   * The max shots this {@link FireMode} allows to fire in a row.
   */
  public Optional<Integer> getMaxShots() {
    return this.maxShotsOptional;
  }
}
