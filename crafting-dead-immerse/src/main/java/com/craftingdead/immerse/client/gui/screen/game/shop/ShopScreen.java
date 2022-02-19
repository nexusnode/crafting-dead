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

package com.craftingdead.immerse.client.gui.screen.game.shop;

import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.module.shop.ClientShopModule;
import com.craftingdead.immerse.game.module.shop.ShopCategory;
import net.minecraft.client.gui.screens.Screen;

public class ShopScreen extends AbstractShopScreen {

  public ShopScreen(Screen lastScreen, ClientShopModule shop, PlayerExtension<?> player) {
    super(lastScreen, shop, player);
    for (ShopCategory category : this.getShop().getCategories()) {
      this.addShopButton(new CategoryButton(this, player, category));
    }
  }
}
