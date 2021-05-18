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
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.JsonServerList;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.server.ServerListView;
import com.craftingdead.immerse.client.gui.screen.menu.play.list.world.WorldListView;
import com.craftingdead.immerse.client.gui.view.Colour;
import com.craftingdead.immerse.client.gui.view.DropdownView;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.TabsView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import io.noties.tumbleweed.Tween;
import io.noties.tumbleweed.TweenCallback;
import io.noties.tumbleweed.equations.Expo;
import net.minecraft.util.text.TranslationTextComponent;

public class PlayView extends ParentView<PlayView, YogaLayout, YogaLayout> {

  public static final long RED = 0x66ff7583;
  public static final long RED_HIGHLIGHTED = 0x66ff8c98;
  public static final long GREEN = 0x6652F2B7;
  public static final long GREEN_HIGHLIGHTED = 0x6692F0CE;
  public static final long BLUE = 0x6674b9f7;
  public static final long BLUE_HIGHLIGHTED = 0x6691cbff;

  private final ParentView<?, YogaLayout, YogaLayout> dropdownContent =
      new ParentView<>(new YogaLayout().setFlexShrink(1), new YogaLayoutParent());

  public PlayView() {
    super(new YogaLayout(), new YogaLayoutParent());

    ParentView<?, YogaLayout, YogaLayout> officialParent =
        new ParentView<>(new YogaLayout(), new YogaLayoutParent());

    ParentView<?, YogaLayout, YogaLayout> serverListParent =
        new ParentView<>(new YogaLayout().setFlexShrink(1), new YogaLayoutParent());

    View<?, YogaLayout> survivalServerList = new ServerListView<>(
        new YogaLayout().setTopMargin(1F),
        new JsonServerList(
            Paths.get(System.getProperty("user.dir"), "survival_servers.json")))
                .setBackgroundColour(new Colour(0, 0, 0, 0.25F));

    View<?, YogaLayout> deathmatchServerList = new ServerListView<>(
        new YogaLayout().setTopMargin(1F),
        new JsonServerList(
            Paths.get(System.getProperty("user.dir"), "tdm_servers.json")))
                .setBackgroundColour(new Colour(0, 0, 0, 0.25F));

    officialParent
        .addChild(new TabsView<>(new YogaLayout().setHeight(20))
            .addTab(new TabsView.Tab(new TranslationTextComponent("menu.play.tab.survival"),
                () -> serverListParent.clearChildren().addChild(survivalServerList)
                    .layout()))
            .addTab(new TabsView.Tab(new TranslationTextComponent("menu.play.tab.tdm"),
                () -> serverListParent.clearChildren().addChild(deathmatchServerList)
                    .layout())))
        .addChild(serverListParent);

    ParentView<?, YogaLayout, YogaLayout> singleplayerParent =
        new WorldListView<>(new YogaLayout()
            .setFlexShrink(0)
            .setFlexGrow(1));

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
            .setTopMargin(2)
            .setBottomPadding(1)
            .setLeftMargin(10F))
                .addItem(new TranslationTextComponent("menu.play.dropdown.official"),
                    () -> this.displayContent(officialParent))
                .addItem(new TranslationTextComponent("menu.play.dropdown.singleplayer"),
                    () -> this.displayContent(singleplayerParent)))
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
        .clearChildren()
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
  protected void removed(Runnable remove) {
    Tween.to(this, X_TRANSLATION, 800.0F)
        .ease(Expo.OUT)
        .target(-this.minecraft.screen.width)
        .addCallback(TweenCallback.COMPLETE, (type, source) -> remove.run())
        .start(this.getTweenManager());
  }
}
