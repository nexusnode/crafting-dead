/*
 * Crafting Dead
 * Copyright (C)  2021  Nexus Node
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

import com.craftingdead.core.util.Text;
import com.craftingdead.immerse.client.gui.component.Colour;
import com.craftingdead.immerse.client.gui.component.ContainerComponent;
import com.craftingdead.immerse.client.gui.component.TextBlockComponent;
import com.craftingdead.immerse.client.gui.component.type.Align;
import com.craftingdead.immerse.client.gui.component.type.FlexDirection;
import com.craftingdead.immerse.network.ServerPinger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConnectingScreen;
import net.minecraft.util.text.TranslationTextComponent;

public class ServerItemComponent extends ContainerComponent {

  private final TextBlockComponent pingComponent;
  private final TextBlockComponent playersAmountComponent;
  private final ServerListComponent parent;
  private final String hostName;
  private final int port;

  private boolean hovered = false;
  private boolean selected = false;
  private int doubleClickTicks = 0;

  public ServerItemComponent(ServerListComponent parent, String serverName, String hostName, int port) {
    this.parent = parent;
    this.hostName = hostName;
    this.port = port;
    //TODO replace the loading with animation?
    pingComponent = new TextBlockComponent("loading");
    playersAmountComponent = new TextBlockComponent("loading");
    this.setTopMargin(3F)
        .setLeftMargin(7F)
        .setRightMargin(7F)
        .setLeftPadding(10F)
        .setRightPadding(20F)
        .setHeight(22F)
        .setBackgroundColour(new Colour(0x44393939))
        .setMaxWidth(520F)
        .setFlexDirection(FlexDirection.ROW)
        .setAlignItems(Align.CENTER)
        .addChild(new TextBlockComponent(serverName)
            .setFlexGrow(1F)
            .setShadow(false)
            .setCentered(true)
            .setHeight(8))
        .addChild(pingComponent
            .setWidth(60F)
            .setShadow(false)
            .setCentered(true)
            .setLeftMargin(10F)
            .setHeight(8))
        .addChild(playersAmountComponent
            .setWidth(60F)
            .setShadow(false)
            .setCentered(true)
            .setLeftMargin(10F)
            .setHeight(8));

    this.pingServer();
  }

  public void refreshPing() {
    this.pingComponent.changeText(Text.of("loading"));
    this.playersAmountComponent.changeText(Text.of("loading"));
    this.pingServer();
  }

  public void connect() {
    Minecraft.getInstance().displayGuiScreen(new ConnectingScreen(this.getScreen(), this.minecraft, this.hostName,
        this.port));
  }

  private void pingServer() {
    this.parent.getExecutor().execute(() -> {
      try {
        this.parent.getServerPinger().ping(hostName, (pingData) ->
            this.minecraft.execute(() -> this.updateServerInfo(pingData)));
      } catch (Exception exception) {
        this.minecraft.execute(() -> this.updateServerInfo(ServerPinger.PingData.FAILED));
      }
    });
  }

  private void updateServerInfo(ServerPinger.PingData pingData) {
    if (pingData.getPing() >= 0) {
      pingComponent.changeText(Text.of(pingData.getPing()));
    } else {
      pingComponent.changeText(new TranslationTextComponent("menu.play.server_list.failed_to_load"));
    }
    playersAmountComponent.changeText(pingData.getPlayersAmount());
  }

  @Override
  protected void mouseEntered(double mouseX, double mouseY) {
    super.mouseEntered(mouseX, mouseY);
    this.hovered = true;
    this.updateBorder();
  }

  @Override
  protected void mouseLeft(double mouseX, double mouseY) {
    super.mouseLeft(mouseX, mouseY);
    this.hovered = false;
    this.updateBorder();
  }

  private void updateBorder() {
    if (selected) {
      this.setBorderWidth(1.5F);
    } else if (hovered) {
      this.setBorderWidth(0.7F);
    } else {
      this.setBorderWidth(0F);
    }
  }

  public void removeSelect() {
    this.selected = false;
    this.updateBorder();
  }
  public boolean isSelected() {
    return selected;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    this.selected = true;
    this.updateBorder();
    if (doubleClickTicks > 0) {
      this.connect();
    } else {
      this.doubleClickTicks = 4;
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public void tick() {
    super.tick();
    if (doubleClickTicks > 0) {
      doubleClickTicks--;
    }
  }

}
