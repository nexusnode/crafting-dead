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

package com.craftingdead.immerse.client.gui.component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.lwjgl.util.yoga.Yoga;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

public class ComponentScreen extends Screen implements IParentView {

  private final ParentComponent<?> container;
  private final long node;

  private boolean hovered;

  private IView lastHovered;

  protected ComponentScreen(ITextComponent title) {
    super(title);
    this.node = Yoga.YGNodeNew();
    this.minecraft = Minecraft.getInstance();
    this.width = this.minecraft.getWindow().getGuiScaledWidth();
    this.height = this.minecraft.getWindow().getGuiScaledHeight();
    this.container = new ParentComponent<>();
    this.container.parent = this;
    Yoga.YGNodeInsertChild(this.node, this.container.node, 0);
  }

  protected final ParentComponent<?> getRoot() {
    return this.container;
  }

  @Override
  public void init() {
    Yoga.YGNodeCalculateLayout(this.node, this.getWidth(), this.getHeight(), Yoga.YGDirectionLTR);
    this.container.layout();
    double mouseX = this.minecraft.mouseHandler.xpos() * (double) this.width
        / (double) this.minecraft.getWindow().getWidth();
    double mouseY = this.minecraft.mouseHandler.ypos() * (double) this.height
        / (double) this.minecraft.getWindow().getHeight();
    this.mouseMoved(mouseX, mouseY);
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    this.container.mouseMoved(mouseX, mouseY);

    IView hovered = this.container.isMouseOver(mouseX, mouseY) ? this.container : null;
    while (hovered instanceof ParentComponent) {
      IView nextHovered = ((ParentComponent<?>) hovered)
          .getChildAt(mouseX, mouseY)
          .filter(listener -> listener instanceof IView)
          .map(listener -> (IView) listener)
          .orElse(null);

      if (nextHovered == null) {
        break;
      }

      hovered = nextHovered;

      if (!hovered.isHovered()) {
        hovered.mouseEntered(mouseX, mouseY);
      }
    }

    while (this.lastHovered != null && (!this.lastHovered.isMouseOver(mouseX, mouseY)
        || (hovered != null && hovered.compareTo(this.lastHovered) > 0))) {
      this.lastHovered.mouseLeft(mouseX, mouseY);
      this.lastHovered = this.lastHovered.getParent().orElse(null);
    }

    this.lastHovered = hovered;
  }

  @Override
  public void tick() {
    this.container.tick();
  }

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.container.render(matrixStack, mouseX, mouseY, partialTicks);
  }

  @Override
  public List<? extends IGuiEventListener> children() {
    return Collections.singletonList(this.container);
  }

  @Override
  public void mouseEntered(double mouseX, double mouseY) {
    this.hovered = true;
  }

  @Override
  public void mouseLeft(double mouseX, double mouseY) {
    this.hovered = false;
  }

  @Override
  public float getWidth() {
    return this.width;
  }

  @Override
  public float getHeight() {
    return this.height;
  }

  @Override
  public float getZOffset() {
    return 0.0F;
  }

  @Override
  public Optional<IParentView> getParent() {
    return Optional.empty();
  }

  @Override
  public boolean isHovered() {
    return this.hovered;
  }
}
