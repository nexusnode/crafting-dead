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

import com.craftingdead.immerse.client.gui.screen.Theme;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.ViewScreen;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DirectJoinServerScreen;
import net.minecraft.client.gui.screens.EditServerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TranslatableComponent;

public class MutableServerListView extends ServerListView {

  private View removeButton;

  public MutableServerListView(MutableServerList serverProvider) {
    super(serverProvider);
  }

  @Override
  protected ParentView createTopRowControls() {
    var directConnectButton = Theme.createBlueButton(
        new TranslatableComponent("view.mutable_server_list.button.direct_connect"), () -> {
          ServerData tempServerData =
              new ServerData(I18n.get("selectServer.defaultName"), "", false);
          ViewScreen screen = this.getScreen();
          screen.keepOpenAndSetScreen(new DirectJoinServerScreen(screen,
              connect -> {
                if (connect) {
                  ConnectScreen.startConnecting(screen, this.minecraft,
                      ServerAddress.parseString(tempServerData.ip), tempServerData);
                } else {
                  this.minecraft.setScreen(screen);
                }
              },
              tempServerData));
        });

    var addServerButton = Theme.createGreenButton(
        new TranslatableComponent("view.mutable_server_list.button.add"), () -> {
          ServerData tempServerData =
              new ServerData(I18n.get("selectServer.defaultName"), "", false);
          ViewScreen screen = this.getScreen();
          screen.keepOpenAndSetScreen(new EditServerScreen(screen,
              success -> {
                if (success) {
                  this.addServer(tempServerData.ip);
                }
                this.minecraft.setScreen(screen);
              },
              tempServerData));
        });

    var view = super.createTopRowControls();
    view.addChild(directConnectButton);
    view.addChild(addServerButton);
    return view;
  }

  @Override
  protected ParentView createBottomRowControls() {
    this.removeButton = Theme.createRedButton(
        new TranslatableComponent("view.mutable_server_list.button.remove"),
        () -> this.getSelectedItem().ifPresent(this::removeServer));
    this.removeButton.setEnabled(false);

    var view = super.createBottomRowControls();
    view.addChild(this.removeButton);
    return view;
  }

  @Override
  protected void updateSelected() {
    super.updateSelected();
    this.removeButton.setEnabled(this.getSelectedItem().isPresent());
  }

  private void addServer(String host) {
    var address = ServerAddress.parseString(host);
    var entry = new ServerEntry(null, address.getHost(), address.getPort());
    this.addServer(entry);
    this.saveServerList();
  }

  private void removeServer(ServerItemView serverItem) {
    this.listView.removeChild(serverItem);
    this.updateSelected();
    this.saveServerList();
  }

  private void saveServerList() {
    ((MutableServerList) this.serverProvider)
        .save(this.listView.getChildViews().stream()
            .filter(ServerItemView.class::isInstance)
            .map(ServerItemView.class::cast)
            .map(ServerItemView::getServerEntry));
  }
}
