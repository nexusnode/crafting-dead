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

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.KeyFrames;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.immerse.client.gui.screen.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import reactor.core.Disposable;
import reactor.core.scheduler.Schedulers;
import sm0keysa1m0n.bliss.Animation;
import sm0keysa1m0n.bliss.style.Percentage;
import sm0keysa1m0n.bliss.style.States;
import sm0keysa1m0n.bliss.view.ParentView;
import sm0keysa1m0n.bliss.view.View;

public class ServerListView<S extends ServerList> extends ParentView {

  private final S serverList;

  protected final ParentView listView;

  protected final ParentView controlsView;

  @Nullable
  private ServerItemView selectedItem;

  private final View playButton;

  private final VanillaServerStatusProvider statusProvider = new VanillaServerStatusProvider();

  @Nullable
  private Disposable refreshTask;

  public ServerListView(S serverList) {
    super(new Properties());
    this.serverList = serverList;

    this.listView = new ParentView(new Properties().styleClasses("list-content"));
    this.addChild(this.listView);

    this.playButton = Theme.createGreenButton(
        new TranslatableComponent("view.server_list.button.play"),
        () -> this.getSelectedItem().ifPresent(ServerItemView::connect));
    this.playButton.setEnabled(false);

    this.controlsView = new ParentView(new ParentView.Properties().styleClasses("list-controls"));
    this.controlsView.addChild(this.createTopRowControls());
    this.controlsView.addChild(this.createBottomRowControls());

    this.addChild(this.controlsView);

    this.refresh();
  }

  @Override
  public void tick() {
    super.tick();
    this.statusProvider.tick();
  }

  public S getServerList() {
    return this.serverList;
  }

  public ServerStatusProvider getStatusProvider() {
    return this.statusProvider;
  }

  protected ParentView createTopRowControls() {
    var view = new ParentView(new Properties());
    view.addChild(this.playButton);
    return view;
  }

  protected ParentView createBottomRowControls() {
    var view = new ParentView(new Properties());

    var quickRefreshButton = Theme.createBlueButton(
        new TranslatableComponent("view.server_list.button.quick_refresh"), this::quickRefresh);
    view.addChild(quickRefreshButton);

    var refreshButton = Theme.createBlueButton(
        new TranslatableComponent("view.server_list.button.refresh"), this::refresh);
    view.addChild(refreshButton);

    return view;
  }

  @Override
  public void added() {
    super.added();
    int delay = 0;
    for (var view : this.listView.getChildren()) {
      new Animator.Builder()
          .addTarget(Animation.forProperty(view.getStyle().xTranslation)
              .keyFrames(new KeyFrames.Builder<>(-100.0F)
                  .addFrame(0.0F)
                  .build())
              .build())
          .addTarget(Animation.forProperty(view.getStyle().opacity)
              .keyFrames(new KeyFrames.Builder<>(Percentage.ZERO)
                  .addFrame(Percentage.ONE_HUNDRED)
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
  public void removed() {
    super.removed();
    if (this.refreshTask != null) {
      this.refreshTask.dispose();
    }
  }

  public void setSelectedItem(@Nullable ServerItemView selectedItem) {
    if (this.selectedItem == selectedItem) {
      return;
    }
    if (this.selectedItem != null) {
      this.selectedItem.getStyleManager().removeState(States.CHECKED);
      this.selectedItem.getStyleManager().notifyListeners();
    }
    this.selectedItem = selectedItem;
    if (selectedItem != null) {
      this.selectedItem.getStyleManager().addState(States.CHECKED);
      this.selectedItem.getStyleManager().notifyListeners();
    }
    this.playButton.setEnabled(selectedItem != null);
  }

  public Optional<ServerItemView> getSelectedItem() {
    return Optional.ofNullable(this.selectedItem);
  }

  private void refresh() {
    if (this.refreshTask != null && !this.refreshTask.isDisposed()) {
      return;
    }
    this.listView.clearChildren();
    this.setSelectedItem(null);
    this.refreshTask = this.serverList.load()
        .publishOn(Schedulers.fromExecutor(Minecraft.getInstance()))
        .map(entry -> new ServerItemView(this, entry))
        .doOnNext(this.listView::addChild)
        .then()
        .doAfterTerminate(this.listView::layout)
        .subscribe();
  }

  private void quickRefresh() {
    for (var child : this.listView.getChildren()) {
      if (child instanceof ServerItemView serverItem) {
        serverItem.ping();
      }
    }
  }
}
