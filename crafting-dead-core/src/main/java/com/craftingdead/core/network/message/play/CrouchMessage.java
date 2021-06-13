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

public class CrouchMessage {

  private final int entityId;
  private final boolean crouching;

  public CrouchMessage(int entityId, boolean crouching) {
    this.entityId = entityId;
    this.crouching = crouching;
  }

  public static void encode(CrouchMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
    out.writeBoolean(msg.crouching);
  }

  public static CrouchMessage decode(PacketBuffer in) {
    return new CrouchMessage(in.readVarInt(), in.readBoolean());
  }

  public static boolean handle(CrouchMessage msg, Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil.getEntity(ctx.get(), msg.entityId)
        .filter(entity -> entity instanceof LivingEntity)
        .flatMap(entity -> entity.getCapability(Capabilities.LIVING).resolve())
        .ifPresent(living -> living.setCrouching(msg.crouching,
            ctx.get().getDirection().getReceptionSide().isServer()));
    return true;
  }
}
