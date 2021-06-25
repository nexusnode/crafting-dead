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

public class CrouchMessage {

  private final int entityId;
  private final boolean crouching;

  public CrouchMessage(int entityId, boolean crouching) {
    this.entityId = entityId;
    this.crouching = crouching;
  }

  public void encode(PacketBuffer out) {
    out.writeVarInt(this.entityId);
    out.writeBoolean(this.crouching);
  }

  public static CrouchMessage decode(PacketBuffer in) {
    return new CrouchMessage(in.readVarInt(), in.readBoolean());
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> NetworkUtil.getEntityOrSender(ctx.get(), this.entityId)
        .getCapability(Capabilities.LIVING_EXTENSION)
        .ifPresent(living -> living.setCrouching(this.crouching,
            ctx.get().getDirection().getReceptionSide().isServer())));
    return true;
  }
}
