/*
 * Crafting Dead
 * Copyright (C)  2021  Nexus Node
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

import com.craftingdead.immerse.client.gui.component.*;
import net.minecraft.util.text.TranslationTextComponent;

public class PlayComponent extends ContainerComponent {

  public static final long RED = 0x66ff7583;
  public static final long RED_HIGHLIGHTED = 0x66ff8c98;
  public static final long GREEN = 0x6652F2B7;
  public static final long GREEN_HIGHLIGHTED = 0x6692F0CE;
  public static final long BLUE = 0x6674b9f7;
  public static final long BLUE_HIGHLIGHTED = 0x6691cbff;

  public PlayComponent() {
    ContainerComponent dropdownContent = new ContainerComponent();
    ContainerComponent officialContent = new ContainerComponent();
    ContainerComponent communityContent = new ContainerComponent();
    ContainerComponent singleplayerContent = new ContainerComponent();
    ContainerComponent container = new ContainerComponent()
        .setBackgroundColour(new Colour(0x50777777))
        .setBackgroundBlur(50.0F)
        .addChild(new TextBlockComponent(new TranslationTextComponent("menu.play.play"))
            .setShadow(true)
            .setMargin(5.5F))
        .addChild(this.newSeparator())
        .addChild(new ContentDropdownComponent(dropdownContent)
            .addItem(1, new TranslationTextComponent("menu.play.dropdown.official"), officialContent)
            .addItem(2, new TranslationTextComponent("menu.play.dropdown.singleplayer"), singleplayerContent)
            .addItem(3, new TranslationTextComponent("menu.play.dropdown.community"), communityContent)
            .setDisabled(3, true)
            .selectItem(1)
            .setWidth(100F)
            .setHeight(19F)
            .setTopMargin(1F)
            .setLeftMargin(1F))
        .addChild(dropdownContent
            .setFlexShrink(1F));

    ContainerComponent officialContentContainer = new ContainerComponent();
    ContainerComponent officialSurvivalContent = new ContainerComponent()
        .addChild(newSeparator())
        .addChild(new ServerListComponent("official_survival")
            .setTopMargin(1F));
    ContainerComponent officialTdmContent = new ContainerComponent()
        .addChild(newSeparator())
        .addChild(new ServerListComponent("official_tdm")
            .setTopMargin(1F));
    officialContent
        .addChild(this.newSeparator())
        .addChild(new ContentTabsComponent(officialContentContainer)
            .addTab(new TabsComponent.Tab(new TranslationTextComponent("menu.play.tab.survival")), officialSurvivalContent)
            .addTab(new TabsComponent.Tab(new TranslationTextComponent("menu.play.tab.team_death_match")), officialTdmContent)
            .setHeight(19)
            .setZLevel(1))
        .addChild(officialContentContainer
            .setFlexShrink(1F));


    ContainerComponent communityContentContainer = new ContainerComponent();
    ContainerComponent communitySurvivalContent = new ContainerComponent()
        .addChild(newSeparator())
        .addChild(new TextBlockComponent("Community Survival content")
            .setTopMargin(4F)
            .setShadow(false));
    communityContent.addChild(newSeparator())
        .addChild(new ContentTabsComponent(communityContentContainer)
            .addTab(new TabsComponent.Tab(new TranslationTextComponent("menu.play.tab.survival")), communitySurvivalContent)
            .setHeight(19F)
            .setZLevel(1))
        .addChild(communityContentContainer
            .setFlexShrink(1F));

    ContainerComponent spContentContainer = new ContainerComponent();
    ContainerComponent spSurvivalContent = new ContainerComponent()
        .addChild(newSeparator())
        .addChild(new WorldListComponent()
            .setFlexShrink(1F));
    singleplayerContent.addChild(newSeparator())
        .addChild(new ContentTabsComponent(spContentContainer)
            .addTab(new TabsComponent.Tab(new TranslationTextComponent("menu.play.tab.survival")), spSurvivalContent)
            .setHeight(19F)
            .setZLevel(1))
        .addChild(spContentContainer
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
