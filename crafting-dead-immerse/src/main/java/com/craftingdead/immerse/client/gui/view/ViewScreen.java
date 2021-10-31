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
import java.util.function.Function;
import com.craftingdead.immerse.client.gui.view.layout.EmptyLayout;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

public final class ViewScreen extends Screen implements EmptyLayout {

  private final View<?, ?> view;

  private View<?, ?> lastHovered;

  private boolean keepOpen;

  private final ScissorStack viewStack;

  public ViewScreen(ITextComponent title,
      Function<ViewScreen, View<?, ViewScreen>> viewFactory) {
    super(title);
    this.minecraft = Minecraft.getInstance();
    this.width = this.minecraft.getWindow().getGuiScaledWidth();
    this.height = this.minecraft.getWindow().getGuiScaledHeight();
    this.view = viewFactory.apply(this);
    this.view.screen = this;
    this.viewStack = new ScissorStack();
  }

  public ScissorStack getScissorStack() {
    return this.viewStack;
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
    this.view.layout();
    // Reset mouse pos
    double scaledMouseX =
        this.minecraft.mouseHandler.xpos() / this.minecraft.getWindow().getGuiScale();
    double scaledMouseY =
        this.minecraft.mouseHandler.ypos() / this.minecraft.getWindow().getGuiScale();
    this.mouseMoved(scaledMouseX, scaledMouseY);
  }

  @Override
  public void removed() {
    if (this.keepOpen) {
      this.keepOpen = false;
      return;
    }
    this.view.close();
  }

  private void updateHovered(double mouseX, double mouseY) {
    // Update hovered views

    boolean keepLastHovered = false;
    View<?, ?> hovered = this.view.isMouseOver(mouseX, mouseY) ? this.view : null;
    while (hovered instanceof ParentView) {
      View<?, ?> nextHovered = ((ParentView<?, ?, ?>) hovered)
          .getChildAt(mouseX, mouseY)
          .filter(listener -> listener instanceof View)
          .map(listener -> (View<?, ?>) listener)
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
          && (!this.lastHovered.isMouseOver(mouseX, mouseY)
              || (hovered != null && hovered.compareTo(this.lastHovered) > 0))) {
        this.lastHovered.mouseLeft(mouseX, mouseY);
        this.lastHovered = this.lastHovered.getParent();
      }
    }

    this.lastHovered = hovered;
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    this.view.mouseMoved(mouseX, mouseY);
    this.updateHovered(mouseX, mouseY);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
    boolean result = this.view.mouseScrolled(mouseX, mouseY, scrollDelta);

    this.updateHovered(mouseX, mouseY);
    return result;
  }

  @Override
  public void tick() {
    this.view.tick();
  }

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.view.render(matrixStack, mouseX, mouseY, partialTicks);
  }

  @Override
  public List<? extends IGuiEventListener> children() {
    return Collections.singletonList(this.view);
  }

  @Override
  public float getWidth() {
    return this.width;
  }

  @Override
  public float getHeight() {
    return this.height;
  }
}
