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

package com.craftingdead.immerse.game.module.shop;

import java.util.UUID;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ShopItem {

  private final UUID id;
  private final ItemStack itemStack;
  private final int price;

  public ShopItem(Item item) {
    this(UUID.randomUUID(), item, 0);
  }

  public ShopItem(ItemStack itemStack) {
    this(UUID.randomUUID(), itemStack, 0);
  }

  public ShopItem(UUID id, ItemLike item, int price) {
    this(id, item.asItem().getDefaultInstance(), price);
  }

  public ShopItem(UUID id, ItemStack itemStack, int price) {
    this.id = id;
    this.itemStack = itemStack;
    this.price = price;
  }

  public UUID getId() {
    return this.id;
  }

  public ItemStack getItemStack() {
    return this.itemStack;
  }

  public int getPrice() {
    return this.price;
  }
}
