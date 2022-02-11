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
    this.listView.queueChildForRemoval(serverItem, () -> {
      this.updateSelected();
      this.saveServerList();
    });
  }

  private void saveServerList() {
    ((MutableServerList) this.serverProvider)
        .save(this.listView.getChildViews().stream()
            .filter(ServerItemView.class::isInstance)
            .map(ServerItemView.class::cast)
            .map(ServerItemView::getServerEntry));
  }
}
