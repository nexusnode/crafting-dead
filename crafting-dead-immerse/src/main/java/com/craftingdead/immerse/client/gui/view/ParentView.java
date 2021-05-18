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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.LayoutParent;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;

public class ParentView<SELF extends ParentView<SELF, L, C>, L extends Layout, C extends Layout>
    extends View<SELF, L>
    implements INestedGuiEventHandler {

  private final List<View<?, C>> children = new ArrayList<>();
  @SuppressWarnings("unchecked")
  private View<?, C>[] sortedChildren = new View[0];

  private final Set<View<?, C>> pendingRemoval = new HashSet<>();

  protected final LayoutParent<C> layoutParent;

  @Nullable
  private IGuiEventListener focusedListener;
  private boolean dragging;

  public ParentView(L layout, LayoutParent<C> layoutParent) {
    super(layout);
    this.layoutParent = layoutParent;
  }

  @SuppressWarnings("unchecked")
  public SELF addChild(View<?, C> child) {
    this.updatePendingRemoval();

    child.index = this.children.size();
    this.children.add(child);
    this.sortedChildren = this.children.toArray(new View[0]);
    this.sortChildren();

    this.layoutParent.addChild(child.getLayout(), child.index);
    child.parent = this;
    child.added();

    return this.self();
  }

  private void removeChild(View<?, C> childComponent) {
    childComponent.removed(() -> {
      childComponent.pendingRemoval = true;
      this.pendingRemoval.add(childComponent);
    });
  }

  public SELF clearChildren() {
    this.children.forEach(this::removeChild);
    return this.self();
  }

  public SELF closeChildren() {
    this.children.forEach(View::close);
    return this.self();
  }

  public List<? extends View<?, C>> getChildComponents() {
    return this.children;
  }

  @Override
  public float computeFullHeight() {
    final float height = (float) (this.children
        .stream()
        .mapToDouble(c -> c.getY() + c.getHeight()) // TODO + c.getBottomMargin()
        .max()
        .orElse(0.0F)
        - super.getScaledContentY());

    return Math.max(height, 0);
  }

  @Override
  public void layout() {
    this.layoutParent.layout(this.getContentWidth(), this.getContentHeight());
    this.children.forEach(View::layout);
    super.layout();
  }

  @SuppressWarnings("unchecked")
  private void updatePendingRemoval() {
    if (!this.pendingRemoval.isEmpty()) {
      this.pendingRemoval.forEach((component) -> {
        this.children.remove(component);
        component.parent = null;
        this.layoutParent.removeChild(component.getLayout());
        component.pendingRemoval = false;
      });
      this.pendingRemoval.clear();

      for (int i = 0; i < this.children.size(); i++) {
        this.children.get(i).index = i;
      }

      this.sortedChildren = this.children.toArray(new View[0]);
      this.sortChildren();
    }
  }


  @Override
  public void tick() {
    this.updatePendingRemoval();
    super.tick();
    this.children.forEach(View::tick);
  }

  @Override
  public void renderContent(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTicks);
    for (View<?, ?> component : this.sortedChildren) {
      component.render(matrixStack, mouseX, mouseY, partialTicks);
    }
  }

  @Override
  public void close() {
    this.closeChildren();
    super.close();
  }

  public void sortChildren() {
    Arrays.sort(this.sortedChildren);
  }

  @Override
  public List<? extends IGuiEventListener> children() {
    return Lists.reverse(Arrays.asList(this.sortedChildren));
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    this.children().forEach(listener -> listener.mouseMoved(mouseX, mouseY));
    super.mouseMoved(mouseX, mouseY);

  }

  @Override
  public boolean isMouseOver(double mouseX, double mouseY) {
    return this.children().stream()
        .anyMatch(listener -> listener.isMouseOver(mouseX, mouseY))
        || super.isMouseOver(mouseX, mouseY);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    return INestedGuiEventHandler.super.mouseClicked(mouseX, mouseY, button)
        || super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX,
      double deltaY) {
    return INestedGuiEventHandler.super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
        || super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
    return INestedGuiEventHandler.super.mouseScrolled(mouseX, mouseY, scrollDelta)
        || super.mouseScrolled(mouseX, mouseY, scrollDelta);
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    return INestedGuiEventHandler.super.keyPressed(keyCode, scanCode, modifiers)
        || super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
    return INestedGuiEventHandler.super.keyReleased(keyCode, scanCode, modifiers)
        || super.keyReleased(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    return INestedGuiEventHandler.super.charTyped(codePoint, modifiers)
        || super.charTyped(codePoint, modifiers);
  }

  @Override
  public boolean isDragging() {
    return this.dragging;
  }

  @Override
  public void setDragging(boolean dragging) {
    this.dragging = dragging;
  }

  @Nullable
  @Override
  public IGuiEventListener getFocused() {
    return this.focusedListener;
  }

  @Override
  public boolean changeFocus(boolean focus) {
    return super.changeFocus(focus) || INestedGuiEventHandler.super.changeFocus(focus);
  }

  @Override
  public void setFocused(@Nullable IGuiEventListener focusedListener) {
    this.focusedListener = focusedListener;
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    this.setDragging(false);
    return this.getFocused() != null && this.getFocused().mouseReleased(mouseX, mouseY, button);
  }
}
