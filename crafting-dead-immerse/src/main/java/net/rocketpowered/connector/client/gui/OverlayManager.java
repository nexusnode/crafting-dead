package net.rocketpowered.connector.client.gui;

import java.util.Collections;
import java.util.List;
import com.craftingdead.immerse.client.gui.view.ViewScreen;
import com.craftingdead.immerse.client.gui.view.layout.EmptyLayout;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.LoadingGui;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.ForgeHooksClient;

public class OverlayManager extends LoadingGui implements EmptyLayout, INestedGuiEventHandler {

  public static final OverlayManager INSTANCE = new OverlayManager();

  private final Minecraft minecraft = Minecraft.getInstance();

  private final OverlayView overlayView = OverlayView.create(this);

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    if (this.minecraft.screen != null) {
      ForgeHooksClient.drawScreen(this.minecraft.screen, matrixStack, mouseX, mouseY, partialTicks);
    }
    this.overlayView.render(new MatrixStack(), mouseX, mouseY, partialTicks);
  }

  public void toggle() {
    if (this.isVisible()) {
      this.hide();
    } else {
      this.show();
    }
  }

  public void show() {
    this.minecraft.setScreen(new ViewScreen(StringTextComponent.EMPTY, GuildView::new));

    // if (!this.isVisible()) {
    // this.minecraft.setOverlay(this);
    // Tween.to(this.overlayView, View.ALPHA, 250.0F)
    // .target(1.0F)
    // .build()
    // .start(this.overlayView.getTweenManager());
    // }
  }

  public void hide() {
    // if (this.isVisible()) {
    // Tween.to(this.overlayView, View.ALPHA, 250.0F)
    // .target(0.0F)
    // .addCallback(TweenCallback.END, (type, source) -> {
    // if (this.isVisible()) {
    // this.minecraft.setOverlay(null);
    // }
    // })
    // .build()
    // .start(this.overlayView.getTweenManager());
    // }
  }

  public boolean isVisible() {
    return this.minecraft.getOverlay() == this;
  }

  @Override
  public float getWidth() {
    return this.minecraft.getWindow().getGuiScaledWidth();
  }

  @Override
  public float getHeight() {
    return this.minecraft.getWindow().getGuiScaledHeight();
  }

  @Override
  public List<? extends IGuiEventListener> children() {
    return Collections.singletonList(this.overlayView);
  }

  @Override
  public boolean isDragging() {
    return this.overlayView.isDragging();
  }

  @Override
  public void setDragging(boolean dragging) {
    this.overlayView.setDragging(dragging);
  }

  @Override
  public IGuiEventListener getFocused() {
    return this.overlayView.getFocused();
  }

  @Override
  public void setFocused(IGuiEventListener focusedListener) {
    this.overlayView.setFocused(focusedListener);
  }
}
