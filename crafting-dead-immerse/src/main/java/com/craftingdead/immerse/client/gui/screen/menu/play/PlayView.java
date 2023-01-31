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

package com.craftingdead.immerse.client.gui.screen.menu.play;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.KeyFrames;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.GuiUtil;
import com.craftingdead.immerse.client.gui.screen.Theme;
import com.craftingdead.immerse.client.gui.screen.menu.TransitionView;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.JsonServerList;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.MutableServerListView;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.NbtServerList;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.ServerListView;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.world.WorldListView;
import com.craftingdead.immerse.sounds.ImmerseSoundEvents;
import net.minecraft.network.chat.TranslatableComponent;
import sm0keysa1m0n.bliss.Animation;
import sm0keysa1m0n.bliss.minecraft.AdapterUtil;
import sm0keysa1m0n.bliss.view.DropdownView;
import sm0keysa1m0n.bliss.view.ParentView;
import sm0keysa1m0n.bliss.view.TabsView;
import sm0keysa1m0n.bliss.view.TextView;
import sm0keysa1m0n.bliss.view.View;
import sm0keysa1m0n.bliss.view.event.ActionEvent;
import sm0keysa1m0n.bliss.view.event.MouseEnterEvent;

public class PlayView extends ParentView implements TransitionView {

  private final ParentView content = new ParentView(new Properties().id("play-content"));
  private final ParentView officialContent =
      new ParentView(new Properties().id("official-content"));

  private final Animator entranceAnimator;
  private final Animator exitAnimator;
  private Runnable removeCallback;

  public PlayView() {
    super(new Properties().styleClasses("blur"));

    var officialView = new ParentView(new Properties().id("official"));

    var survivalServerListView = new ServerListView<>(new JsonServerList(
        Paths.get(System.getProperty("user.dir"), "survival_servers.json")));

    var deathmatchServerListView = new ServerListView<>(new JsonServerList(
        Paths.get(System.getProperty("user.dir"), "tdm_servers.json")));

    officialView.addChild(new TabsView(new Properties())
        .addTab(() -> this.officialContent.replace(survivalServerListView),
            GuiUtil.translatable("menu.play.tab.survival"),
            tab -> {
              GuiUtil.addEventSound(tab, ActionEvent.class, ImmerseSoundEvents.TAB_SELECT);
              GuiUtil.addEventSound(tab, MouseEnterEvent.class, ImmerseSoundEvents.TAB_HOVER);
            })
        .addTab(() -> this.officialContent.replace(deathmatchServerListView),
            GuiUtil.translatable("menu.play.tab.tdm"),
            tab -> {
              GuiUtil.addEventSound(tab, ActionEvent.class, ImmerseSoundEvents.TAB_SELECT);
              GuiUtil.addEventSound(tab, MouseEnterEvent.class, ImmerseSoundEvents.TAB_HOVER);
            }));
    officialView.addChild(this.officialContent);

    var singleplayerView = new WorldListView();

    var customServerListView = new MutableServerListView<>(new NbtServerList(
        CraftingDeadImmerse.getInstance().getModDir().resolve("custom_servers.dat")));

    this.addChild(new TextView(new Properties().id("title"))
        .setText(AdapterUtil.createStyledText(new TranslatableComponent("menu.play.title"))));
    this.addChild(Theme.newSeparator());

    var dropdownView = new DropdownView(new Properties())
        .addItem(
            GuiUtil.translatable("menu.play.dropdown.official"),
            () -> this.dropDownSelect(officialView))
        .addItem(
            GuiUtil.translatable("menu.play.dropdown.singleplayer"),
            () -> this.dropDownSelect(singleplayerView))
        .addItem(
            GuiUtil.translatable("menu.play.dropdown.custom"),
            () -> this.dropDownSelect(customServerListView));
    GuiUtil.addEventSound(dropdownView, DropdownView.ToggleExpandedEvent.class,
        ImmerseSoundEvents.DROP_DOWN_EXPAND);
    GuiUtil.addEventSound(dropdownView, DropdownView.ItemHoverEvent.class,
        ImmerseSoundEvents.TAB_HOVER);

    this.addChild(dropdownView);
    this.addChild(Theme.newSeparator());
    this.addChild(this.content);

    this.entranceAnimator = new Animator.Builder()
        .addTarget(Animation.forProperty(this.getStyle().xTranslation)
            .keyFrames(new KeyFrames.Builder<>((float) -this.graphicsContext.width())
                .addFrame(0.0F)
                .build())
            .build())
        .setInterpolator(new SplineInterpolator(0.1F, 1.0F, 0.1F, 1.0F))
        .setDuration(250L, TimeUnit.MILLISECONDS)
        .build();

    this.exitAnimator = new Animator.Builder()
        .addTarget(Animation.forProperty(this.getStyle().xTranslation)
            .to((float) -this.graphicsContext.width())
            .build())

        .setDuration(250L, TimeUnit.MILLISECONDS)
        .addTarget(new TimingTargetAdapter() {
          @Override
          public void end(Animator source) {
            PlayView.this.removeCallback.run();
          }
        })
        .build();
  }

  private void dropDownSelect(View content) {
    GuiUtil.playSound(ImmerseSoundEvents.SUBMENU_SELECT);
    this.setContent(content);
  }

  private void setContent(View content) {
    this.content.replace(content);
  }

  @Override
  public void added() {
    super.added();
    this.entranceAnimator.start();
    GuiUtil.playSound(ImmerseSoundEvents.MAIN_MENU_PRESS_PLAY);
  }

  @Override
  public void removed() {
    super.removed();
    this.entranceAnimator.stop();
    this.exitAnimator.stop();
  }

  @Override
  public void transitionOut(Runnable removeCallback) {
    this.removeCallback = removeCallback;
    this.exitAnimator.start();
  }
}
