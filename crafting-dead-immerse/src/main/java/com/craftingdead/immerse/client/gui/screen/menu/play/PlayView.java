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

package com.craftingdead.immerse.client.gui.screen.menu.play;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.KeyFrames;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.JsonServerList;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.MutableServerListView;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.NbtServerList;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.ServerListView;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.world.WorldListView;
import com.craftingdead.immerse.client.gui.view.Animation;
import com.craftingdead.immerse.client.gui.view.Color;
import com.craftingdead.immerse.client.gui.view.DropDownView;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TabsView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import com.craftingdead.immerse.sounds.ImmerseSoundEvents;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.text.TranslationTextComponent;

public class PlayView extends ParentView<PlayView, YogaLayout, YogaLayout> {

  private final ParentView<?, YogaLayout, YogaLayout> dropdownContent =
      new ParentView<>(new YogaLayout().setFlexShrink(1), new YogaLayoutParent());

  public PlayView() {
    super(new YogaLayout(), new YogaLayoutParent());

    ParentView<?, YogaLayout, YogaLayout> officialView =
        new ParentView<>(new YogaLayout(), new YogaLayoutParent());

    ParentView<?, YogaLayout, YogaLayout> officialServerListView =
        new ParentView<>(new YogaLayout().setFlexShrink(1), new YogaLayoutParent());
    officialServerListView.getBackgroundColorProperty().setBaseValue(new Color(0, 0, 0, 0.25F));

    View<?, YogaLayout> survivalServerListView = new ServerListView<>(
        new YogaLayout().setTopMargin(1F),
        new JsonServerList(
            Paths.get(System.getProperty("user.dir"), "survival_servers.json")));

    View<?, YogaLayout> deathmatchServerListView = new ServerListView<>(
        new YogaLayout().setTopMargin(1F),
        new JsonServerList(
            Paths.get(System.getProperty("user.dir"), "tdm_servers.json")));

    officialView
        .addChild(new TabsView<>(new YogaLayout().setHeight(20))
            .setZOffset(5)
            .addTab((TabsView.Tab) new TabsView.Tab(
                new TranslationTextComponent("menu.play.tab.survival"),
                () -> officialServerListView.queueAllForRemoval().addChild(survivalServerListView)
                    .layout())
                        .addActionSound(ImmerseSoundEvents.TAB_SELECT.get())
                        .addHoverSound(ImmerseSoundEvents.TAB_HOVER.get()))
            .addTab((TabsView.Tab) new TabsView.Tab(
                new TranslationTextComponent("menu.play.tab.tdm"),
                () -> officialServerListView.queueAllForRemoval().addChild(deathmatchServerListView)
                    .layout())
                        .addActionSound(ImmerseSoundEvents.TAB_SELECT.get())
                        .addHoverSound(ImmerseSoundEvents.TAB_HOVER.get())))
        .addChild(officialServerListView);

    View<?, YogaLayout> singleplayerView = new WorldListView<>(new YogaLayout());
    singleplayerView.getBackgroundColorProperty().setBaseValue(new Color(0, 0, 0, 0.25F));

    View<?, YogaLayout> customServerListView = new MutableServerListView<>(
        new YogaLayout(),
        new NbtServerList(
            CraftingDeadImmerse.getInstance().getModDir().resolve("custom_servers.dat")));
    customServerListView.getBackgroundColorProperty().setBaseValue(new Color(0, 0, 0, 0.25F));

    this.getBackgroundColorProperty().setBaseValue(new Color(0x50777777));

    this
        .setBackgroundBlur(50.0F)
        .addChild(new TextView<>(
            new YogaLayout()
                .setWidth(25)
                .setHeight(30)
                .setTopPadding(12)
                .setLeftMargin(17),
            new TranslationTextComponent("menu.play.title"))
                .setShadow(true)
                .configure(view -> view.getXScaleProperty().setBaseValue(1.5F))
                .configure(view -> view.getYScaleProperty().setBaseValue(1.5F)))
        .addChild(this.newSeparator())
        .addChild(new DropDownView<>(new YogaLayout()
            .setWidth(100F)
            .setHeight(21F)
            .setMargin(2)
            .setLeftMargin(10F))
                .setExpandSound(ImmerseSoundEvents.DROP_DOWN_EXPAND.get())
                .setItemHoverSound(ImmerseSoundEvents.TAB_HOVER.get())
                .addItem(new TranslationTextComponent("menu.play.dropdown.official"),
                    () -> this.dropDownSelect(officialView))
                .addItem(new TranslationTextComponent("menu.play.dropdown.singleplayer"),
                    () -> this.dropDownSelect(singleplayerView))
                .addItem(new TranslationTextComponent("menu.play.dropdown.custom"),
                    () -> this.dropDownSelect(customServerListView)))
        .addChild(this.newSeparator())
        .addChild(this.dropdownContent);
  }

  private void dropDownSelect(View<?, YogaLayout> content) {
    this.minecraft.getSoundManager().play(
        SimpleSound.forUI(ImmerseSoundEvents.SUBMENU_SELECT.get(), 1.0F));
    this.displayContent(content);
  }

  private View<?, YogaLayout> newSeparator() {
    return new View<>(new YogaLayout().setHeight(1F))
        .setUnscaleHeight(true)
        .configure(view -> view.getBackgroundColorProperty().setBaseValue(new Color(0X80B5B5B5)));
  }

  private void displayContent(View<?, YogaLayout> content) {
    this.dropdownContent
        .queueAllForRemoval()
        .addChild(content)
        .layout();
  }

  @Override
  protected void added() {
    this.minecraft.getSoundManager().play(
        SimpleSound.forUI(ImmerseSoundEvents.MAIN_MENU_PRESS_PLAY.get(), 1.0F));

    new Animator.Builder()
        .addTarget(Animation.forProperty(this.getXTranslationProperty())
            .keyFrames(new KeyFrames.Builder<>(-this.getScreen().getWidth())
                .addFrame(0.0F)
                .build())
            .build())
        .setInterpolator(new SplineInterpolator(0.25, 0.1, 0.25, 1))
        .setDuration(250L, TimeUnit.MILLISECONDS)
        .build()
        .start();
  }

  @Override
  protected void queueRemoval(Runnable remove) {
    new Animator.Builder()
        .addTarget(Animation.forProperty(this.getXTranslationProperty())
            .to(-this.getScreen().getWidth())
            .build())
        .setInterpolator(new SplineInterpolator(0.25, 0.1, 0.25, 1))
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
