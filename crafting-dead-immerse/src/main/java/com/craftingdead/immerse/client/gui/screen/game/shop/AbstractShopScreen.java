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

package com.craftingdead.immerse.client.gui.screen.game.shop;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.craftingdead.immerse.game.module.shop.ClientShopModule;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public abstract class AbstractShopScreen extends Screen {

  private static final Component TITLE =
      new TranslatableComponent("gui.screen.shop.title");

  private final List<GameButton> shopButtons = new ArrayList<>();

  private final Screen lastScreen;

  private final ClientShopModule shop;

  private final PlayerExtension<?> player;

  private int cachedBuyTime = -1;

  public AbstractShopScreen(Screen lastScreen, ClientShopModule shop, PlayerExtension<?> player) {
    super(TITLE);
    this.lastScreen = lastScreen;
    this.shop = shop;
    this.player = player;
  }

  protected void addShopButton(GameButton shopButton) {
    this.shopButtons.add(shopButton);
  }

  public ClientShopModule getShop() {
    return this.shop;
  }

  @Override
  public void init() {
    int mx = this.width / 2;
    int my = this.height / 2;

    int x = mx - 140;
    int y = my - 72;
    int w1 = 100;
    int h1 = 20;
    int ym = 22;

    for (int i = 0; i < this.shopButtons.size(); i++) {
      var shopButton = this.shopButtons.get(i);
      shopButton.x = x;
      shopButton.y = y + (i * ym);
      shopButton.setWidth(w1);
      shopButton.setHeight(h1);
      this.addRenderableWidget(shopButton);
    }
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (super.keyPressed(keyCode, scanCode, modifiers)) {
      return true;
    } else if (keyCode == GLFW.GLFW_KEY_B) {
      this.minecraft.setScreen(this.lastScreen);
      return true;
    }
    return false;
  }

  @Override
  public void tick() {
    this.cachedBuyTime = this.shop.getBuyTimeSeconds();
    if (this.cachedBuyTime <= 0) {
      this.onClose();
    }
  }

  @Override
  public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(poseStack);

    int mx = this.width / 2;
    int my = this.height / 2;

    // Render Top and Bottom margins
    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderUtil.fillWithShadow(poseStack, 0, 0, this.width, 30, 0x80000000);
    RenderUtil.fillWithShadow(poseStack, 0, this.height - 30, this.width, 30, 0x80000000);

    drawCenteredString(poseStack, font,
        new TranslatableComponent("gui.screen.shop.back", "B").withStyle(ChatFormatting.ITALIC),
        mx - 150, 18, 0xFFFFFFFF);

    drawCenteredString(poseStack, this.font,
        this.getTitle().copy().withStyle(ChatFormatting.BOLD),
        mx, 10, 0xFFFFFFFF);

    RenderUtil.renderTextRight(this.font, poseStack, mx + 150, 18,
        new TranslatableComponent("gui.screen.shop.buy_time",
            new TextComponent(String.valueOf(this.cachedBuyTime))
                .withStyle(ChatFormatting.RED))
                    .withStyle(ChatFormatting.ITALIC),
        0xFFFFFFFF, true);

    // render slots and background
    int bh = this.shopButtons.size() * 22;
    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderUtil.fillWithShadow(poseStack, mx - 150, my - 80, 120, bh + 15, 0x80000000);

    super.render(poseStack, mouseX, mouseY, partialTicks);

    // render info of item over
    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderUtil.fillWithShadow(poseStack, mx - 25, my - 80, 115, 160, 0x80000000);
    this.font.drawShadow(poseStack,
        new TranslatableComponent("gui.screen.shop.selected").withStyle(ChatFormatting.BOLD),
        mx - 20, my - 75, 0xFFFFFFFF);

    for (var shopButton : this.shopButtons) {
      if (shopButton instanceof InfoPanel infoPanel && shopButton.isHoveredOrFocused()) {
        infoPanel.renderInfo(mx, my, poseStack, mouseX, mouseY, partialTicks);
      }
    }

    final int spacing = 5;
    final int moneyHeight = 15;

    boolean renderMoney = this.shop.getMoney() > -1;
    if (renderMoney) {
      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      RenderUtil.fillWithShadow(poseStack, mx + 95, my - 80, 69, moneyHeight, 0x80000000);
      this.font.drawShadow(poseStack,
          new TextComponent("$" + this.shop.getMoney())
              .withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN),
          mx + 100, my - 76, 0);
    }

    int inventoryYOffset = renderMoney ? moneyHeight + spacing : 0;

    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderUtil.fillWithShadow(poseStack, mx + 95, my - 80 + inventoryYOffset, 69, 140,
        0x80000000);
    this.font.drawShadow(poseStack,
        new TranslatableComponent("gui.screen.shop.inventory").withStyle(ChatFormatting.BOLD),
        mx + 100, my - 75 + inventoryYOffset, 0xFFFFFFFF);


    var inventory = this.player.entity().getInventory();
    for (int i = 0; i < 7; i++) {
      var itemStack = inventory.getItem(i);
      com.craftingdead.core.client.util.RenderUtil.renderGuiItem(poseStack, itemStack, mx + 120,
          my - 60 + inventoryYOffset + (i * 21), 0xFFFFFFFF,
          ItemTransforms.TransformType.FIXED);
    }
  }
}
