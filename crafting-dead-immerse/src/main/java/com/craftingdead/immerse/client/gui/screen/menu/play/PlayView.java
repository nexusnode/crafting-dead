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
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.JsonServerList;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.MutableServerListView;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.NBTMutableServerList;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.ServerListView;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.world.WorldListView;
import com.craftingdead.immerse.client.gui.view.Colour;
import com.craftingdead.immerse.client.gui.view.DropdownView;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TabsView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import io.noties.tumbleweed.Tween;
import io.noties.tumbleweed.TweenCallback;
import io.noties.tumbleweed.equations.Expo;
import net.minecraft.util.text.TranslationTextComponent;

public class PlayView extends ParentView<PlayView, YogaLayout, YogaLayout> {

  private final ParentView<?, YogaLayout, YogaLayout> dropdownContent =
      new ParentView<>(new YogaLayout().setFlexShrink(1), new YogaLayoutParent());

  public PlayView() {
    super(new YogaLayout(), new YogaLayoutParent());

    ParentView<?, YogaLayout, YogaLayout> officialView =
        new ParentView<>(new YogaLayout(), new YogaLayoutParent());

    ParentView<?, YogaLayout, YogaLayout> officialServerListView =
        new ParentView<>(new YogaLayout().setFlexShrink(1), new YogaLayoutParent())
            .setBackgroundColour(new Colour(0, 0, 0, 0.25F));

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
            .addTab(new TabsView.Tab(new TranslationTextComponent("menu.play.tab.survival"),
                () -> officialServerListView.queueAllForRemoval().addChild(survivalServerListView)
                    .layout()))
            .addTab(new TabsView.Tab(new TranslationTextComponent("menu.play.tab.tdm"),
                () -> officialServerListView.queueAllForRemoval().addChild(deathmatchServerListView)
                    .layout())))
        .addChild(officialServerListView);

    View<?, YogaLayout> singleplayerView = new WorldListView<>(new YogaLayout()
        .setFlexShrink(0)
        .setFlexGrow(1))
            .setBackgroundColour(new Colour(0, 0, 0, 0.25F));

    View<?, YogaLayout> customServerListView = new MutableServerListView<>(
        new YogaLayout(),
        new NBTMutableServerList(
            CraftingDeadImmerse.getInstance().getModDir().resolve("custom_servers.dat")))
                .setBackgroundColour(new Colour(0, 0, 0, 0.25F));

    this
        .setBackgroundColour(new Colour(0x50777777))
        .setBackgroundBlur(50.0F)
        .addChild(new TextView<>(
            new YogaLayout()
                .setWidth(25)
                .setHeight(30)
                .setTopPadding(12)
                .setLeftMargin(17),
            new TranslationTextComponent("menu.play.title"))
                .setShadow(true)
                .setScale(1.5F))
        .addChild(this.newSeparator())
        .addChild(new DropdownView<>(new YogaLayout()
            .setWidth(100F)
            .setHeight(21F)
            .setMargin(2)
            .setLeftMargin(10F))
                .addItem(new TranslationTextComponent("menu.play.dropdown.official"),
                    () -> this.displayContent(officialView))
                .addItem(new TranslationTextComponent("menu.play.dropdown.singleplayer"),
                    () -> this.displayContent(singleplayerView))
                .addItem(new TranslationTextComponent("menu.play.dropdown.custom"),
                    () -> this.displayContent(customServerListView)))
        .addChild(this.newSeparator())
        .addChild(this.dropdownContent);
  }

  private View<?, YogaLayout> newSeparator() {
    return new View<>(new YogaLayout().setHeight(1F))
        .setUnscaleHeight()
        .setBackgroundColour(new Colour(0X80B5B5B5));
  }

  private void displayContent(View<?, YogaLayout> content) {
    this.dropdownContent
        .queueAllForRemoval()
        .addChild(content)
        .layout();
  }

  @Override
  protected void added() {
    this.setXOffset(-this.minecraft.screen.width);
    Tween.to(this, X_TRANSLATION, 800.0F)
        .ease(Expo.OUT)
        .target(0.0F)
        .start(this.getTweenManager());
  }

  @Override
  protected void queueRemoval(Runnable remove) {
    Tween.to(this, X_TRANSLATION, 800.0F)
        .ease(Expo.OUT)
        .target(-this.minecraft.screen.width)
        .addCallback(TweenCallback.COMPLETE, (type, source) -> remove.run())
        .start(this.getTweenManager());
  }
}
