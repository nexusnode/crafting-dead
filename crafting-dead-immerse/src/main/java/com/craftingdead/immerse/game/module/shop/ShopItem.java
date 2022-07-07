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

import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public record ShopItem(UUID id, ItemStack itemStack, int price) {

  public ShopItem(Item item) {
    this(UUID.randomUUID(), item, 0);
  }

  public ShopItem(ItemStack itemStack) {
    this(UUID.randomUUID(), itemStack, 0);
  }

  public ShopItem(UUID id, ItemLike item, int price) {
    this(id, item.asItem().getDefaultInstance(), price);
  }

  public void encode(FriendlyByteBuf out) {
    out.writeUUID(this.id);
    out.writeItemStack(this.itemStack, true);
    out.writeVarInt(this.price);
  }

  public static ShopItem decode(FriendlyByteBuf in) {
    return new ShopItem(in.readUUID(), in.readItem(), in.readVarInt());
  }
}
