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

import java.util.Optional;
import java.util.function.Supplier;
import com.craftingdead.core.capability.Capabilities;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncLivingMessage {

  private final int entityId;
  private final PacketBuffer data;

  public SyncLivingMessage(int entityId, PacketBuffer data) {
    this.entityId = entityId;
    this.data = data;
  }

  public static void encode(SyncLivingMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
    out.writeVarInt(msg.data.readableBytes());
    out.writeBytes(msg.data);
  }

  public static SyncLivingMessage decode(PacketBuffer in) {
    int entityId = in.readVarInt();
    byte[] data = new byte[in.readVarInt()];
    in.readBytes(data);
    SyncLivingMessage msg = new SyncLivingMessage(entityId,
        new PacketBuffer(Unpooled.wrappedBuffer(data)));
    return msg;
  }

  public static boolean handle(SyncLivingMessage msg, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      Optional<World> world =
          LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
      Entity entity = world.map(w -> w.getEntity(msg.entityId)).orElse(null);
      if (entity == null) {
        return;
      }
      entity.getCapability(Capabilities.LIVING).ifPresent(living -> living.decode(msg.data));
    });
    return true;
  }
}
