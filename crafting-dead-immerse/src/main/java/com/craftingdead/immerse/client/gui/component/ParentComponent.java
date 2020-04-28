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

  private Component<?> lastMouseOver;

  public ParentComponent(RegionBuilder regionBuilder) {
    super(regionBuilder);
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    for (Component<?> child : this.children) {
      child.render(mouseX, mouseY, partialTicks);
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
    child.getParentProperty().set(this);
    return this.self();
  }

  private Optional<Component<?>> getComponentForPos(double mouseX, double mouseY) {
    return INestedGuiEventHandler.super.getEventListenerForPos(mouseX, mouseY)
        .map(e -> e instanceof Component ? (Component<?>) e : null);
  }

  @Override
  public void mouseEntered() {
    super.mouseEntered();
  }

  @Override
  public void mouseLeft() {
    super.mouseLeft();
    if (this.lastMouseOver != null) {
      this.lastMouseOver.mouseLeft();
      this.lastMouseOver = null;
    }
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    Component<?> current = this.getComponentForPos(mouseX, mouseY).orElse(null);
    if (current != this.lastMouseOver) {
      if (current != null) {
        current.mouseEntered();
      }
      if (this.lastMouseOver != null) {
        this.lastMouseOver.mouseLeft();
      }
    }
    if (current != null) {
      current.mouseMoved(mouseX, mouseY);
    }
    this.lastMouseOver = current;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    final boolean handled = this
        .getComponentForPos(mouseX, mouseY)
        .filter(component -> component.mouseClicked(mouseX, mouseY, button))
        .map(component -> {
          this.setFocused(component);
          if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
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
