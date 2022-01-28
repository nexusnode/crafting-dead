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
import com.craftingdead.core.network.NetworkUtil;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncLivingMessage {

  private final int entityId;
  private final FriendlyByteBuf data;

  public SyncLivingMessage(int entityId, FriendlyByteBuf data) {
    this.entityId = entityId;
    this.data = data;
  }

  public void encode(FriendlyByteBuf out) {
    out.writeVarInt(this.entityId);
    out.writeVarInt(this.data.readableBytes());
    out.writeBytes(this.data);
  }

  public static SyncLivingMessage decode(FriendlyByteBuf in) {
    int entityId = in.readVarInt();
    byte[] data = new byte[in.readVarInt()];
    in.readBytes(data);
    SyncLivingMessage msg = new SyncLivingMessage(entityId,
        new FriendlyByteBuf(Unpooled.wrappedBuffer(data)));
    return msg;
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> NetworkUtil.getEntity(ctx.get(), this.entityId)
        .getCapability(LivingExtension.CAPABILITY)
        .ifPresent(living -> living.decode(this.data)));
    return true;
  }
}
