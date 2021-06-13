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
import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PerformActionMessage {

  private final ActionType actionType;
  /**
   * Ignored on server reception - assumed to be the player who sent the message.
   */
  private final int performerEntityId;
  private final int targetEntityId;

  public PerformActionMessage(ActionType actionType, int performerEntityId,
      int targetEntityId) {
    this.actionType = actionType;
    this.performerEntityId = performerEntityId;
    this.targetEntityId = targetEntityId;
  }

  public void encode(PacketBuffer out) {
    out.writeRegistryId(this.actionType);
    out.writeVarInt(this.performerEntityId);
    out.writeVarInt(this.targetEntityId);
  }

  public static PerformActionMessage decode(PacketBuffer in) {
    return new PerformActionMessage(in.readRegistryId(), in.readVarInt(), in.readVarInt());
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil.getEntity(ctx.get(), this.performerEntityId).ifPresent(performerEntity -> {
      LivingExtension<?, ?> performer =
          performerEntity.getCapability(Capabilities.LIVING).orElse(null);
      LivingExtension<?, ?> target = this.targetEntityId == -1 ? null
          : performerEntity.level.getEntity(this.targetEntityId)
              .getCapability(Capabilities.LIVING).orElse(null);
      final boolean isServer = ctx.get().getDirection().getReceptionSide().isServer();
      if (!isServer || this.actionType.isTriggeredByClient()) {
        performer.performAction(this.actionType.createAction(performer, target), isServer);
      }
    });
    return true;
  }
}
