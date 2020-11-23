/**
 * Crafting Dead 
 * Copyright (C) 2020 Nexus Node
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package com.craftingdead.immerse.client.gui.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.yoga.Yoga;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;

public abstract class ParentComponent<SELF extends ParentComponent<SELF>> extends Component<SELF>
    implements INestedGuiEventHandler {

  private final List<Component<?>> children = new ArrayList<>();

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
    Yoga.YGNodeFreeRecursive(this.contentNode);
  }

  @Override
  protected void layout() {
    super.layout();
    Yoga.YGNodeCalculateLayout(this.contentNode, this.getContentWidth(), this.getContentHeight(),
        Yoga.YGDirectionLTR);
    this.children.forEach(Component::layout);
  }

  @Override
  public void tick() {
    super.tick();
    for (Component<?> child : this.children()) {
      child.tick();
    }
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    this.renderChildren(mouseX, mouseY, partialTicks);
  }

  protected void renderChildren(int mouseX, int mouseY, float partialTicks) {
    for (Component<?> child : this.children()) {
      child.render(mouseX, mouseY, partialTicks);
    }
  }

  @Override
  public List<Component<?>> children() {
    return this.children;
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    super.mouseMoved(mouseX, mouseY);
    for (Component<?> child : this.children()) {
      child.mouseMoved(mouseX, mouseY);
    }
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (super.mouseClicked(mouseX, mouseY, button)) {
      return true;
    }

    final Component<?> component = this.getComponentForPos(mouseX, mouseY)
        .filter(c -> c.mouseClicked(mouseX, mouseY, button)).orElse(null);
    if (component == null) {
      this.setFocused(null);
      return false;
    } else {
      this.setFocused(component);
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

  @Nullable
  @Override
  public IGuiEventListener getFocused() {
    return this.focused;
  }

  @Override
  public void setFocused(@Nullable IGuiEventListener focused) {
    if (this.getFocused() != focused) {
      if (this.getFocused() instanceof Component) {
        ((Component<?>) this.getFocused()).focusChanged(false);
      }
      if (focused instanceof Component) {
        ((Component<?>) focused).focusChanged(true);
      }
    }
    this.focused = focused;
  }

  public SELF addChild(int index, Component<?> child) {
    this.children().add(index, child);
    this.childAdded(child, index);
    return this.self();
  }

  public SELF addChild(Component<?> child) {
    this.children().add(child);
    this.childAdded(child, this.children.size() - 1);
    return this.self();
  }

  protected void childAdded(Component<?> child, int index) {
    Yoga.YGNodeInsertChild(this.contentNode, child.node, index);
    child.parent = this;
    child.added();
  }

  public SELF removeChild(Component<?> child) {
    this.childRemoved(child);
    this.children().remove(child);
    return this.self();
  }

  protected void childRemoved(Component<?> child) {
    child.removed();
    child.parent = null;
    Yoga.YGNodeRemoveChild(this.contentNode, child.node);
  }

  public SELF clearChildren() {
    this.children.forEach(this::removeChild);
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
    for (int i = this.children().size() - 1; i >= 0; i--) {
      Component<?> component = this.children().get(i);
      if (component.isMouseOver(mouseX, mouseY)) {
        return Optional.of(component);
      }
    }
    return Optional.empty();
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    this.setDragging(false);
    if (this.getFocused() != null) {
      this.getFocused().mouseReleased(mouseX, mouseY, button);
    }
    return true;
  }

  public final SELF setFlexDirection(FlexDirection flexDirection) {
    Yoga.YGNodeStyleSetFlexDirection(this.contentNode, flexDirection.getYogaType());
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
}
