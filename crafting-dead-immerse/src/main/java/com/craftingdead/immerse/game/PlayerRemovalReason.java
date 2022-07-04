package com.craftingdead.immerse.game;

public enum PlayerRemovalReason {

  LOGGED_OUT, GAME_UNLOADED;

  public boolean isLoggedOut() {
    return this == LOGGED_OUT;
  }

  public boolean isGameUnloaded() {
    return this == GAME_UNLOADED;
  }
}
