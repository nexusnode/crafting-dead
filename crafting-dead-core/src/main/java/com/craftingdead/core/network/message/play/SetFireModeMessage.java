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
import com.craftingdead.core.world.item.gun.FireMode;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SetFireModeMessage {

  private final int entityId;
  private final FireMode fireMode;

  public SetFireModeMessage(int entityId, FireMode fireMode) {
    this.entityId = entityId;
    this.fireMode = fireMode;
  }

  public void encode(PacketBuffer out) {
    out.writeVarInt(this.entityId);
    out.writeEnum(this.fireMode);
  }

  public static SetFireModeMessage decode(PacketBuffer in) {
    return new SetFireModeMessage(in.readVarInt(), in.readEnum(FireMode.class));
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> NetworkUtil.getEntityOrSender(ctx.get(), this.entityId)
        .getCapability(Capabilities.LIVING_EXTENSION)
        .ifPresent(extension -> extension.getMainHandGun()
            .ifPresent(gun -> gun.setFireMode(extension, this.fireMode,
                ctx.get().getDirection().getReceptionSide().isServer()))));
    return true;
  }
}
