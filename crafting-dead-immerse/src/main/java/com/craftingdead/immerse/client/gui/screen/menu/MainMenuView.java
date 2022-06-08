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

package com.craftingdead.immerse.client.gui.screen.menu;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.tags.ModItemTags;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.fake.FakePlayer;
import com.craftingdead.immerse.client.gui.screen.FogView;
import com.craftingdead.immerse.client.gui.screen.Theme;
import com.craftingdead.immerse.client.gui.screen.menu.play.PlayView;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import sm0keysa1m0n.bliss.minecraft.view.EntityView;
import sm0keysa1m0n.bliss.view.ParentView;
import sm0keysa1m0n.bliss.view.TextView;
import sm0keysa1m0n.bliss.view.View;
import sm0keysa1m0n.bliss.view.ViewScreen;

public class MainMenuView extends ParentView {

  private static final Component TITLE = new TranslatableComponent("menu.home.title");

  private final ParentView contentContainer = new ParentView(new Properties().id("content"));

  @SuppressWarnings("removal")
  public MainMenuView() {
    super(new Properties());

    var homeView = new HomeView();
    var playView = new PlayView();

    this.addChild(Theme.createBackground());

    this.addChild(new FogView(new Properties()));

    var sideBar = new ParentView(new Properties().id("side-bar").styleClasses("blur"));

    sideBar.addChild(this.createButton(
        new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/logo.svg"),
        new Properties().id("home"),
        new TranslatableComponent("menu.home_button"),
        () -> this.transitionTo(homeView)));

    sideBar.addChild(Theme.newSeparator());

    sideBar.addChild(this.createButton(
        new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/play.svg"),
        new Properties().id("play").styleClasses("grow-button"),
        new TranslatableComponent("menu.play_button"),
        () -> this.transitionTo(playView)));

    sideBar.addChild(this.createButton(
        new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/settings.svg"),
        new Properties().id("settings").styleClasses("grow-button"),
        new TranslatableComponent("menu.options"),
        () -> this.getScreen().keepOpenAndSetScreen(
            new OptionsScreen(this.getScreen(), this.minecraft.options))));

    sideBar.addChild(this.createButton("quit-container",
        new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/power.svg"),
        new Properties().id("quit").styleClasses("grow-button"),
        new TranslatableComponent("menu.quit"),
        this.minecraft::stop));

    var fakePlayerEntity = new FakePlayer(this.minecraft.getUser().getGameProfile());

    var hatItem = ForgeRegistries.ITEMS.tags()
        .getTag(ModItemTags.HATS)
        .getRandomElement(ThreadLocalRandom.current())
        .map(Item::getDefaultInstance)
        .orElse(ItemStack.EMPTY);

    var livingExtension = LivingExtension.getOrThrow(fakePlayerEntity);
    livingExtension.getItemHandler().insertItem(ModEquipmentSlot.HAT.getIndex(), hatItem, false);

    var entityContainer = new ParentView(new Properties().id("entity-container"));
    entityContainer.addChild(new EntityView(new Properties(), fakePlayerEntity));
    this.addChild(entityContainer);

    this.addChild(this.contentContainer);

    this.addChild(sideBar);

    this.contentContainer.addChild(homeView);
  }

  private View createButton(ResourceLocation imageLocation, Properties properties,
      FormattedText tooltip, Runnable actionListener) {
    return this.createButton(null, imageLocation, properties, tooltip, actionListener);
  }

  private View createButton(@Nullable String containerId, ResourceLocation imageLocation,
      Properties properties, FormattedText tooltip, Runnable actionListener) {
    var buttonContainer =
        new ParentView(new Properties().id(containerId).styleClasses("tooltip-container"));
    buttonContainer.addChild(Theme.createImageButton(imageLocation,
        actionListener, properties));
    var tooltipView = new ParentView(new Properties().styleClasses("tooltip"));
    tooltipView.addChild(new TextView(new Properties()).setText(tooltip));
    buttonContainer.addChild(tooltipView);
    return buttonContainer;
  }

  private void transitionTo(View view) {
    var views = this.contentContainer.getChildren();
    if (views.contains(view)) {
      return;
    }
    if (views.size() == 1) {
      var currentView = views.get(0);
      if (currentView instanceof TransitionView animatable) {
        animatable.transitionOut(() -> {
          this.contentContainer.removeChild(currentView);
          this.contentContainer.layout();
        });
      } else {
        this.contentContainer.removeChild(currentView);
      }
    }
    this.contentContainer.addChild(view);
    this.contentContainer.layout();
  }

  public static Screen createScreen() {
    var screen = new ViewScreen(TITLE, new MainMenuView());
    screen.setStylesheets(List.of(new ResourceLocation(CraftingDeadImmerse.ID, "main-menu")));
    return screen;
  }
}
