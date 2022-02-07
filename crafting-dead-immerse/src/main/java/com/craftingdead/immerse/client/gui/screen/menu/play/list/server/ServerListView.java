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

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.KeyFrames;
import com.craftingdead.immerse.client.gui.screen.Theme;
import com.craftingdead.immerse.client.gui.view.Animation;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.View;
import net.minecraft.network.chat.TranslatableComponent;

public class ServerListView extends ParentView {

  protected final ServerList serverProvider;

  protected final ParentView listView;

  protected final ParentView controlsView;

  @Nullable
  private ServerItemView selectedItem;

  private final View playButton;

  @Nullable
  private CompletableFuture<Void> refreshFuture;

  public ServerListView(ServerList serverEntryProvider) {
    super(new Properties<>());
    this.serverProvider = serverEntryProvider;

    this.listView = new ParentView(new Properties<>().id("content"));
    this.addChild(this.listView);

    this.playButton = Theme.createGreenButton(
        new TranslatableComponent("view.server_list.button.play"),
        () -> this.getSelectedItem().ifPresent(ServerItemView::connect));
    this.playButton.setEnabled(false);

    this.controlsView = new ParentView(new ParentView.Properties<>().id("controls"));
    this.controlsView.addChild(this.createTopRowControls());
    this.controlsView.addChild(this.createBottomRowControls());

    this.addChild(this.controlsView);

    this.refresh();
  }

  protected ParentView createTopRowControls() {
    var view = new ParentView(new ParentView.Properties<>());
    view.addChild(this.playButton);
    return view;
  }

  protected ParentView createBottomRowControls() {
    var view = new ParentView(new ParentView.Properties<>());

    var quickRefreshButton = Theme.createBlueButton(
        new TranslatableComponent("view.server_list.button.quick_refresh"), this::quickRefresh);
    view.addChild(quickRefreshButton);

    var refreshButton = Theme.createBlueButton(
        new TranslatableComponent("view.server_list.button.refresh"), this::refresh);
    view.addChild(refreshButton);

    return view;
  }

  @Override
  protected void added() {
    super.added();
    int delay = 0;
    for (var view : this.listView.getChildViews()) {
      new Animator.Builder()
          .addTarget(Animation.forProperty(view.getXTranslationProperty())
              .keyFrames(new KeyFrames.Builder<>(-100.0F)
                  .addFrame(0.0F)
                  .build())
              .build())
          .addTarget(Animation.forProperty(view.getAlphaProperty())
              .keyFrames(new KeyFrames.Builder<>(0.0F)
                  .addFrame(1.0F)
                  .build())
              .build())
          .setStartDelay(delay, TimeUnit.MILLISECONDS)
          .setDuration(200L, TimeUnit.MILLISECONDS)
          .build()
          .start();
      delay += 100;
    }
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    var result = super.mouseClicked(mouseX, mouseY, button);
    this.updateSelected();
    return result;
  }

  protected void updateSelected() {
    this.selectedItem = this.listView.getChildViews().stream()
        .filter(child -> child instanceof ServerItemView)
        .map(child -> (ServerItemView) child)
        .filter(View::isFocused)
        .findAny()
        .orElse(null);

    this.playButton.setEnabled(this.selectedItem != null);
  }

  public Optional<ServerItemView> getSelectedItem() {
    return Optional.ofNullable(this.selectedItem);
  }

  private void refresh() {
    if (this.refreshFuture == null || this.refreshFuture.isDone()) {
      this.listView.clearChildren();
      this.selectedItem = null;
      this.updateSelected();
      this.refreshFuture = this.serverProvider.load()
          .thenAcceptAsync(servers -> servers.forEach(this::addServer), this.minecraft);
    }
  }

  private void quickRefresh() {
    for (var child : this.listView.getChildViews()) {
      if (child instanceof ServerItemView serverItem) {
        serverItem.ping();
      }
    }
  }

  protected void addServer(ServerEntry entry) {
    this.listView.addChild(new ServerItemView(entry));
    this.listView.layout();
  }
}
