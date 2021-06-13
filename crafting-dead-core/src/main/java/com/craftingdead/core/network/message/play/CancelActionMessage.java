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
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.network.NetworkUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class CancelActionMessage {

  private final int entityId;

  public CancelActionMessage(int entityId) {
    this.entityId = entityId;
  }

  public static void encode(CancelActionMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
  }

  public static CancelActionMessage decode(PacketBuffer in) {
    return new CancelActionMessage(in.readVarInt());
  }

  public static boolean handle(CancelActionMessage msg, Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil.getEntity(ctx.get(), msg.entityId)
        .filter(entity -> entity instanceof LivingEntity)
        .flatMap(entity -> entity.getCapability(Capabilities.LIVING).resolve())
        .ifPresent(
            living -> living.cancelAction(ctx.get().getDirection().getReceptionSide().isServer()));
    return true;
  }
}
