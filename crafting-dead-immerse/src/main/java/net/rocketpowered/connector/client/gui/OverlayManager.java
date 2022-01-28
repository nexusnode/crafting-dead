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

package net.rocketpowered.connector.client.gui;

import java.util.Collections;
import java.util.List;
import com.craftingdead.immerse.client.gui.view.ViewScreen;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.LoadingGui;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.ForgeHooksClient;
import net.rocketpowered.connector.client.gui.guild.GuildView;

public class OverlayManager extends LoadingGui implements Layout, INestedGuiEventHandler {

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
