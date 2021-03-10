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

package com.craftingdead.immerse.game.deathmatch.message;

import com.craftingdead.core.capability.living.IPlayer;
import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class BuyItemMessage {

  private final ItemStack itemStack;

  public BuyItemMessage(ItemStack itemStack) {
    this.itemStack = itemStack;
  }

  public static void encode(BuyItemMessage message, PacketBuffer out) {
    out.writeItem(message.itemStack);
  }

  public static BuyItemMessage decode(PacketBuffer in) {
    return new BuyItemMessage(in.readItem());
  }

  public static void handle(BuyItemMessage message, NetworkEvent.Context ctx) {
    CraftingDeadImmerse.getInstance().getLogicalServer().getGameServer().getExpectedShop()
        .buyItem(IPlayer.getExpected(ctx.getSender()), message.itemStack);
  }
}
