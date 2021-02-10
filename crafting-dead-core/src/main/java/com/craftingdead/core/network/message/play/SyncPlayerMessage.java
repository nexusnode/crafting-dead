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

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.living.Player;
import com.craftingdead.core.network.util.GenericDataManager;
import com.craftingdead.core.network.util.GenericDataManager.DataEntry;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncPlayerMessage {

  private int entityId;
  private List<GenericDataManager.DataEntry<?>> dataManagerEntries;

  public SyncPlayerMessage(int entityId, List<DataEntry<?>> dataManagerEntries) {
    this.entityId = entityId;
    this.dataManagerEntries = dataManagerEntries;
  }

  public static void encode(SyncPlayerMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
    try {
      GenericDataManager.writeEntries(msg.dataManagerEntries, out);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static SyncPlayerMessage decode(PacketBuffer in) {
    try {
      return new SyncPlayerMessage(in.readVarInt(), GenericDataManager.readEntries(in));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean handle(SyncPlayerMessage msg, Supplier<NetworkEvent.Context> ctx) {
    Optional<World> world =
        LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
    world.flatMap(w -> Optional.ofNullable(w.getEntityByID(msg.entityId)))
        .flatMap(entity -> entity.getCapability(ModCapabilities.LIVING).resolve())
        .filter(living -> living instanceof Player)
        .ifPresent(p -> ((Player<?>) p).getDataManager().setEntryValues(msg.dataManagerEntries));
    return true;
  }
}
