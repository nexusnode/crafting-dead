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

package com.craftingdead.immerse.client.gui.screen.menu.play.list.server;

import org.lwjgl.glfw.GLFW;
import com.craftingdead.immerse.client.gui.view.Colour;
import com.craftingdead.immerse.client.gui.view.Overflow;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Align;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import com.craftingdead.immerse.client.util.ServerPinger;
import net.minecraft.client.gui.screen.ConnectingScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class ServerItemView
    extends ParentView<ServerItemView, YogaLayout, YogaLayout> {

  private static final ITextComponent SERVER_LIST_FAILED =
      new TranslationTextComponent("menu.play.server_list.failed_to_load");

  private final ServerEntry serverEntry;

  private final TextView<YogaLayout> motdComponent;
  private final TextView<YogaLayout> pingComponent;
  private final TextView<YogaLayout> playersAmountComponent;

  private boolean selected = false;

  public ServerItemView(ServerEntry serverEntry) {
    super(new YogaLayout()
        .setTopMargin(3F)
        .setLeftMargin(7F)
        .setRightMargin(7F)
        .setLeftPadding(10F)
        .setRightPadding(20F)
        .setHeight(22F)
        .setMaxWidth(520F),
        new YogaLayoutParent().setFlexDirection(FlexDirection.ROW)
            .setAlignItems(Align.CENTER));
    this.serverEntry = serverEntry;

    // TODO replace the loading with animation?
    this.motdComponent = new TextView<>(new YogaLayout()
        .setFlex(2)
        .setHeight(8), "???")
            .setOverflow(Overflow.HIDDEN)
            .setShadow(false)
            .setCentered(true);
    this.pingComponent =
        new TextView<>(new YogaLayout()
            .setWidth(60F)
            .setLeftMargin(10F)
            .setHeight(8), "???")
                .setShadow(false)
                .setCentered(true);
    this.playersAmountComponent = new TextView<>(new YogaLayout()
        .setWidth(60F)
        .setLeftMargin(10F)
        .setHeight(8), "???")
            .setShadow(false)
            .setCentered(true);

    this
        .setBackgroundColour(new Colour(0X882C2C2C))
        .setFocusable(true)
        .setDoubleClick(true)
        .addListener(ActionEvent.class, (c, e) -> this.connect())
        .addChild(this.motdComponent)
        .addChild(new TextView<>(new YogaLayout().setFlex(1).setHeight(8),
            new StringTextComponent(this.serverEntry.getMap().orElse("-"))
                .withStyle(TextFormatting.GRAY))
                    .setOverflow(Overflow.HIDDEN)
                    .setShadow(false)
                    .setCentered(true))
        .addChild(this.pingComponent)
        .addChild(this.playersAmountComponent);

    this.ping();
  }

  public void connect() {
    this.getScreen().keepOpenAndSetScreen(
        new ConnectingScreen(this.getScreen(), this.minecraft, this.serverEntry.getHostName(),
            this.serverEntry.getPort()));
  }

  public void ping() {
    ServerPinger.INSTANCE.ping(this.serverEntry.getHostName(), this.serverEntry.getPort(),
        (pingData) -> this.minecraft.execute(() -> this.updateServerInfo(pingData)));
  }

  private void updateServerInfo(ServerPinger.PingData pingData) {
    this.motdComponent.setText(pingData.getMotd());
    if (pingData.getPing() >= 0) {
      long ping = pingData.getPing();
      String pingText = ping + "ms";
      if (ping < 200) {
        pingText = TextFormatting.GREEN + pingText;
      } else if (ping < 400) {
        pingText = TextFormatting.YELLOW + pingText;
      } else if (ping < 1200) {
        pingText = TextFormatting.RED + pingText;
      } else {
        pingText = TextFormatting.DARK_RED + pingText;
      }
      this.pingComponent.setText(new StringTextComponent(pingText));
    } else {
      this.pingComponent.setText(SERVER_LIST_FAILED);
    }
    this.playersAmountComponent.setText(pingData.getPlayersAmount());
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (keyCode == GLFW.GLFW_KEY_SPACE && this.focused) {
      this.selected = !this.selected;
      this.updateBorder();
      return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  protected void focusChanged() {
    this.updateBorder();
  }

  @Override
  public void mouseEntered(double mouseX, double mouseY) {
    super.mouseEntered(mouseX, mouseY);
    this.updateBorder();
  }

  @Override
  public void mouseLeft(double mouseX, double mouseY) {
    super.mouseLeft(mouseX, mouseY);
    this.updateBorder();
  }

  private void updateBorder() {
    if (this.selected) {
      this.setBorderWidth(1.5F);
    } else if (this.isHovered() || this.isFocused()) {
      this.setBorderWidth(0.7F);
    } else {
      this.setBorderWidth(0F);
    }
  }

  public boolean isSelected() {
    return this.selected;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (super.mouseClicked(mouseX, mouseY, button)) {
      return true;
    }

    if (this.isHovered()) {
      this.selected = true;
      this.updateBorder();
      return true;
    } else {
      this.selected = false;
      this.updateBorder();
    }
    return false;
  }
}
