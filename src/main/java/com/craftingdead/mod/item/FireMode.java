package com.craftingdead.mod.item;

public enum FireMode {

  AUTO("fire_mode.auto"), SEMI("fire_mode.semi");

  private final String translationKey;

  private FireMode(String translationKey) {
    this.translationKey = translationKey;
  }

  public String getTranslationKey() {
    return this.translationKey;
  }
}
