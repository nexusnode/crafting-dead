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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.craftingdead.core.network.Synched;
import com.craftingdead.immerse.game.module.GameModule;
import com.craftingdead.immerse.game.module.ModuleType;
import com.craftingdead.immerse.game.module.ModuleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

class ShopModule implements GameModule, Synched {

  protected final Map<UUID, ShopItem> items = new HashMap<>();
  private final List<ShopCategory> categories = new ArrayList<>();

  public List<ShopCategory> getCategories() {
    return Collections.unmodifiableList(this.categories);
  }

  public void addCategory(ShopCategory category) {
    for (ShopItem item : category.getItems()) {
      this.items.put(item.getId(), item);
    }
    this.categories.add(category);
  }

  @Override
  public void encode(FriendlyByteBuf out, boolean writeAll) {
    if (writeAll) {
      out.writeVarInt(this.items.size());
      for (ShopItem item : this.items.values()) {
        out.writeUUID(item.getId());
        out.writeItemStack(item.getItemStack(), true);
        out.writeVarInt(item.getPrice());
      }
    } else {
      out.writeVarInt(-1);
    }

    if (writeAll) {
      out.writeVarInt(this.categories.size());
      for (ShopCategory category : this.categories) {
        out.writeComponent(category.getDisplayName());
        out.writeComponent(category.getInfo());
        out.writeVarInt(category.getItems().size());
        for (ShopItem item : category.getItems()) {
          out.writeUUID(item.getId());
        }
      }
    } else {
      out.writeVarInt(-1);
    }
  }

  @Override
  public void decode(FriendlyByteBuf in) {
    int itemsSize = in.readVarInt();
    if (itemsSize > 0) {
      this.items.clear();
      for (int i = 0; i < itemsSize; i++) {
        ShopItem item = new ShopItem(in.readUUID(), in.readItem(), in.readVarInt());
        this.items.put(item.getId(), item);
      }
    }

    int categoriesSize = in.readVarInt();
    if (categoriesSize > 0) {
      this.categories.clear();
      for (int i = 0; i < categoriesSize; i++) {
        Component displayName = in.readComponent();
        Component info = in.readComponent();
        List<ShopItem> items = new ArrayList<>();
        int categoryItemsSize = in.readVarInt();
        for (int j = 0; j < categoryItemsSize; j++) {
          UUID itemId = in.readUUID();
          ShopItem item = this.items.get(itemId);
          if (item == null) {
            throw new IllegalStateException("Unknown item with ID: " + itemId.toString());
          }
          items.add(item);
        }
        this.categories.add(new ShopCategory(displayName, info, items));
      }
    }
  }

  @Override
  public boolean requiresSync() {
    return false;
  }

  @Override
  public ModuleType getType() {
    return ModuleTypes.SHOP.get();
  }
}
