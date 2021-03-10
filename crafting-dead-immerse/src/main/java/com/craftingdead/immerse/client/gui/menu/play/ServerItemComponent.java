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

package com.craftingdead.immerse.client.gui.menu.play;

import org.lwjgl.glfw.GLFW;
import com.craftingdead.core.util.Text;
import com.craftingdead.immerse.client.gui.component.Colour;
import com.craftingdead.immerse.client.gui.component.ParentComponent;
import com.craftingdead.immerse.client.gui.component.TextBlockComponent;
import com.craftingdead.immerse.client.gui.component.event.ActionEvent;
import com.craftingdead.immerse.client.gui.component.serverentry.ServerEntry;
import com.craftingdead.immerse.client.gui.component.type.Align;
import com.craftingdead.immerse.client.gui.component.type.FlexDirection;
import com.craftingdead.immerse.client.gui.component.type.Overflow;
import com.craftingdead.immerse.network.ServerPinger;
import net.minecraft.client.gui.screen.ConnectingScreen;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class ServerItemComponent extends ParentComponent<ServerItemComponent> {

  private final ServerEntry serverEntry;

  private final TextBlockComponent motdComponent;
  private final TextBlockComponent pingComponent;
  private final TextBlockComponent playersAmountComponent;

  private boolean selected = false;

  public ServerItemComponent(ServerEntry serverEntry) {
    this.serverEntry = serverEntry;

    // TODO replace the loading with animation?
    this.motdComponent = new TextBlockComponent("???");
    this.pingComponent = new TextBlockComponent("???");
    this.playersAmountComponent = new TextBlockComponent("???");

    this.setTopMargin(3F)
        .setLeftMargin(7F)
        .setRightMargin(7F)
        .setLeftPadding(10F)
        .setRightPadding(20F)
        .setHeight(22F)
        .setBackgroundColour(new Colour(0X882C2C2C))
        .setMaxWidth(520F)
        .setFlexDirection(FlexDirection.ROW)
        .setAlignItems(Align.CENTER)
        .setFocusable(true)
        .setDoubleClick(true)
        .addListener(ActionEvent.class, (c, e) -> this.connect())
        .addChild(this.motdComponent
            .setOverflow(Overflow.HIDDEN)
            .setFlex(2)
            .setShadow(false)
            .setCentered(true)
            .setHeight(8))
        .addChild(new TextBlockComponent(
            Text.of(this.serverEntry.getMap().orElse("-")).withStyle(TextFormatting.GRAY))
                .setOverflow(Overflow.HIDDEN)
                .setFlex(1)
                .setShadow(false)
                .setCentered(true)
                .setHeight(8))
        .addChild(this.pingComponent
            .setWidth(60F)
            .setShadow(false)
            .setCentered(true)
            .setLeftMargin(10F)
            .setHeight(8))
        .addChild(this.playersAmountComponent
            .setWidth(60F)
            .setShadow(false)
            .setCentered(true)
            .setLeftMargin(10F)
            .setHeight(8));

    this.ping();
  }

  public void connect() {
    this.minecraft.setScreen(
        new ConnectingScreen(this.getScreen(), this.minecraft, this.serverEntry.getHostName(),
            this.serverEntry.getPort()));
  }

  public void ping() {
    ServerPinger.INSTANCE.ping(this.serverEntry.getHostName(), this.serverEntry.getPort(),
        (pingData) -> this.minecraft.execute(() -> this.updateServerInfo(pingData)));
  }

  private void updateServerInfo(ServerPinger.PingData pingData) {
    this.motdComponent.changeText(pingData.getMotd());
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
      this.pingComponent.changeText(Text.of(pingText));
    } else {
      this.pingComponent
          .changeText(new TranslationTextComponent("menu.play.server_list.failed_to_load"));
    }
    this.playersAmountComponent.changeText(pingData.getPlayersAmount());
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
  protected void mouseEntered(double mouseX, double mouseY) {
    super.mouseEntered(mouseX, mouseY);
    this.updateBorder();
  }

  @Override
  protected void mouseLeft(double mouseX, double mouseY) {
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
