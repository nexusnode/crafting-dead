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

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import com.craftingdead.immerse.client.gui.component.event.ZLevelChangeEvent;
import com.craftingdead.immerse.client.gui.component.type.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.yoga.Yoga;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;

public abstract class ParentComponent<SELF extends ParentComponent<SELF>> extends Component<SELF>
    implements INestedGuiEventHandler {

  private final List<Child> children = new ArrayList<>();
  private int prevChildIndex = -1;

  protected final long contentNode;

  @Nullable
  private IGuiEventListener focused;
  private boolean dragging;

  public ParentComponent() {
    this.contentNode = Yoga.YGNodeNew();
  }

  @Override
  protected void removed() {
    super.removed();
  }

  @Override
  protected void layout() {
    super.layout();
    Yoga.YGNodeCalculateLayout(this.contentNode, this.getContentWidth(), this.getContentHeight(),
        Yoga.YGDirectionLTR);
    this.getChildren().forEach(child -> child.getComponent().layout());
  }

  public float getTopScissorBoundScaled() {
    float bound = this.getOverflow() == Overflow.HIDDEN ? Float.MIN_VALUE : this.getScaledY();
    if (parent == null || !(parent instanceof ParentComponent)) {
      return bound;
    }
    return Math.max(bound, ((ParentComponent<?>)this.parent).getTopScissorBoundScaled());
  }

  public float getBotScissorBoundScaled() {
    float bound = this.getOverflow() == Overflow.HIDDEN ? Float.MAX_VALUE : this.getScaledY() + this.getScaledHeight();
    if (parent == null || !(parent instanceof ParentComponent)) {
      return bound;
    }
    return Math.min(bound, ((ParentComponent<?>)this.parent).getBotScissorBoundScaled());
  }

  @Override
  public void tick() {
    super.tick();
    for (Child child : this.getChildren()) {
      child.getComponent().tick();
    }
  }

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.render(matrixStack, mouseX, mouseY, partialTicks);
    this.renderChildren(matrixStack, mouseX, mouseY, partialTicks);
  }

  protected void renderChildren(MatrixStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    for (Child child : this.getChildren()) {
      child.getComponent().render(matrixStack, mouseX, mouseY, partialTicks);
    }
  }

  @Override
  public List<Component<?>> getEventListeners() {
    return this.getChildrenComponents();
  }

  public List<Child> getChildren() {
    return this.children;
  }

  public List<Component<?>> getChildrenComponents() {
    return this.getChildren().stream()
        .map(Child::getComponent)
        .collect(Collectors.toList());
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    super.mouseMoved(mouseX, mouseY);
    for (Child child : this.getChildren()) {
      child.getComponent().mouseMoved(mouseX, mouseY);
    }

    Optional<Component<?>> mouseOverChild = this.getComponentForPos(mouseX, mouseY);
    if (this.isMouseOver()) {
      mouseOverChild
          .filter(component -> !component.isMouseOver())
          .ifPresent(component -> component.mouseEntered(mouseX,mouseY));
    }
    this.getChildren().stream()
        .filter(child -> child.getComponent().isMouseOver() &&
            (!mouseOverChild.isPresent() || !mouseOverChild.get().equals(child.getComponent())))
        .forEach(child -> child.getComponent().mouseLeft(mouseX, mouseY));
  }

  @Override
  public boolean isMouseOver(double mouseX, double mouseY) {
    boolean mouseOverThis = super.isMouseOver(mouseX, mouseY);
    if (mouseOverThis) {
      return true;
    }
    //for cases when a child is overflowing its parent (e.g. expanded dropdown)
    for (Child child : this.getChildren()) {
      if (child.getComponent().isMouseOver(mouseX, mouseY)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (super.mouseClicked(mouseX, mouseY, button)) {
      return true;
    }

    final Component<?> component = this.getComponentForPos(mouseX, mouseY)
        .filter(c -> c.mouseClicked(mouseX, mouseY, button))
        .orElse(null);
    if (component == null) {
      this.setListener(null);
      return false;
    } else {
      this.setListener(component);
      if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
        this.setDragging(true);
      }
      return true;
    }
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX,
      double deltaY) {
    return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
        || INestedGuiEventHandler.super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
    return super.mouseScrolled(mouseX, mouseY, scrollDelta)
        || this.getComponentForPos(mouseX, mouseY)
            .filter(component -> component.mouseScrolled(mouseX, mouseY, scrollDelta)).isPresent();
  }

  @Override
  public boolean keyPressed(int key, int scancode, int mods) {
    return super.keyPressed(key, scancode, scancode)
        || INestedGuiEventHandler.super.keyPressed(key, scancode, scancode);
  }

  @Override
  public boolean keyReleased(int key, int scancode, int mods) {
    return super.keyReleased(key, scancode, scancode)
        || INestedGuiEventHandler.super.keyReleased(key, scancode, mods);
  }

  @Override
  public boolean charTyped(char character, int mods) {
    return super.charTyped(character, mods)
        || INestedGuiEventHandler.super.charTyped(character, mods);
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
  public IGuiEventListener getListener() {
    return this.focused;
  }

  @Override
  public void setListener(@Nullable IGuiEventListener focused) {
    if (this.getListener() != focused) {
      if (this.getListener() instanceof Component) {
        ((Component<?>) this.getListener()).focusChanged(false);
      }
      if (focused instanceof Component) {
        ((Component<?>) focused).focusChanged(true);
      }
    }
    this.focused = focused;
  }

  public SELF addChild(Component<?> child) {
    this.getChildren().add(new Child(child, ++prevChildIndex));
    this.childAdded(child, this.getChildren().size() - 1);
    return this.self();
  }

  private void sortChildren() {
    this.getChildren().sort(Comparator.naturalOrder());
  }

  protected void childAdded(Component<?> child, int index) {
    Yoga.YGNodeInsertChild(this.contentNode, child.node, index);
    child.addListener(ZLevelChangeEvent.class, (component, zLevelChangeEvent) -> this.sortChildren());
    //TODO how to unregister the listener
    child.parent = this;
    sortChildren();
    child.added();
  }

  public SELF removeChild(Component<?> childComponent) {
    this.getChildren().stream().
        filter(child -> child.getComponent().equals(childComponent))
        .findAny()
        .ifPresent(child -> {
          this.childRemoved(child);
          this.getChildren().remove(child);
        });
    return this.self();
  }

  protected void childRemoved(Child child) {
    Yoga.YGNodeRemoveChild(this.contentNode, child.getComponent().node);
    child.getComponent().removed();
    child.getComponent().parent = null;
  }

  public SELF clearChildren() {
    this.getChildren().forEach(this::childRemoved);
    this.getChildren().clear();
    return this.self();
  }

  public SELF closeChildren() {
    this.getChildren().forEach(child -> child.getComponent().close());
    return this.self();
  }

  public SELF clearChildrenClosing() {
    for (Child child : this.getChildren()) {
      this.childRemoved(child);
      child.getComponent().close();
    }
    this.getChildren().clear();
    return this.self();
  }

  /**
   * Get a {@link Component} at the specified mouse position, similar to
   * {@link #getEventListenerForPos(double, double)} but searches in reverse.
   * 
   * @param mouseX
   * @param mouseY
   * @return the {@link Component} if found
   */
  public Optional<Component<?>> getComponentForPos(double mouseX, double mouseY) {
    ListIterator<Child> iter = this.getChildren().listIterator(this.getChildren().size());
    while (iter.hasPrevious()) {
      Component<?> component = iter.previous().getComponent();
      if (component.isMouseOver(mouseX, mouseY)) {
        return Optional.of(component);
      }
    }
    return Optional.empty();
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    this.setDragging(false);
    if (this.getListener() != null) {
      this.getListener().mouseReleased(mouseX, mouseY, button);
    }
    return true;
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

  public final Overflow getOverflow() {
    return Overflow.fromYogaType(Yoga.YGNodeStyleGetOverflow(this.contentNode));
  }

  public final SELF setOverflow(Overflow overflow) {
    Yoga.YGNodeStyleSetOverflow(this.contentNode, overflow.getYogaType());
    return this.self();
  }

  public static class Child implements Comparable<Child> {
    private final Component<?> component;
    private final int insertionId;

    public Child(Component<?> component, int insertionId) {
      this.component = component;
      this.insertionId = insertionId;
    }

    public int getInsertionId() {
      return insertionId;
    }

    public Component<?> getComponent() {
      return component;
    }


    @Override
    public int compareTo(Child another) {
      if (another == null) {
        return 1;
      }
      if (this.getComponent().getZLevel() < another.getComponent().getZLevel()) {
        return -1;
      } else if (this.getComponent().getZLevel() > another.getComponent().getZLevel()) {
        return 1;
      } else {
        return Integer.compare(this.insertionId, another.insertionId);
      }
    }
  }
}
