/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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

import com.craftingdead.core.world.action.Action.StopReason;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

public record ActionCompletionSync() {

  public void encode(FriendlyByteBuf out) {
  }

  public static ActionCompletionSync decode(FriendlyByteBuf in) {
    return new ActionCompletionSync();
  }

  public boolean handle(Supplier<Context> ctx) {
    if (ctx.get().getDirection().getReceptionSide().isClient()) {
      ctx.get().enqueueWork(ActionCompletionSync::doClientSync);
    }
    return true;
  }

  private static void doClientSync() {
    var player = Objects.requireNonNull(PlayerExtension.get(Minecraft.getInstance().player));
    // We can't directly mark an action as completed, so we need to tell it directly that it was completed.
    player.getAction().ifPresent(action -> {
      action.stop(StopReason.COMPLETED);
      // And now we cancel it on the client to be sure it's not around anymore
      player.cancelAction(false);
    });
  }
}
