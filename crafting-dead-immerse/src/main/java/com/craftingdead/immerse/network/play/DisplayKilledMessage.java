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

package com.craftingdead.immerse.network.play;

import java.util.Optional;
import java.util.function.Supplier;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.KilledMessage;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class DisplayKilledMessage {

  private final int killerEntityId;
  private final ItemStack itemStack;

  public DisplayKilledMessage(int killerEntityId, ItemStack itemStack) {
    this.killerEntityId = killerEntityId;
    this.itemStack = itemStack;
  }

  public static void encode(DisplayKilledMessage message, PacketBuffer out) {
    out.writeVarInt(message.killerEntityId);
    out.writeItem(message.itemStack);
  }

  public static DisplayKilledMessage decode(PacketBuffer in) {
    return new DisplayKilledMessage(in.readVarInt(), in.readItem());
  }

  public static boolean handle(DisplayKilledMessage message, Supplier<NetworkEvent.Context> ctx) {
    LogicalSidedProvider.CLIENTWORLD
        .<Optional<World>>get(ctx.get().getDirection().getReceptionSide())
        .map(w -> w.getEntity(message.killerEntityId))
        .filter(e -> e instanceof AbstractClientPlayerEntity)
        .ifPresent(e -> CraftingDeadImmerse.getInstance().getClientDist().getIngameGui()
            .displayKilledMessage(
                new KilledMessage((AbstractClientPlayerEntity) e, message.itemStack)));
    return true;
  }
}
