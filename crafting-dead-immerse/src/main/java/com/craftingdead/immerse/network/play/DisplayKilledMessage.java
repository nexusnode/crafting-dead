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

import java.util.function.Supplier;
import com.craftingdead.core.network.NetworkUtil;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.KilledMessage;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class DisplayKilledMessage {

  private final int killerEntityId;
  private final ItemStack itemStack;

  public DisplayKilledMessage(int killerEntityId, ItemStack itemStack) {
    this.killerEntityId = killerEntityId;
    this.itemStack = itemStack;
  }

  public void encode(PacketBuffer out) {
    out.writeVarInt(this.killerEntityId);
    out.writeItem(this.itemStack);
  }

  public static DisplayKilledMessage decode(PacketBuffer in) {
    return new DisplayKilledMessage(in.readVarInt(), in.readItem());
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(
        () -> CraftingDeadImmerse.getInstance().getClientDist().getIngameGui().displayKilledMessage(
            new KilledMessage(
                NetworkUtil.getEntity(ctx.get(), this.killerEntityId,
                    AbstractClientPlayerEntity.class),
                this.itemStack)));
    return true;
  }
}
