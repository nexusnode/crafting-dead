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

package com.craftingdead.immerse.client.gui.menu.play;

import java.nio.file.Paths;
import com.craftingdead.immerse.client.gui.component.Colour;
import com.craftingdead.immerse.client.gui.component.Component;
import com.craftingdead.immerse.client.gui.component.ContainerComponent;
import com.craftingdead.immerse.client.gui.component.ContentTabsComponent;
import com.craftingdead.immerse.client.gui.component.DropdownComponent;
import com.craftingdead.immerse.client.gui.component.RectangleComponent;
import com.craftingdead.immerse.client.gui.component.TabsComponent;
import com.craftingdead.immerse.client.gui.component.TextBlockComponent;
import com.craftingdead.immerse.client.gui.component.serverentry.JsonServerList;
import net.minecraft.util.text.TranslationTextComponent;

public class PlayComponent extends ContainerComponent {

  public static final long RED = 0x66ff7583;
  public static final long RED_HIGHLIGHTED = 0x66ff8c98;
  public static final long GREEN = 0x6652F2B7;
  public static final long GREEN_HIGHLIGHTED = 0x6692F0CE;
  public static final long BLUE = 0x6674b9f7;
  public static final long BLUE_HIGHLIGHTED = 0x6691cbff;

  private final ContainerComponent dropdownContent;

  private void displayContent(Component<?> content) {
    this.dropdownContent.clearChildren();
    this.dropdownContent.addChild(content);
    this.dropdownContent.layout();
  }

  public PlayComponent() {
    this.dropdownContent = new ContainerComponent();

    ContainerComponent officialContent = new ContainerComponent();
    ContainerComponent singleplayerContent = new ContainerComponent();
    ContainerComponent container = new ContainerComponent()
        .setBackgroundColour(new Colour(0x50777777))
        .setBackgroundBlur(50.0F)
        .addChild(new TextBlockComponent(new TranslationTextComponent(
            "menu.play.title"))
                .setShadow(true)
                .setPadding(12)
                .setScale(1.5F)
                .setWidthPercent(100)
                .setHeight(30))
        .addChild(this.newSeparator())
        .addChild(new DropdownComponent()
            .addItem(new TranslationTextComponent("menu.play.dropdown.official"),
                () -> this.displayContent(officialContent))
            .addItem(new TranslationTextComponent("menu.play.dropdown.singleplayer"),
                () -> this.displayContent(singleplayerContent))
            // .addItem(3, new TranslationTextComponent("menu.play.dropdown.community"),
            // new ServerListComponent("tdm")
            // .setBackgroundColour(new Colour(0, 0, 0, 0.25F))
            // .setHeight(19)
            // .setWidthPercent(100))
            .setWidth(100F)
            .setHeight(21F)
            .setTopMargin(1)
            .setBottomPadding(1)
            .setLeftMargin(10F))
        .addChild(this.newSeparator())
        .addChild(this.dropdownContent
            .setFlexShrink(1F));

    ContainerComponent officialContentContainer = new ContainerComponent();
    officialContent
        .addChild(new ContentTabsComponent(officialContentContainer)
            .addTab(new TabsComponent.Tab(new TranslationTextComponent("menu.play.tab.survival")),
                new ServerListComponent(new JsonServerList(
                    Paths.get(System.getProperty("user.dir"), "survival_servers.json")))
                        .setBackgroundColour(new Colour(0, 0, 0, 0.25F))
                        .setTopMargin(1F))
            .addTab(
                new TabsComponent.Tab(
                    new TranslationTextComponent("menu.play.tab.tdm")),
                new ServerListComponent(new JsonServerList(
                    Paths.get(System.getProperty("user.dir"), "tdm_servers.json")))
                        .setBackgroundColour(new Colour(0, 0, 0, 0.25F))
                        .setTopMargin(1F))
            .setHeight(19)
            .setZLevelOffset(2))
        .addChild(officialContentContainer
            .setFlexShrink(1F));


    singleplayerContent
        .addChild(new ContainerComponent()
            .setBackgroundColour(new Colour(0, 0, 0, 0.25F))
            .addChild(new WorldListComponent()
                .setFlexShrink(1F))
            .setFlexShrink(1F));

    container.layout();
    this.addChild(container);

  }

  private RectangleComponent newSeparator() {
    return new RectangleComponent()
        .setUnscaleHeight()
        .setHeight(1F)
        .setWidthPercent(100.0F)
        .setBackgroundColour(new Colour(0X80B5B5B5));
  }
}
