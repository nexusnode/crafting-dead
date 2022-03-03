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
    // Might have joined a world/server so we are removed
    if (this.isAdded()) {
      this.updateSelected();
    }
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
