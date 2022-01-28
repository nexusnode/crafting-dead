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

import java.util.Iterator;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.immerse.client.gui.screen.ConnectView;
import com.craftingdead.immerse.client.gui.view.Color;
import com.craftingdead.immerse.client.gui.view.Overflow;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.States;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.ViewScreen;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Align;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import com.craftingdead.immerse.client.util.ServerPinger;
import com.google.common.collect.Iterators;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;

class ServerItemView extends ParentView<ServerItemView, YogaLayout, YogaLayout> {

  private final Iterator<String> animation = Iterators.cycle("O o o", "o O o", "o o O");

  private final ServerEntry serverEntry;

  private final TextView<YogaLayout> motdComponent;
  private final TextView<YogaLayout> pingComponent;
  private final TextView<YogaLayout> playersAmountComponent;

  private long lastAnimationUpdateMs;

  ServerItemView(ServerEntry serverEntry) {
    super(
        new YogaLayout()
            .setTopMargin(3F)
            .setLeftMargin(7F)
            .setRightMargin(7F)
            .setLeftPadding(10F)
            .setRightPadding(20F)
            .setHeight(22F)
            .setMaxWidth(520F),
        new YogaLayoutParent()
            .setFlexDirection(FlexDirection.ROW)
            .setAlignItems(Align.CENTER));
    this.serverEntry = serverEntry;

    this.getOutlineWidthProperty()
        .registerState(1.0F, States.SELECTED)
        .registerState(1.0F, States.HOVERED);
    this.getOutlineColorProperty()
        .registerState(Color.WHITE, States.SELECTED)
        .registerState(Color.GRAY, States.HOVERED);

    this.motdComponent = new TextView<>(new YogaLayout()
        .setOverflow(Overflow.HIDDEN)
        .setFlex(2)
        .setWidthPercent(100)
        .setHeight(8), "...")
            .setShadow(false)
            .setCentered(true);

    this.pingComponent = new TextView<>(new YogaLayout()
        .setWidth(60F)
        .setLeftMargin(10F)
        .setHeight(8), "...")
            .setShadow(false)
            .setCentered(true);
    this.playersAmountComponent = new TextView<>(new YogaLayout()
        .setWidth(60F)
        .setLeftMargin(10F)
        .setHeight(8), "...")
            .setShadow(false)
            .setCentered(true);

    this.getBackgroundColorProperty().setBaseValue(new Color(0X882C2C2C));

    this
        .setFocusable(true)
        .setDoubleClick(true)
        .addListener(ActionEvent.class, (c, e) -> this.connect())
        .addChild(this.motdComponent)
        .addChild(new TextView<>(
            new YogaLayout().setOverflow(Overflow.HIDDEN).setFlex(1).setHeight(8),
            new TextComponent(this.serverEntry.getMap().orElse("-"))
                .withStyle(ChatFormatting.GRAY))
                    .setShadow(false)
                    .setCentered(true))
        .addChild(this.pingComponent)
        .addChild(this.playersAmountComponent);

    this.ping();
  }

  @Override
  public void tick() {
    super.tick();
    long currentTime = Util.getMillis();
    if (this.lastAnimationUpdateMs != -1L && currentTime - this.lastAnimationUpdateMs >= 100L) {
      this.lastAnimationUpdateMs = currentTime;
      this.motdComponent.setText(new TextComponent(this.animation.next()));
    }
  }

  public void connect() {
    // Call this before creating a ConnectingScreen instance.
    ((ViewScreen) this.getScreen()).keepOpen();
    this.minecraft.setScreen(ConnectView.createScreen(
        ((ViewScreen) this.getScreen()), this.serverEntry.toServerAddress()));
  }

  public ServerEntry getServerEntry() {
    return this.serverEntry;
  }

  public void ping() {
    this.motdComponent.setText(new TextComponent("..."));
    this.pingComponent.setText(new TextComponent("..."));
    this.playersAmountComponent.setText(new TextComponent("..."));
    this.lastAnimationUpdateMs = 0;

    ServerPinger.INSTANCE.ping(this.serverEntry.toServerAddress(),
        (pingData) -> this.minecraft.execute(() -> this.updateServerInfo(pingData)));
  }

  private void updateServerInfo(ServerPinger.PingData pingData) {
    this.motdComponent.setText(pingData.getMotd());
    if (pingData.getPing() >= 0) {
      long ping = pingData.getPing();
      String pingText = ping + "ms";
      if (ping < 200) {
        pingText = ChatFormatting.GREEN + pingText;
      } else if (ping < 400) {
        pingText = ChatFormatting.YELLOW + pingText;
      } else if (ping < 1200) {
        pingText = ChatFormatting.RED + pingText;
      } else {
        pingText = ChatFormatting.DARK_RED + pingText;
      }
      this.pingComponent.setText(new TextComponent(pingText));
    } else {
      this.pingComponent.setText(new TextComponent("?"));
    }
    this.playersAmountComponent.setText(pingData.getPlayersAmount());
    this.lastAnimationUpdateMs = -1L;
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (keyCode == GLFW.GLFW_KEY_SPACE && this.hasState(States.FOCUSED)) {
      this.toggleState(States.SELECTED);
      this.updateProperties(false);
      return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    boolean consume = super.mouseClicked(mouseX, mouseY, button);
    this.setSelected(this.isHovered());
    return consume;
  }
}
