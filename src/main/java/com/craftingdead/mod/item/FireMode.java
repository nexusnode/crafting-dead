package com.craftingdead.mod.item;

public enum FireMode {

  AUTO("message.fire_mode.auto"), SEMI("message.fire_mode.semi");

  private final String translationKey;

  private FireMode(String translationKey) {
    this.translationKey = translationKey;
  }

  public String getTranslationKey() {
    return this.translationKey;
  }
}
