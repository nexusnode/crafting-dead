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
import com.craftingdead.immerse.client.gui.view.States;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.ViewScreen;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.ConnectingScreen;
import net.minecraft.client.gui.screen.ServerListScreen;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;

public class MutableServerListView<L extends Layout> extends ServerListView<L> {

  private View<?, YogaLayout> removeButton;

  public MutableServerListView(L layout, MutableServerList serverProvider) {
    super(layout, serverProvider);
  }

  @Override
  protected ParentView<?, YogaLayout, YogaLayout> createTopRowControls() {
    return super.createTopRowControls()
        .configure(view -> view.getLayout().setWidth(300))
        .addChild(createButton(Theme.BLUE, Theme.BLUE_HIGHLIGHTED,
            new TranslationTextComponent("view.mutable_server_list.button.direct_connect"), () -> {
              ServerData tempServerData =
                  new ServerData(I18n.get("selectServer.defaultName"), "", false);
              ViewScreen screen = ((ViewScreen) this.getScreen());
              screen.keepOpenAndSetScreen(new ServerListScreen(screen,
                  connect -> this.minecraft.setScreen(connect
                      ? new ConnectingScreen(screen, this.minecraft, tempServerData)
                      : screen),
                  tempServerData));
            }).configure(view -> view.getLayout().setMargin(3)))
        .addChild(createButton(Theme.GREEN, Theme.GREEN_HIGHLIGHTED,
            new TranslationTextComponent("view.mutable_server_list.button.add"), () -> {
              ServerData tempServerData =
                  new ServerData(I18n.get("selectServer.defaultName"), "", false);
              ViewScreen screen = ((ViewScreen) this.getScreen());
              screen.keepOpenAndSetScreen(new AddServerScreen(screen,
                  success -> {
                    if (success) {
                      this.addServer(tempServerData.ip);
                    }
                    this.minecraft.setScreen(screen);
                  },
                  tempServerData));
            }).configure(view -> view.getLayout().setMargin(3)));
  }

  @Override
  protected ParentView<?, YogaLayout, YogaLayout> createBottomRowControls() {
    this.removeButton = createButton(Theme.RED, Theme.RED_HIGHLIGHTED,
        new TranslationTextComponent("view.mutable_server_list.button.remove"),
        () -> this.getSelectedItem().ifPresent(this::removeServer))
            .configure(view -> view.getBackgroundColorProperty()
                .registerState(Theme.RED_DISABLED, States.DISABLED))
            .setEnabled(false)
            .configure(view -> view.getLayout().setMargin(3));
    return super.createBottomRowControls()
        .configure(view -> view.getLayout().setWidth(300))
        .addChild(this.removeButton);
  }

  @Override
  protected void updateSelected() {
    super.updateSelected();
    this.removeButton.setEnabled(this.getSelectedItem().isPresent());
  }

  private void addServer(String host) {
    ServerAddress address = ServerAddress.parseString(host);
    ServerEntry entry = new ServerEntry(null, address.getHost(), address.getPort());
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
