package com.craftingdead.mod.item;

import java.util.function.Supplier;

public interface IFireMode {

  boolean canShoot(boolean triggerPressed);

  public static enum Modes implements Supplier<IFireMode> {

    AUTO {
      @Override
      public IFireMode get() {
        return new IFireMode() {
          @Override
          public boolean canShoot(boolean triggerPressed) {
            return triggerPressed;
          }
        };
      }
    },
    SEMI {
      @Override
      public IFireMode get() {
        return new IFireMode() {

          private boolean lastTriggerPressed;

          @Override
          public boolean canShoot(boolean triggerPressed) {
            boolean result = triggerPressed && !this.lastTriggerPressed;
            this.lastTriggerPressed = triggerPressed;
            return result;
          }
        };
      }
    };
  }
}
