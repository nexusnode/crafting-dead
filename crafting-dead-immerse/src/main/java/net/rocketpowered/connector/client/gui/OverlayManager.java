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

import java.util.List;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.view.ViewScreen;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.rocketpowered.connector.client.gui.guild.GuildView;

public class OverlayManager extends Overlay implements Layout, ContainerEventHandler {

  public static final OverlayManager INSTANCE = new OverlayManager();

  private final Minecraft minecraft = Minecraft.getInstance();

  private final OverlayView overlayView = new OverlayView();

  @Override
  public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
    if (this.minecraft.screen != null) {
      ForgeHooksClient.drawScreen(this.minecraft.screen, PoseStack, mouseX, mouseY, partialTicks);
    }
    this.overlayView.render(new PoseStack(), mouseX, mouseY, partialTicks);
  }

  public void toggle() {
    if (this.isVisible()) {
      this.hide();
    } else {
      this.show();
    }
  }

  public void show() {
    var screen = new ViewScreen(TextComponent.EMPTY, new GuildView());
    screen.setStylesheets(List.of(new ResourceLocation(CraftingDeadImmerse.ID, "css/guild.css")));
    this.minecraft.setScreen(screen);

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
  public List<? extends GuiEventListener> children() {
    return List.of(this.overlayView);
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
  public GuiEventListener getFocused() {
    return this.overlayView.getFocused();
  }

  @Override
  public void setFocused(GuiEventListener focusedListener) {
    this.overlayView.setFocused(focusedListener);
  }
}
