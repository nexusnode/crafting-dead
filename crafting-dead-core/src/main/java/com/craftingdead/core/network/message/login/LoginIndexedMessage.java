/*******************************************************************************
 * Copyright (C) 2020 Nexus Node
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
