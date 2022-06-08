package sm0keysa1m0n.bliss.view;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import sm0keysa1m0n.bliss.layout.Layout;
import sm0keysa1m0n.bliss.style.StyleList;
import sm0keysa1m0n.bliss.style.StyleSheetManager;

public final class ViewScreen extends Screen {

  private final ParentView root;

  private StyleList styleList;

  @Nullable
  private View hovered;
  @Nullable
  private View focused;
  @Nullable
  private View dragging;

  private boolean keepOpen;

  private boolean layout;

  public ViewScreen(Component title, ParentView root) {
    super(title);
    this.minecraft = Minecraft.getInstance();
    this.width = this.minecraft.getWindow().getGuiScaledWidth();
    this.height = this.minecraft.getWindow().getGuiScaledHeight();
    this.root = root;
    this.root.setLayout(new Layout() {

      @Override
      public float getX() {
        return 0.0F;
      }

      @Override
      public float getY() {
        return 0.0F;
      }

      @Override
      public float getWidth() {
        return ViewScreen.this.width;
      }

      @Override
      public float getHeight() {
        return ViewScreen.this.height;
      }
    });
    this.root.screen = this;
  }

  public ParentView getRoot() {
    return this.root;
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
    if (this.root.isClosed()) {
      throw new IllegalStateException("Root is closed.");
    }

    this.minecraft.keyboardHandler.setSendRepeatsToGui(true);

    if (!this.root.isAdded()) {
      this.root.added();
    }

    this.layout = true;
  }

  @Override
  public void removed() {
    this.minecraft.keyboardHandler.setSendRepeatsToGui(false);

    if (this.keepOpen) {
      this.keepOpen = false;
      return;
    }
    this.root.removed();
    this.root.close();
  }

  private void updateHovered(double mouseX, double mouseY) {
    var lowestCommonAncestor = new AtomicReference<View>();
    var hovered =
        this.root.traverseSortedDepthFirst(view -> view.isMouseOver(mouseX, mouseY), view -> {
          if (view.isHovered()) {
            lowestCommonAncestor.compareAndExchange(null, view);
          }
        }).orElse(null);

    while (this.hovered != null && this.hovered != lowestCommonAncestor.getPlain()) {
      this.hovered.mouseLeft(mouseX, mouseY);
      this.hovered = this.hovered.getParent();
    }

    if (hovered != null) {
      hovered.traverseUpwardUntil(view -> {
        if (view == lowestCommonAncestor.getPlain()) {
          return true;
        }
        view.mouseEntered(mouseX, mouseY);
        return false;
      });
    }

    this.hovered = hovered;
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    this.updateHovered(mouseX, mouseY);
    if (this.hovered != null) {
      this.hovered.traverseUpward(view -> view.mouseMoved(mouseX, mouseY));
    }
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    var view = this.hovered;
    var foundFocus = false;
    do {
      if (this.minecraft.screen != this) {
        break;
      }
      if (!foundFocus && view.tryFocus(false)) {
        foundFocus = true;
        this.setFocused(view);
      }
      if (view.mousePressed(mouseX, mouseY, button) && this.dragging == null) {
        this.dragging = view;
      }
      view = view.getParent();
    } while (view != null);

    if (!foundFocus) {
      this.setFocused((View) null);
    }

    return true;
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    this.dragging = null;

    if (this.hovered == null) {
      return false;
    }

    this.hovered.traverseUpward(
        view -> view.mouseReleased(mouseX, mouseY, button));
    return true;
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button,
      double deltaX, double deltaY) {
    if (this.dragging != null) {
      this.dragging.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    return true;
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
    if (this.hovered == null) {
      return false;
    }

    this.hovered.traverseUpward(view -> view.mouseScrolled(mouseX, mouseY, scrollDelta));
    this.updateHovered(mouseX, mouseY);
    return true;
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.shouldCloseOnEsc()) {
      this.onClose();
      return true;
    }

    if (keyCode == GLFW.GLFW_KEY_TAB) {
      var forward = !hasShiftDown();
      this.root.changeFocus(forward)
          .or(() -> this.root.changeFocus(forward))
          .ifPresent(this::setFocused);
      return false;
    }

    if (this.focused == null) {
      return false;
    }

    this.focused.traverseUpward(view -> view.keyPressed(keyCode, scanCode, modifiers));
    return true;
  }

  @Override
  public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
    if (this.focused == null) {
      return false;
    }
    this.focused.traverseUpward(view -> view.keyReleased(keyCode, scanCode, modifiers));
    return true;
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    if (this.focused == null) {
      return false;
    }
    this.focused.traverseUpward(view -> view.charTyped(codePoint, modifiers));
    return true;
  }

  public View getFocusedView() {
    return this.focused;
  }

  public void setFocused(@Nullable View focused) {
    if (this.focused == focused) {
      return;
    }

    if (this.focused != null) {
      this.focused.removeFocus();
    }

    this.focused = focused;
  }

  @Override
  public void tick() {
    if (this.layout) {
      this.layout = false;
      this.root.layout();

      // Reset mouse pos
      var scaledMouseX =
          this.minecraft.mouseHandler.xpos() / this.minecraft.getWindow().getGuiScale();
      var scaledMouseY =
          this.minecraft.mouseHandler.ypos() / this.minecraft.getWindow().getGuiScale();
      this.mouseMoved(scaledMouseX, scaledMouseY);
    }
    this.root.tick();
  }


  @Override
  public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
    this.root.graphicsContext.begin();
    this.root.render(poseStack, mouseX, mouseY, partialTicks);
    this.root.graphicsContext.end();
  }

  public void setStylesheets(List<ResourceLocation> stylesheets) {
    this.styleList = StyleSheetManager.getInstance().createStyleList(stylesheets);
    this.root.refreshStyle();
  }

  public StyleList getStyleList() {
    return this.styleList;
  }
}
