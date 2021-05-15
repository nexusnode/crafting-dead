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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.lwjgl.util.yoga.Yoga;
import com.craftingdead.immerse.client.gui.component.type.Align;
import com.craftingdead.immerse.client.gui.component.type.FlexDirection;
import com.craftingdead.immerse.client.gui.component.type.FlexWrap;
import com.craftingdead.immerse.client.gui.component.type.Justify;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IGuiEventListener;

public class ParentComponent<SELF extends ParentComponent<SELF>> extends Component<SELF>
    implements IParentView {

  private final List<Component<?>> children = new ArrayList<>();
  private Component<?>[] sortedChildren = new Component[0];

  private final Set<Component<?>> pendingRemoval = new HashSet<>();

  protected final long contentNode;

  @Nullable
  private IGuiEventListener focusedListener;
  private boolean dragging;

  public ParentComponent() {
    this.contentNode = Yoga.YGNodeNew();
  }

  public SELF addChild(Component<?> child) {
    if (this.pendingRemoval.remove(child)) {
      child.pendingRemoval = false;
    }

    child.index = this.children.size();
    this.children.add(child);
    this.sortedChildren = this.children.toArray(new Component[0]);
    this.sortChildren();

    Yoga.YGNodeInsertChild(this.contentNode, child.node, child.index);
    child.parent = this;
    child.added();

    return this.self();
  }

  private void removeChild(Component<?> childComponent) {
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
    this.children.forEach(Component::close);
    return this.self();
  }

  public List<Component<?>> getChildComponents() {
    return this.children;
  }

  @Override
  public float computeFullHeight() {
    final float height = (float) (this.children
        .stream()
        .mapToDouble(
            c -> c.getY() + c.getHeight() + c.getBottomMargin())
        .max()
        .orElse(0.0F)
        - super.getScaledContentY());

    return Math.max(height, 0);
  }

  @Override
  public void layout() {
    Yoga.YGNodeCalculateLayout(this.contentNode, this.getContentWidth(), this.getContentHeight(),
        Yoga.YGDirectionLTR);
    this.children.forEach(Component::layout);
    super.layout();
  }

  @Override
  public void tick() {
    if (!this.pendingRemoval.isEmpty()) {
      this.pendingRemoval.forEach((component) -> {
        this.children.remove(component);
        component.parent = null;
        Yoga.YGNodeRemoveChild(this.contentNode, component.node);
        component.pendingRemoval = false;
      });
      this.pendingRemoval.clear();

      for (int i = 0; i < this.children.size(); i++) {
        this.children.get(i).index = i;
      }

      this.sortedChildren = this.children.toArray(new Component[0]);
      this.sortChildren();
    }

    super.tick();

    this.children.forEach(Component::tick);
  }

  @Override
  public void renderContent(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTicks);
    for (Component<?> component : this.sortedChildren) {
      component.render(matrixStack, mouseX, mouseY, partialTicks);
    }
  }

  @Override
  public void close() {
    this.closeChildren();
    super.close();
  }

  @Override
  public void sortChildren() {
    Arrays.sort(this.sortedChildren);
  }

  @Override
  public List<? extends IGuiEventListener> children() {
    return Lists.reverse(Arrays.asList(this.sortedChildren));
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    IParentView.super.mouseMoved(mouseX, mouseY);
    super.mouseMoved(mouseX, mouseY);
  }

  @Override
  public boolean isMouseOver(double mouseX, double mouseY) {
    return IParentView.super.isMouseOver(mouseX, mouseY) || super.isMouseOver(mouseX, mouseY);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    return IParentView.super.mouseClicked(mouseX, mouseY, button)
        || super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX,
      double deltaY) {
    return IParentView.super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
        || super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
    return IParentView.super.mouseScrolled(mouseX, mouseY, scrollDelta)
        || super.mouseScrolled(mouseX, mouseY, scrollDelta);
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    return IParentView.super.keyPressed(keyCode, scanCode, modifiers)
        || super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
    return IParentView.super.keyReleased(keyCode, scanCode, modifiers)
        || super.keyReleased(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    return IParentView.super.charTyped(codePoint, modifiers)
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
    return super.changeFocus(focus) || IParentView.super.changeFocus(focus);
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

  public final SELF setFlexDirection(FlexDirection flexDirection) {
    Yoga.YGNodeStyleSetFlexDirection(this.contentNode, flexDirection.getYogaType());
    return this.self();
  }

  public final SELF setFlexWrap(FlexWrap flexDirection) {
    Yoga.YGNodeStyleSetFlexWrap(this.contentNode, flexDirection.getYogaType());
    return this.self();
  }

  public final SELF setAlignItems(Align align) {
    Yoga.YGNodeStyleSetAlignItems(this.contentNode, align.getYogaType());
    return this.self();
  }

  public final SELF setJustifyContent(Justify justify) {
    Yoga.YGNodeStyleSetJustifyContent(this.contentNode, justify.getYogaType());
    return this.self();
  }
}
