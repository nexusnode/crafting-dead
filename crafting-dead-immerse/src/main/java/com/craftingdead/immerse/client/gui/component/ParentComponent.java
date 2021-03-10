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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.lwjgl.util.yoga.Yoga;
import com.craftingdead.immerse.client.gui.component.event.ZLevelChangeEvent;
import com.craftingdead.immerse.client.gui.component.type.Align;
import com.craftingdead.immerse.client.gui.component.type.FlexDirection;
import com.craftingdead.immerse.client.gui.component.type.FlexWrap;
import com.craftingdead.immerse.client.gui.component.type.Justify;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IGuiEventListener;

public abstract class ParentComponent<SELF extends ParentComponent<SELF>> extends Component<SELF>
    implements IParentView {

  private final List<Component<?>> children = new ArrayList<>();
  private int prevChildIndex = -1;

  protected final long contentNode;

  @Nullable
  private IGuiEventListener focusedListener;
  private boolean dragging;

  public ParentComponent() {
    this.contentNode = Yoga.YGNodeNew();
  }

  @Override
  public float getActualContentHeight() {
    return Math.max(
        (float) (this.children
            .stream()
            .mapToDouble(
                c -> c.getY() + c.getHeight() + c.getBottomMargin())
            .max()
            .orElse(0.0F)
            - super.getContentY()),
        0);
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
    super.tick();
    this.children.forEach(Component::tick);
  }

  @Override
  public void renderContent(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTicks);
    this.children.forEach(component -> component.render(matrixStack, mouseX, mouseY, partialTicks));
  }

  @Override
  public List<? extends IGuiEventListener> children() {
    return this.children.stream()
        .sorted(Collections.reverseOrder())
        .collect(Collectors.toList());
  }

  public List<Component<?>> getChildComponents() {
    return this.children;
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

  @Override
  public void close() {
    this.closeChildren();
    super.close();
  }

  @Nullable
  @Override
  public IGuiEventListener getFocused() {
    return this.focusedListener;
  }

  @Override
  public boolean changeFocus(boolean focus) {
    if (!super.changeFocus(focus)) {
      return IParentView.super.changeFocus(focus);
    }
    return true;
  }

  @Override
  public void setFocused(@Nullable IGuiEventListener focusedListener) {
    this.focusedListener = focusedListener;
  }

  public SELF addChild(Component<?> child) {
    int zLevel = ++this.prevChildIndex;
    IParentView parent = this;
    while (parent != null) {
      zLevel += parent.getZLevel();
      parent = parent.getParent();
    }
    child.zLevel = zLevel;

    this.children.add(child);
    this.childAdded(child, this.children.size() - 1);
    return this.self();
  }

  private void sortChildren() {
    this.children.sort(Comparator.naturalOrder());
  }

  protected void childAdded(Component<?> child, int index) {
    Yoga.YGNodeInsertChild(this.contentNode, child.node, index);
    child.addListener(ZLevelChangeEvent.class,
        (component, zLevelChangeEvent) -> this.sortChildren());
    // TODO how to unregister the listener
    child.parent = this;
    this.sortChildren();
    child.added();
  }

  public SELF removeChild(Component<?> childComponent) {
    this.children.stream()
        .filter(childComponent::equals)
        .findAny()
        .ifPresent(child -> {
          this.childRemoved(child);
          this.children.remove(child);
        });
    return this.self();
  }

  protected void childRemoved(Component<?> child) {
    Yoga.YGNodeRemoveChild(this.contentNode, child.node);
    child.removed();
    child.parent = null;
  }

  public SELF clearChildren() {
    this.children.forEach(this::childRemoved);
    this.children.clear();
    return this.self();
  }

  public SELF closeChildren() {
    this.children.forEach(Component::close);
    return this.self();
  }

  public SELF clearChildrenClosing() {
    for (Component<?> child : this.children) {
      this.childRemoved(child);
      child.close();
    }
    this.children.clear();
    return this.self();
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    this.setDragging(false);
    if (this.getFocused() != null) {
      return this.getFocused().mouseReleased(mouseX, mouseY, button);
    }
    return false;
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
