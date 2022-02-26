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

package com.craftingdead.immerse.client.gui.screen.menu.play;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.KeyFrames;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.screen.Theme;
import com.craftingdead.immerse.client.gui.screen.menu.AnimatableView;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.JsonServerList;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.MutableServerListView;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.NbtServerList;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.ServerListView;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.world.WorldListView;
import com.craftingdead.immerse.client.gui.view.Animation;
import com.craftingdead.immerse.client.gui.view.DropdownView;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TabsView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.sounds.ImmerseSoundEvents;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.TranslatableComponent;

public class PlayView extends ParentView implements AnimatableView {

  private final ParentView content = new ParentView(new Properties<>().id("content"));
  private final ParentView officialContent =
      new ParentView(new Properties<>().id("official-content"));

  public PlayView() {
    super(new Properties<>().backgroundBlur(50));

    var officialView = new ParentView(new Properties<>().id("official"));

    var survivalServerListView = new ServerListView(new JsonServerList(
        Paths.get(System.getProperty("user.dir"), "survival_servers.json")));

    var deathmatchServerListView = new ServerListView(new JsonServerList(
        Paths.get(System.getProperty("user.dir"), "tdm_servers.json")));

    var survivalTab = (TabsView.TabView) new TabsView.TabView()
        .setSelectedListener(() -> this.officialContent.replace(survivalServerListView))
        .setText(new TranslatableComponent("menu.play.tab.survival"));
    survivalTab.addActionSound(ImmerseSoundEvents.TAB_SELECT.get());
    survivalTab.addHoverSound(ImmerseSoundEvents.TAB_HOVER.get());

    var deathmatchTab = (TabsView.TabView) new TabsView.TabView()
        .setSelectedListener(() -> this.officialContent.replace(deathmatchServerListView))
        .setText(new TranslatableComponent("menu.play.tab.tdm"));
    deathmatchTab.addActionSound(ImmerseSoundEvents.TAB_SELECT.get());
    deathmatchTab.addHoverSound(ImmerseSoundEvents.TAB_HOVER.get());

    officialView.addChild(new TabsView(new Properties<>())
        .addTab(survivalTab)
        .addTab(deathmatchTab));
    officialView.addChild(this.officialContent);

    var singleplayerView = new WorldListView();

    var customServerListView = new MutableServerListView(new NbtServerList(
        CraftingDeadImmerse.getInstance().getModDir().resolve("custom_servers.dat")));

    this.addChild(new TextView(new Properties<>().id("title"))
        .setShadow(true)
        .setText(new TranslatableComponent("menu.play.title")));
    this.addChild(Theme.newSeparator());
    this.addChild(new DropdownView(new Properties<>())
        .setExpandSound(ImmerseSoundEvents.DROP_DOWN_EXPAND.get())
        .setItemHoverSound(ImmerseSoundEvents.TAB_HOVER.get())
        .addItem(new TranslatableComponent("menu.play.dropdown.official"),
            () -> this.dropDownSelect(officialView))
        .addItem(new TranslatableComponent("menu.play.dropdown.singleplayer"),
            () -> this.dropDownSelect(singleplayerView))
        .addItem(new TranslatableComponent("menu.play.dropdown.custom"),
            () -> this.dropDownSelect(customServerListView)));
    this.addChild(Theme.newSeparator());
    this.addChild(this.content);
  }

  private void dropDownSelect(View content) {
    this.minecraft.getSoundManager().play(
        SimpleSoundInstance.forUI(ImmerseSoundEvents.SUBMENU_SELECT.get(), 1.0F));
    this.setContent(content);
  }

  private void setContent(View content) {
    this.content.replace(content);
  }

  @Override
  protected void added() {
    super.added();
    this.minecraft.getSoundManager().play(
        SimpleSoundInstance.forUI(ImmerseSoundEvents.MAIN_MENU_PRESS_PLAY.get(), 1.0F));

    new Animator.Builder()
        .addTarget(Animation.forProperty(this.getXTranslationProperty())
            .keyFrames(new KeyFrames.Builder<>((float) -this.window.getWidth())
                .addFrame(0.0F)
                .build())
            .build())
        .setInterpolator(new SplineInterpolator(0.1F, 1.0F, 0.1F, 1.0F))
        .setDuration(250L, TimeUnit.MILLISECONDS)
        .build()
        .start();
  }

  @Override
  public void animateRemoval(Runnable remove) {
    new Animator.Builder()
        .addTarget(Animation.forProperty(this.getXTranslationProperty())
            .to((float) -this.window.getWidth())
            .build())

        .setDuration(250L, TimeUnit.MILLISECONDS)
        .addTarget(new TimingTargetAdapter() {
          @Override
          public void end(Animator source) {
            remove.run();
          }
        })
        .build()
        .start();
  }
}
