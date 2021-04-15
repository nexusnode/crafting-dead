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

package com.craftingdead.immerse.client.gui.screen.game;

import com.craftingdead.core.living.IPlayer;
import com.craftingdead.immerse.game.shop.IShop;
import com.craftingdead.immerse.game.shop.IShopCategory;
import net.minecraft.client.gui.screen.Screen;

public class ShopScreen extends AbstractShopScreen {

  public ShopScreen(Screen lastScreen, IShop shop, IPlayer<?> player) {
    super(lastScreen, shop, player);
    for (IShopCategory category : this.getShop().getCategories(player)) {
      this.addShopButton(new CategoryButton(this, player, category));
    }
  }
}
