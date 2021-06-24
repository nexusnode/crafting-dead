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
import com.craftingdead.immerse.game.ClientGameWrapper;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncGameMessage {

  private final PacketBuffer data;

  public SyncGameMessage(PacketBuffer data) {
    this.data = data;
  }

  public void encode(PacketBuffer out) {
    out.writeBytes(this.data);
  }

  public static SyncGameMessage decode(PacketBuffer in) {
    return new SyncGameMessage(in);
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    ClientGameWrapper gameWrapper =
        CraftingDeadImmerse.getInstance().getClientDist().getGameWrapper();
    if (gameWrapper != null) {
      gameWrapper.decode(this.data);
    }
    return true;
  }
}
