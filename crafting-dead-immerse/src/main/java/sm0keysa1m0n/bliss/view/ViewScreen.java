package sm0keysa1m0n.bliss.view;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import sm0keysa1m0n.bliss.layout.Layout;
import sm0keysa1m0n.bliss.style.StyleList;
import sm0keysa1m0n.bliss.style.StyleSheetManager;

public final class ViewScreen extends Screen {

  private final ParentView root;
  private final List<? extends GuiEventListener> children;

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
    // Don't use List::of as it does not permit null values.
    this.children = Collections.singletonList(this.root);
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
    var keepLastHovered = new AtomicBoolean();
    var hovered = this.root.hover(mouseX, mouseY, view -> {
      if (view == this.hovered) {
        keepLastHovered.setPlain(true);
      }

      if (!view.isHovered()) {
        view.mouseEntered(mouseX, mouseY);
      }
    });

    if (!keepLastHovered.getPlain()) {
      while (this.hovered != null
          && (!this.hovered.isAdded()
              || !this.hovered.isMouseOver(mouseX, mouseY)
              || (hovered != null && hovered.compareTo(this.hovered) > 0))) {
        this.hovered.mouseLeft(mouseX, mouseY);
        this.hovered = this.hovered.getParent();
      }
    }

    this.hovered = hovered;
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    this.root.mouseMoved(mouseX, mouseY);
    this.updateHovered(mouseX, mouseY);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
    var view = this.hovered;
    while (view != null) {
      if (view.mouseScrolled(mouseX, mouseY, scrollDelta)) {
        break;
      }
      view = view.getParent();
    }
    this.updateHovered(mouseX, mouseY);
    return true;
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

  @Override
  public List<? extends GuiEventListener> children() {
    return this.root.isAdded() ? this.children : Collections.emptyList();
  }

  public void setStylesheets(List<ResourceLocation> stylesheets) {
    this.styleList = StyleSheetManager.getInstance().createStyleList(stylesheets);
    this.root.refreshStyle();
  }

  public StyleList getStyleList() {
    return this.styleList;
  }
}
