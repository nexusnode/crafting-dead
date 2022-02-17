/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

/**
 * Crafting Dead Copyright (C) 2020 Nexus Node
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package com.craftingdead.immerse.client.gui.screen.menu;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import com.craftingdead.core.world.item.HatItem;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.fake.FakePlayerEntity;
import com.craftingdead.immerse.client.gui.screen.Theme;
import com.craftingdead.immerse.client.gui.screen.menu.play.PlayView;
import com.craftingdead.immerse.client.gui.view.EntityView;
import com.craftingdead.immerse.client.gui.view.FogView;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.Tooltip;
import com.craftingdead.immerse.client.gui.view.ViewScreen;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class MainMenuView extends ParentView {

  private static final Component TITLE = new TranslatableComponent("menu.home.title");

  private final ParentView contentContainer = new ParentView(new Properties<>().id("content"));

  public MainMenuView() {
    super(new Properties<>());

    var homeView = new HomeView();
    var playView = new PlayView();

    this.addChild(Theme.createBackground());

    this.addChild(new FogView(new Properties<>()));

    var sideBar = new ParentView(new Properties<>().id("side-bar").backgroundBlur(50.0F));

    var homeButton = Theme.createImageButton(
        new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/icon.png"),
        () -> this.contentContainer.replace(homeView),
        new Properties<>()
            .id("home")
            .tooltip(new Tooltip(new TranslatableComponent("menu.home_button"))));
    sideBar.addChild(homeButton);

    sideBar.addChild(Theme.newSeparator());

    var playButton = Theme.createImageButton(
        new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/play.png"),
        () -> this.contentContainer.replace(playView),
        new Properties<>()
            .id("play")
            .styleClasses("grow-button")
            .tooltip(new Tooltip(new TranslatableComponent("menu.play_button"))));
    playButton.setBilinearFiltering(true);
    sideBar.addChild(playButton);

    var settingsButton = Theme.createImageButton(
        new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/settings.png"),
        () -> this.getScreen().keepOpenAndSetScreen(
            new OptionsScreen(this.getScreen(), this.minecraft.options)),
        new Properties<>()
            .id("settings")
            .styleClasses("grow-button")
            .tooltip(new Tooltip(new TranslatableComponent("menu.options"))));
    settingsButton.setBilinearFiltering(true);
    sideBar.addChild(settingsButton);

    var quitButton = Theme.createImageButton(
        new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/power.png"),
        this.minecraft::stop,
        new Properties<>()
            .id("quit")
            .styleClasses("grow-button"));
    quitButton.setBilinearFiltering(true);
    sideBar.addChild(quitButton);

    var fakePlayerEntity = new FakePlayerEntity(this.minecraft.getUser().getGameProfile());

    var hatItems = ForgeRegistries.ITEMS.getValues()
        .stream()
        .filter(item -> item instanceof HatItem)
        .toList();
    var randomHatItem = hatItems.get(ThreadLocalRandom.current().nextInt(hatItems.size()));

    var livingExtension = LivingExtension.getOrThrow(fakePlayerEntity);
    livingExtension.getItemHandler().insertItem(ModEquipmentSlot.HAT.getIndex(),
        randomHatItem.getDefaultInstance(), false);

    var entityContainer = new ParentView(new Properties<>().id("entity-container"));
    entityContainer.addChild(new EntityView(new Properties<>(), fakePlayerEntity));
    this.addChild(entityContainer);

    this.addChild(this.contentContainer);

    this.addChild(sideBar);

    this.contentContainer.replace(homeView);
  }

  public static Screen createScreen() {
    var screen = new ViewScreen(TITLE, new MainMenuView());
    screen.setStylesheets(
        List.of(new ResourceLocation(CraftingDeadImmerse.ID, "css/main-menu.css")));
    return screen;
  }
}
