/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
 */
package com.craftingdead.immerse.client.gui.component;

import net.minecraftforge.eventbus.api.Event;

public class KeyEvent extends Event {

  private final int key;
  private final int scancode;
  private final int action;
  private final int mods;

  public KeyEvent(int key, int scancode, int action, int mods) {
    this.key = key;
    this.scancode = scancode;
    this.action = action;
    this.mods = mods;
  }

  public int getKey() {
    return this.key;
  }

  public int getScancode() {
    return this.scancode;
  }

  public int getAction() {
    return this.action;
  }

  public int getMods() {
    return this.mods;
  }
}

