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

class ShopModule implements GameModule, Synched {

  protected final Map<UUID, ShopItem> items = new HashMap<>();
  private final List<ShopCategory> categories = new ArrayList<>();

  public List<ShopCategory> getCategories() {
    return Collections.unmodifiableList(this.categories);
  }

  public void addCategory(ShopCategory category) {
    for (var item : category.items()) {
      this.items.put(item.id(), item);
    }
    this.categories.add(category);
  }

  @Override
  public void encode(FriendlyByteBuf out, boolean writeAll) {
    if (writeAll) {
      out.writeVarInt(this.items.size());
      this.items.values().forEach(item -> item.encode(out));
    } else {
      out.writeVarInt(-1);
    }

    if (writeAll) {
      out.writeVarInt(this.categories.size());
      this.categories.forEach(category -> category.encode(out));
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
        var item = ShopItem.decode(in);
        this.items.put(item.id(), item);
      }
    }

    int categoriesSize = in.readVarInt();
    if (categoriesSize > 0) {
      this.categories.clear();
      for (int i = 0; i < categoriesSize; i++) {
        this.categories.add(ShopCategory.decode(in, this.items::get));
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
