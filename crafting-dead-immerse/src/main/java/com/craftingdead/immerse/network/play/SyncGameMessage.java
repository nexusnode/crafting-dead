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
import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record SyncGameMessage(FriendlyByteBuf buf) {

  public void encode(FriendlyByteBuf out) {
    out.writeBytes(this.buf);
    this.buf.release();
  }

  public static SyncGameMessage decode(FriendlyByteBuf in) {
    return new SyncGameMessage(in);
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get()
        .enqueueWork(() -> {
          var gameWrapper = CraftingDeadImmerse.getInstance().getClientDist().getGameWrapper();
          if (gameWrapper != null) {
            gameWrapper.decode(this.buf);
          }
        })
        .thenRun(this.buf::release);
    return true;
  }
}
