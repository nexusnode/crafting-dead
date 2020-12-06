/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
import org.lwjgl.util.yoga.Yoga;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

public class ComponentScreen extends Screen implements IView {

  private final ContainerComponent container;
  private final long node;

  protected ComponentScreen(ITextComponent title) {
    super(title);
    this.node = Yoga.YGNodeNew();
    this.minecraft = Minecraft.getInstance();
    this.width = this.minecraft.getMainWindow().getScaledWidth();
    this.height = this.minecraft.getMainWindow().getScaledHeight();
    this.container = new ContainerComponent();
    this.container.parent = this;
    Yoga.YGNodeInsertChild(this.node, this.container.node, 0);
  }

  protected final ContainerComponent getRoot() {
    return this.container;
  }

  @Override
  public void init() {
    Yoga.YGNodeCalculateLayout(this.node, this.getWidth(), this.getHeight(), Yoga.YGDirectionLTR);
    this.container.layout();
    double mouseX = this.minecraft.mouseHelper.getMouseX() * (double) this.width
        / (double) this.minecraft.getMainWindow().getWidth();
    double mouseY = this.minecraft.mouseHelper.getMouseY() * (double) this.height
        / (double) this.minecraft.getMainWindow().getHeight();
    this.container.mouseMoved(mouseX, mouseY);
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    this.container.mouseMoved(mouseX, mouseY);
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
  public List<Component<?>> getEventListeners() {
    return Collections.singletonList(this.container);
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
  public Screen getScreen() {
    return this;
  }
}
