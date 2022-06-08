/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.screen.menu.play.list.server;

import java.util.Iterator;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.immerse.client.gui.screen.ConnectView;
import com.craftingdead.immerse.client.util.ServerPinger;
import com.google.common.collect.Iterators;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import sm0keysa1m0n.bliss.view.ParentView;
import sm0keysa1m0n.bliss.view.TextView;
import sm0keysa1m0n.bliss.view.event.ActionEvent;

class ServerItemView extends ParentView {

  private final Iterator<String> animation = Iterators.cycle("O o o", "o O o", "o o O");

  private final ServerListView list;

  private final ServerEntry serverEntry;

  private final TextView motdComponent;
  private final TextView pingComponent;
  private final TextView playersAmountComponent;

  private long lastAnimationUpdateMs;

  ServerItemView(ServerListView list, ServerEntry serverEntry) {
    super(new Properties().styleClasses("item").doubleClick(true).focusable(true));

    this.list = list;
    this.serverEntry = serverEntry;

    this.motdComponent = new TextView(new Properties().id("motd"))
        .setText("...")
        .setWrap(false);

    this.pingComponent = new TextView(new Properties().id("ping"))
        .setText("...");
    this.playersAmountComponent = new TextView(new Properties().id("players"))
        .setText("...");

    this.addListener(ActionEvent.class, event -> this.connect());
    this.addChild(this.motdComponent);
    this.addChild(new TextView(new Properties().id("map"))
        .setText(new TextComponent(this.serverEntry.getMap().orElse("-"))
            .withStyle(ChatFormatting.GRAY))
        .setWrap(false));
    this.addChild(this.pingComponent);
    this.addChild(this.playersAmountComponent);

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

  @Override
  public void keyPressed(int keyCode, int scanCode, int modifiers) {
    super.keyPressed(keyCode, scanCode, modifiers);
    if (keyCode == GLFW.GLFW_KEY_SPACE && this.isFocused()) {
      this.list.setSelectedItem(this);
    }
  }

  @Override
  public boolean mousePressed(double mouseX, double mouseY, int button) {
    if (this.isFocused()) {
      this.list.setSelectedItem(this);
    }
    return super.mousePressed(mouseX, mouseY, button);
  }

  @SuppressWarnings("removal")
  public void connect() {
    // Call this before creating a ConnectView instance.
    this.getScreen().keepOpen();
    this.minecraft.setScreen(
        ConnectView.createScreen(this.getScreen(), this.serverEntry.toServerAddress()));
  }

  public ServerEntry getServerEntry() {
    return this.serverEntry;
  }

  @SuppressWarnings("removal")
  public void ping() {
    this.motdComponent.setText("...");
    this.pingComponent.setText("...");
    this.playersAmountComponent.setText("...");
    this.lastAnimationUpdateMs = 0;

    ServerPinger.INSTANCE.ping(this.serverEntry.toServerAddress(),
        (pingData) -> this.minecraft.execute(() -> this.updateServerInfo(pingData)));
  }

  private void updateServerInfo(ServerPinger.PingData pingData) {
    this.motdComponent.setText(pingData.getMotd());
    if (pingData.getPing() >= 0) {
      var ping = pingData.getPing();
      ChatFormatting pingColor;
      if (ping < 200) {
        pingColor = ChatFormatting.GREEN;
      } else if (ping < 400) {
        pingColor = ChatFormatting.YELLOW;
      } else if (ping < 1200) {
        pingColor = ChatFormatting.RED;
      } else {
        pingColor = ChatFormatting.DARK_RED;
      }
      this.pingComponent.setText(new TextComponent(ping + "ms").withStyle(pingColor));
    } else {
      this.pingComponent.setText(new TextComponent("?"));
    }
    this.playersAmountComponent.setText(pingData.getPlayersAmount());
    this.lastAnimationUpdateMs = -1L;
  }
}
