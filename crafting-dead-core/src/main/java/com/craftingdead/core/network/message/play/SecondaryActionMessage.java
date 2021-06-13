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
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SecondaryActionMessage {

  private final int entityId;
  private final boolean performing;

  public SecondaryActionMessage(int entityId, boolean performing) {
    this.entityId = entityId;
    this.performing = performing;
  }

  public static void encode(SecondaryActionMessage message, PacketBuffer out) {
    out.writeVarInt(message.entityId);
    out.writeBoolean(message.performing);
  }

  public static SecondaryActionMessage decode(PacketBuffer in) {
    return new SecondaryActionMessage(in.readVarInt(), in.readBoolean());
  }

  public static boolean handle(SecondaryActionMessage message, Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil.getEntity(ctx.get(), message.entityId)
        .flatMap(entity -> entity.getCapability(Capabilities.LIVING).resolve())
        .ifPresent(living -> {
          living.getEntity().getMainHandItem()
              .getCapability(Capabilities.GUN)
              .ifPresent(gun -> gun.setPerformingSecondaryAction(living, message.performing,
                  ctx.get().getDirection().getReceptionSide().isServer()));
        });
    return true;
  }
}
