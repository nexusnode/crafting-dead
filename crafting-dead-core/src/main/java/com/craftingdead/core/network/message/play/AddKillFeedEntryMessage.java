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

package com.craftingdead.core.network.message.play;

import java.util.function.Supplier;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.damagesource.KillFeedEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class AddKillFeedEntryMessage {

  private final KillFeedEntry entry;

  public AddKillFeedEntryMessage(KillFeedEntry entry) {
    this.entry = entry;
  }

  public void encode(FriendlyByteBuf out) {
    this.entry.encode(out);
  }

  public static AddKillFeedEntryMessage decode(FriendlyByteBuf in) {
    return new AddKillFeedEntryMessage(KillFeedEntry.decode(in));
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> CraftingDead.getInstance().getClientDist().getIngameGui()
        .addKillFeedEntry(this.entry));
    return true;
  }
}
