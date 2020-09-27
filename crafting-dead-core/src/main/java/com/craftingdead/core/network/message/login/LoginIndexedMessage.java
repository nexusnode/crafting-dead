package com.craftingdead.core.network.message.login;

import java.util.function.IntSupplier;

public class LoginIndexedMessage implements IntSupplier {

  private int loginIndex;

  public void setLoginIndex(final int loginIndex) {
    this.loginIndex = loginIndex;
  }

  public int getLoginIndex() {
    return loginIndex;
  }

  @Override
  public int getAsInt() {
    return getLoginIndex();
  }
}
