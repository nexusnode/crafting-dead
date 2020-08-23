package com.craftingdead.immerse.client.gui.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;

abstract class ParentComponent<SELF extends ParentComponent<SELF>> extends Component<SELF>
    implements INestedGuiEventHandler {

  private final List<Component<?>> children = new ArrayList<>();

  @Nullable
  private IGuiEventListener focused;
  private boolean dragging;

  @Override
  protected void resized() {
    for (Component<?> child : this.children) {
      child.resized();
    }
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    for (Component<?> child : this.children) {
      child.render(mouseX, mouseY, partialTicks);
    }
  }

  @Override
  public void removed() {
    for (Component<?> child : this.children) {
      child.removed();
    }
  }

  @Override
  public void tick() {
    for (Component<?> child : this.children) {
      child.tick();
    }
  }

  @Override
  public List<Component<?>> children() {
    return this.children;
  }

  public SELF addChild(Component<?> child) {
    this.children.add(child);
    child.parent = Optional.of(this);
    child.added();
    return this.self();
  }

  private Optional<Component<?>> getComponentForPos(double mouseX, double mouseY) {
    for (int i = this.children.size() - 1; i > 0; i--) {
      Component<?> component = this.children.get(i);
      if (component.isMouseOver(mouseX, mouseY)) {
        if (component instanceof ParentComponent) {
          ParentComponent<?> parent = (ParentComponent<?>) component;
          return parent.getComponentForPos(mouseX, mouseY);
        } else if (component instanceof Component) {
          return Optional.of((Component<?>) component);
        }
      }
    }
    return Optional.empty();
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    super.mouseMoved(mouseX, mouseY);
    for (Component<?> child : this.children) {
      child.mouseMoved(mouseX, mouseY);
    }
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    final boolean handled = this
        .getComponentForPos(mouseX, mouseY)
        .filter(component -> component.mouseClicked(mouseX, mouseY, button))
        .filter(component -> component.changeFocus(true))
        .map(component -> {
          if(this.)
          if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (component.changeFocus(true)) {
              this.setFocused(component);
            }
            this.setDragging(true);
          }
          return true;
        })
        .orElse(false);
    return handled ? true : super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    if (INestedGuiEventHandler.super.mouseReleased(mouseX, mouseY, button)) {
      return true;
    }
    return super.mouseReleased(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX,
      double deltaY) {
    if (INestedGuiEventHandler.super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
      return true;
    }
    return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
    if (INestedGuiEventHandler.super.mouseScrolled(mouseX, mouseY, scrollDelta)) {
      return true;
    }
    return super.mouseScrolled(mouseX, mouseY, scrollDelta);
  }

  @Override
  public boolean keyPressed(int key, int scancode, int mods) {
    if (INestedGuiEventHandler.super.keyPressed(key, scancode, scancode)) {
      return true;
    }
    return super.keyPressed(key, scancode, scancode);
  }

  @Override
  public boolean keyReleased(int key, int scancode, int mods) {
    if (INestedGuiEventHandler.super.keyReleased(key, scancode, mods)) {
      return true;
    }
    return super.keyReleased(key, scancode, scancode);
  }

  @Override
  public boolean charTyped(char character, int mods) {
    if (INestedGuiEventHandler.super.charTyped(character, mods)) {
      return true;
    }
    return super.charTyped(character, mods);
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
    this.focused = focused;
  }
}
