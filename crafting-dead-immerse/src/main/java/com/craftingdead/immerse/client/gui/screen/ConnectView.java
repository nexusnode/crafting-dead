/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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

package com.craftingdead.immerse.client.gui.screen;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.view.FogView;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.ViewScreen;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.sounds.ImmerseSoundEvents;
import com.google.common.collect.Iterators;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.Util;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.resources.ResourceLocation;

public class ConnectView extends ParentView {

  private static final Logger logger = LogUtils.getLogger();
  private static final ExecutorService executorService = Executors.newSingleThreadExecutor(
      new ThreadFactoryBuilder()
          .setNameFormat("server-connector-%s")
          .setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(logger))
          .build());

  private final Iterator<String> animation = Iterators.cycle("O o o", "o O o", "o o O");
  private long lastAnimationUpdateMs;

  private Connection connection;
  private boolean aborted;
  private final Screen lastScreen;
  private final TextView statusView;
  private final TextView animationView;

  public ConnectView(Screen lastScreen, ServerAddress address) {
    super(new Properties<>());
    this.lastScreen = lastScreen;

    this.addChild(Theme.createBackground());
    this.addChild(new FogView(new Properties<>()));

    var dialog = new ParentView(new Properties<>().id("dialog").backgroundBlur(50.0F));
    this.addChild(dialog);
    dialog.addChild(this.statusView =
        new TextView(new Properties<>().id("status"))
            .setText(new TranslatableComponent("connect.connecting"))
            .setCentered(true));
    dialog.addChild(this.animationView =
        new TextView(new Properties<>().id("animation"))
            .setText(this.nextAnimation())
            .setCentered(true));

    var cancelButton = new TextView(new Properties<>().id("cancel-button").focusable(true))
        .setText(CommonComponents.GUI_CANCEL)
        .setCentered(true);
    cancelButton.addActionSound(ImmerseSoundEvents.BUTTON_CLICK.get());
    cancelButton.addListener(ActionEvent.class, event -> {
      this.aborted = true;
      if (this.connection != null) {
        this.connection.disconnect(
            new TranslatableComponent("connect.aborted"));
      }
      this.minecraft.setScreen(this.lastScreen);
    }, true);
    dialog.addChild(cancelButton);

    this.minecraft.clearLevel();
    this.connect(address);
  }

  private void connect(ServerAddress serverAddress) {
    logger.info("Connecting to {}:{}", serverAddress.getHost(), serverAddress.getPort());
    executorService.submit(() -> {
      InetSocketAddress inetAddress = null;

      try {
        Optional<InetSocketAddress> optional =
            ServerNameResolver.DEFAULT.resolveAddress(serverAddress)
                .map(ResolvedServerAddress::asInetSocketAddress);
        if (ConnectView.this.aborted) {
          return;
        }

        if (!optional.isPresent()) {
          this.minecraft.execute(
              () -> this.minecraft.setScreen(new DisconnectedScreen(ConnectView.this.getScreen(),
                  CommonComponents.CONNECT_FAILED, ConnectScreen.UNKNOWN_HOST_MESSAGE)));
          return;
        }

        inetAddress = optional.get();

        ConnectView.this.connection = Connection.connectToServer(inetAddress,
            ConnectView.this.minecraft.options.useNativeTransport());
        ConnectView.this.connection.setListener(
            new ClientHandshakePacketListenerImpl(ConnectView.this.connection,
                ConnectView.this.minecraft, ConnectView.this.lastScreen,
                text -> this.minecraft.execute(() -> ConnectView.this.statusView.setText(text))));
        ConnectView.this.connection
            .send(new ClientIntentionPacket(inetAddress.getHostName(), inetAddress.getPort(),
                ConnectionProtocol.LOGIN));
        ConnectView.this.connection.send(
            new ServerboundHelloPacket(ConnectView.this.minecraft.getUser().getGameProfile()));
      } catch (Exception e) {
        if (ConnectView.this.aborted) {
          return;
        }

        ConnectView.logger.error("Couldn't connect to server", e);
        var censoredReason = inetAddress == null
            ? e.getMessage()
            : e.getMessage()
                .replaceAll(inetAddress.getHostName() + ":" + inetAddress.getPort(), "")
                .replaceAll(inetAddress.toString(), "");
        ConnectView.this.minecraft.execute(() -> ConnectView.this.minecraft.setScreen(
            new DisconnectedScreen(ConnectView.this.lastScreen, CommonComponents.CONNECT_FAILED,
                new TranslatableComponent("disconnect.genericReason", censoredReason))));
      }
    });
  }

  @Override
  public void tick() {
    super.tick();
    if (this.connection != null) {
      if (this.connection.isConnected()) {
        this.connection.tick();
      } else {
        this.connection.handleDisconnection();
        return;
      }
    }
    long currentTime = Util.getMillis();
    if (this.lastAnimationUpdateMs != -1L && currentTime - this.lastAnimationUpdateMs >= 100L) {
      this.lastAnimationUpdateMs = currentTime;
      this.animationView.setText(this.nextAnimation());
    }
  }

  private Component nextAnimation() {
    return new TextComponent(this.animation.next()).withStyle(ChatFormatting.GRAY);
  }

  public static Screen createScreen(Screen lastScreen, ServerAddress address) {
    var screen =
        new ViewScreen(NarratorChatListener.NO_TITLE, new ConnectView(lastScreen, address));
    screen.setStylesheets(List.of(new ResourceLocation(CraftingDeadImmerse.ID, "css/connect.css")));
    return screen;
  }
}
