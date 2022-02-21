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

package com.craftingdead.immerse.game.module.shop.message;

import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;

public class BuyItemMessage {

  private final UUID itemId;

  public BuyItemMessage(UUID itemId) {
    this.itemId = itemId;
  }

  public UUID getItemId() {
    return this.itemId;
  }

  public void encode(FriendlyByteBuf out) {
    out.writeUUID(this.itemId);
  }

  public static BuyItemMessage decode(FriendlyByteBuf in) {
    return new BuyItemMessage(in.readUUID());
  }
}
