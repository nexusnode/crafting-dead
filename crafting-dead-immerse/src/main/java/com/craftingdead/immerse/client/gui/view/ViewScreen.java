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

package com.craftingdead.immerse.client.gui.view;

import java.util.Collections;
import java.util.List;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.style.StyleSheetManager;
import com.craftingdead.immerse.client.gui.view.style.tree.StyleList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class ViewScreen extends Screen {

  private final ParentView root;
  private final List<? extends GuiEventListener> children;

  private StyleList styleList;

  private View lastHovered;

  private boolean keepOpen;

  private boolean layout;

  public ViewScreen(Component title, ParentView root) {
    super(title);
    this.minecraft = Minecraft.getInstance();
    this.width = this.minecraft.getWindow().getGuiScaledWidth();
    this.height = this.minecraft.getWindow().getGuiScaledHeight();
    this.root = root;
    this.root.setLayout(new Layout() {

      @Override
      public float getX() {
        return 0.0F;
      }

      @Override
      public float getY() {
        return 0.0F;
      }

      @Override
      public float getWidth() {
        return ViewScreen.this.width;
      }

      @Override
      public float getHeight() {
        return ViewScreen.this.height;
      }
    });
    this.root.screen = this;
    // Don't use List::of as it does not permit null values.
    this.children = Collections.singletonList(this.root);
  }

  public ParentView getRoot() {
    return this.root;
  }

  public void keepOpenAndSetScreen(Screen screen) {
    this.keepOpen();
    this.minecraft.setScreen(screen);
  }

  public void keepOpen() {
    this.keepOpen = true;
  }

  @Override
  public void init() {

    if (!this.root.isAdded()) {
      this.root.added();
    }

    this.layout = true;
  }

  @Override
  public void removed() {
    if (this.keepOpen) {
      this.keepOpen = false;
      return;
    }
    this.root.removed();
    this.root.close();
  }

  private void updateHovered(double mouseX, double mouseY) {
    var keepLastHovered = false;
    View hovered = this.root.isMouseOver(mouseX, mouseY) ? this.root : null;
    while (hovered instanceof ParentView parent) {
      var nextHovered = parent
          .getChildAt(mouseX, mouseY)
          .filter(listener -> listener instanceof View)
          .map(listener -> (View) listener)
          .orElse(null);

      if (nextHovered == null) {
        break;
      }

      hovered = nextHovered;

      if (hovered == this.lastHovered) {
        keepLastHovered = true;
      }

      if (!hovered.isHovered()) {
        hovered.mouseEntered(mouseX, mouseY);
      }
    }

    if (!keepLastHovered) {
      while (this.lastHovered != null
          && (!this.lastHovered.isAdded()
              || !this.lastHovered.isMouseOver(mouseX, mouseY)
              || (hovered != null && hovered.compareTo(this.lastHovered) > 0))) {
        this.lastHovered.mouseLeft(mouseX, mouseY);
        this.lastHovered = this.lastHovered.getParent();
      }
    }

    this.lastHovered = hovered;
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    this.root.mouseMoved(mouseX, mouseY);
    this.updateHovered(mouseX, mouseY);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
    var result = this.root.mouseScrolled(mouseX, mouseY, scrollDelta);
    this.updateHovered(mouseX, mouseY);
    return result;
  }

  @Override
  public void tick() {
    if (this.layout) {
      this.layout = false;
      this.root.layout();

      // Reset mouse pos
      var scaledMouseX =
          this.minecraft.mouseHandler.xpos() / this.minecraft.getWindow().getGuiScale();
      var scaledMouseY =
          this.minecraft.mouseHandler.ypos() / this.minecraft.getWindow().getGuiScale();
      this.mouseMoved(scaledMouseX, scaledMouseY);
    }
    this.root.tick();
  }

  @Override
  public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
    this.root.render(poseStack, mouseX, mouseY, partialTicks);
  }

  @Override
  public List<? extends GuiEventListener> children() {
    return this.root.isAdded() ? this.children : Collections.emptyList();
  }

  public void setStylesheets(List<ResourceLocation> stylesheets) {
    this.styleList = StyleSheetManager.getInstance().createStyleList(stylesheets);
    this.root.refreshStyle();
  }

  public StyleList getStyleList() {
    return this.styleList;
  }
}
