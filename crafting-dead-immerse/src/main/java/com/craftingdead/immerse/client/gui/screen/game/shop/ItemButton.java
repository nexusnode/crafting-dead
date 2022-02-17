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

import java.util.ArrayList;
import java.util.List;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.craftingdead.immerse.game.module.shop.ClientShopModule;
import com.craftingdead.immerse.game.module.shop.ShopItem;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.TooltipFlag.Default;

public class ItemButton extends GameButton implements InfoPanel {

  private final Font font = Minecraft.getInstance().font;

  private final ClientShopModule shop;
  private final ShopItem item;

  public ItemButton(ClientShopModule shop, ShopItem item) {
    super(0, 0, 0, 0, item.getItemStack().getDisplayName(), btn -> shop.buyItem(item.getId()));
    this.shop = shop;
    this.item = item;
  }

  private Component getFormattedPrice() {
    return new TextComponent("$" + this.item.getPrice())
        .withStyle(this.shop.canAfford(this.item.getPrice())
            ? ChatFormatting.GREEN
            : ChatFormatting.RED);
  }

  @Override
  public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
    if (this.item.getPrice() > 0) {
      RenderUtil.renderTextRight(this.font, matrixStack, x + width - 2, y + 7,
          this.getFormattedPrice(), 0, true);
    }
  }

  @Override
  public void renderInfo(int x, int y, PoseStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    this.font.drawShadow(matrixStack, this.getMessage(), x - 20, y - 65, 0xFFFFFFFF);

    drawCenteredString(matrixStack, this.font, this.getFormattedPrice(), x + 53, y - 75, 0);

    var modelViewStack = RenderSystem.getModelViewStack();
    modelViewStack.pushPose();
    modelViewStack.translate(x + 10, y - 40, 0);
    var scale = 1.2F;
    modelViewStack.scale(scale, scale, scale);
    com.craftingdead.core.client.util.RenderUtil.renderGuiItem(this.item.getItemStack(), 0, 0,
        0xFFFFFFFF, ItemTransforms.TransformType.FIXED);
    modelViewStack.popPose();

    List<Component> itemInfo = new ArrayList<>();
    this.item.getItemStack().getItem().appendHoverText(this.item.getItemStack(), null, itemInfo,
        Default.NORMAL);

    for (int i = 0; i < itemInfo.size(); i++) {
      Component info = itemInfo.get(i);
      this.font.drawShadow(matrixStack, info, x - 20,
          y + (i * this.font.lineHeight + 1), 0xFFFFFFFF);
    }
  }
}
