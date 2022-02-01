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
import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

public record PerformActionMessage(ActionType actionType, int performerEntityId,
    int targetEntityId) {

  public void encode(FriendlyByteBuf out) {
    out.writeRegistryId(this.actionType);
    out.writeVarInt(this.performerEntityId);
    out.writeVarInt(this.targetEntityId);
  }

  public static PerformActionMessage decode(FriendlyByteBuf in) {
    return new PerformActionMessage(in.readRegistryId(), in.readVarInt(), in.readVarInt());
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      final var performerEntity =
          NetworkUtil.getEntityOrSender(ctx.get(), this.performerEntityId, LivingEntity.class);
      final var performer = LivingExtension.getOrThrow(performerEntity);
      final var target = this.targetEntityId == -1 ? null
          : performerEntity.level.getEntity(this.targetEntityId)
              .getCapability(LivingExtension.CAPABILITY)
              .orElse(null);
      final var serverSide = ctx.get().getDirection().getReceptionSide().isServer();
      if (!serverSide || this.actionType.isTriggeredByClient()) {
        performer.performAction(this.actionType.createAction(performer, target), serverSide);
      }
    });
    return true;
  }
}
