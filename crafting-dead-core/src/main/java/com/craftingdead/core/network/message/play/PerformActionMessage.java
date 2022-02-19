/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
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
