/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.immerse.game.module.shop;

import java.util.List;
import net.minecraft.network.chat.Component;

public class ShopCategory {

  private final Component displayName;
  private final Component info;
  private final List<ShopItem> items;

  public ShopCategory(Component displayName, Component info, List<ShopItem> items) {
    this.displayName = displayName;
    this.info = info;
    this.items = items;
  }

  public Component getDisplayName() {
    return this.displayName;
  }

  public Component getInfo() {
    return this.info;
  }

  public List<ShopItem> getItems() {
    return this.items;
  }
}
