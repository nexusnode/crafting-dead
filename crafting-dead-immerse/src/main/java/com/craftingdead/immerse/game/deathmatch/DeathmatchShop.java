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

package com.craftingdead.immerse.game.deathmatch;

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.living.IPlayer;
import com.craftingdead.core.inventory.CombatSlotType;
import com.craftingdead.immerse.game.deathmatch.message.BuyItemMessage;
import com.craftingdead.immerse.game.network.GameNetworkChannel;
import com.craftingdead.immerse.game.shop.IShop;
import net.minecraft.item.ItemStack;

public class DeathmatchShop implements IShop {

  private final boolean client;

  public DeathmatchShop(boolean client) {
    this.client = client;
  }

  @Override
  public int getPlayerMoney(IPlayer<?> player) {
    return -1;
  }

  @Override
  public void buyItem(IPlayer<?> player, ItemStack itemStack) {
    if (this.client) {
      GameNetworkChannel.sendToServer(new BuyItemMessage(itemStack));
    } else if (this.getBuyTimeSeconds(player) > 0) {
      CombatSlotType.getSlotType(itemStack)
          .orElseThrow(() -> new IllegalStateException("Invalid item"))
          .addToInventory(itemStack, player.getEntity().inventory, true);
      itemStack.getCapability(ModCapabilities.GUN).ifPresent(gun -> gun.getAcceptedMagazines());
    }
  }

  @Override
  public int getBuyTimeSeconds(IPlayer<?> player) {
    return ((DeathmatchPlayerExtension) player
        .getExpectedExtension(DeathmatchPlayerExtension.EXTENSION_ID)).getRemainingBuyTimeSeconds();
  }
}
