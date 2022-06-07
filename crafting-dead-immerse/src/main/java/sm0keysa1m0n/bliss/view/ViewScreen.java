package sm0keysa1m0n.bliss.view;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
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

  private View hovered;

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

    if (!this.root.isAdded()) {
      this.root.added();
    }

    this.layout = true;
  }

  @Override
  public void removed() {
    if (this.keepOpen) {
      this.keepOpen = false;
      return;
    }
    this.root.removed();
    this.root.close();
  }

  private void updateHovered(double mouseX, double mouseY) {
    var lowestCommonAncestor = new AtomicReference<View>();
    var hovered = this.root.traverseDepthFirst(view -> view.isMouseOver(mouseX, mouseY), view -> {
      if (view.isHovered()) {
        lowestCommonAncestor.compareAndExchange(null, view);
      }
    }).orElse(null);

    while (this.hovered != null && this.hovered != lowestCommonAncestor.getPlain()) {
      this.hovered.mouseLeft(mouseX, mouseY);
      this.hovered = this.hovered.getParent();
    }

    if (hovered != null) {
      hovered.traverseUpward(view -> {
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
    this.root.mouseMoved(mouseX, mouseY);
    this.updateHovered(mouseX, mouseY);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    return this.root.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    return this.root.mouseReleased(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button,
      double deltaX, double deltaY) {
    return this.root.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
    if (this.hovered != null) {
      this.hovered.traverseUpward(view -> view.mouseScrolled(mouseX, mouseY, scrollDelta));
      this.updateHovered(mouseX, mouseY);
    }
    return true;
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    return this.root.keyReleased(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
    return this.root.keyReleased(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    return this.root.charTyped(codePoint, modifiers);
  }

  @Override
  public boolean changeFocus(boolean forward) {
    return this.root.changeFocus(forward);
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
    this.root.render(poseStack, mouseX, mouseY, partialTicks);
  }

  public void setStylesheets(List<ResourceLocation> stylesheets) {
    this.styleList = StyleSheetManager.getInstance().createStyleList(stylesheets);
    this.root.refreshStyle();
  }

  public StyleList getStyleList() {
    return this.styleList;
  }
}
