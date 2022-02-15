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

package com.craftingdead.immerse.client.gui.view;

import java.util.Collections;
import java.util.List;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.style.StylesheetManager;
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

  public ViewScreen(Component title, ParentView root) {
    super(title);
    this.minecraft = Minecraft.getInstance();
    this.width = this.minecraft.getWindow().getGuiScaledWidth();
    this.height = this.minecraft.getWindow().getGuiScaledHeight();
    this.root = root;
    this.root.setLayout(new Layout() {

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

    this.root.layout();

    // Reset mouse pos
    var scaledMouseX =
        this.minecraft.mouseHandler.xpos() / this.minecraft.getWindow().getGuiScale();
    var scaledMouseY =
        this.minecraft.mouseHandler.ypos() / this.minecraft.getWindow().getGuiScale();
    this.mouseMoved(scaledMouseX, scaledMouseY);
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
    this.styleList = StylesheetManager.getInstance().refreshStylesheets(stylesheets);
    this.root.refreshStyle();
  }

  public StyleList getStyleList() {
    return this.styleList;
  }
}
