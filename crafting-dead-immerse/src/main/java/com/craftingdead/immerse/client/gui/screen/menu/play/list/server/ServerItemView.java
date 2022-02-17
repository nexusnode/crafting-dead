/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.screen.menu.play.list.server;

import java.util.Iterator;
import com.craftingdead.immerse.client.gui.screen.ConnectView;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.client.util.ServerPinger;
import com.google.common.collect.Iterators;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;

class ServerItemView extends ParentView {

  private final Iterator<String> animation = Iterators.cycle("O o o", "o O o", "o o O");

  private final ServerEntry serverEntry;

  private final TextView motdComponent;
  private final TextView pingComponent;
  private final TextView playersAmountComponent;

  private long lastAnimationUpdateMs;

  ServerItemView(ServerEntry serverEntry) {
    super(new Properties<>().styleClasses("item").doubleClick(true));
    this.serverEntry = serverEntry;

    this.motdComponent = new TextView(new Properties<>().id("motd"))
        .setText("...")
        .setShadow(false)
        .setCentered(true)
        .setWrap(false);

    this.pingComponent = new TextView(new Properties<>().id("ping"))
        .setText("...")
        .setShadow(false)
        .setCentered(true);
    this.playersAmountComponent = new TextView(new Properties<>().id("players"))
        .setText("...")
        .setShadow(false)
        .setCentered(true);

    this.addListener(ActionEvent.class, event -> this.connect());
    this.addChild(this.motdComponent);
    this.addChild(new TextView(new Properties<>().id("map"))
        .setText(new TextComponent(this.serverEntry.getMap().orElse("-"))
            .withStyle(ChatFormatting.GRAY))
        .setShadow(false)
        .setCentered(true));
    this.addChild(this.pingComponent);
    this.addChild(this.playersAmountComponent);

    this.ping();
  }

  @Override
  protected boolean isFocusable() {
    return true;
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
    // Call this before creating a ConnectView instance.
    this.getScreen().keepOpen();
    this.minecraft.setScreen(
        ConnectView.createScreen(this.getScreen(), this.serverEntry.toServerAddress()));
  }

  public ServerEntry getServerEntry() {
    return this.serverEntry;
  }

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
}
