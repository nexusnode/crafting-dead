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

package com.craftingdead.immerse.client.gui.screen.game.shop;

import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.module.shop.ShopCategory;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class CategoryButton extends GameButton implements InfoPanel {

  private final Component info;

  public CategoryButton(AbstractShopScreen screen, PlayerExtension<?> player, ShopCategory category) {
    super(0, 0, 0, 0, category.getDisplayName(),
        btn -> Minecraft.getInstance()
            .setScreen(new CategoryScreen(screen, player, category)));
    this.info = category.getInfo();
  }

  @Override
  public void renderInfo(int x, int y, PoseStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    this.font.drawShadow(matrixStack, this.getMessage(), x - 20, y - 65, 0xFFFFFFFF);
    this.font.drawWordWrap(this.info, x - 20, y - 45, 90, 0xFFFFFFFF);
  }
}
