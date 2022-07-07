/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.game.module.shop;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public record ShopCategory(Component displayName, Component info, List<ShopItem> items) {

  public void encode(FriendlyByteBuf out) {
    out.writeComponent(this.displayName);
    out.writeComponent(this.info);
    out.writeVarInt(this.items.size());
    for (var item : this.items) {
      out.writeUUID(item.id());
    }
  }

  public static ShopCategory decode(FriendlyByteBuf in, Function<UUID, ShopItem> itemLookup) {
    var displayName = in.readComponent();
    var info = in.readComponent();
    int categoryItemsSize = in.readVarInt();
    var items = new ShopItem[categoryItemsSize];
    for (int j = 0; j < categoryItemsSize; j++) {
      var itemId = in.readUUID();
      var item = itemLookup.apply(itemId);
      if (item == null) {
        throw new IllegalStateException("Unknown item with ID: " + itemId.toString());
      }
      items[j] = item;
    }
    return new ShopCategory(displayName, info, List.of(items));
  }
}
