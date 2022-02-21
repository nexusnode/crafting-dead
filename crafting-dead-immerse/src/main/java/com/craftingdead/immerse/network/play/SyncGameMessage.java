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

package com.craftingdead.immerse.network.play;

import java.util.function.Supplier;
import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record SyncGameMessage(FriendlyByteBuf buf) {

  public void encode(FriendlyByteBuf out) {
    out.writeBytes(this.buf);
    this.buf.release();
  }

  public static SyncGameMessage decode(FriendlyByteBuf in) {
    return new SyncGameMessage(in);
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get()
        .enqueueWork(() -> {
          var gameWrapper = CraftingDeadImmerse.getInstance().getClientDist().getGameWrapper();
          if (gameWrapper != null) {
            gameWrapper.decode(this.buf);
          }
        })
        .thenRun(this.buf::release);
    return true;
  }
}
