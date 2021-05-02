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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.immerse.client.gui.component.Colour;
import com.craftingdead.immerse.client.gui.component.Component;
import com.craftingdead.immerse.client.gui.component.ContainerComponent;
import com.craftingdead.immerse.client.gui.component.ParentComponent;
import com.craftingdead.immerse.client.gui.component.TextBlockComponent;
import com.craftingdead.immerse.client.gui.component.event.ActionEvent;
import com.craftingdead.immerse.client.gui.component.serverentry.IServerEntryReader;
import com.craftingdead.immerse.client.gui.component.type.Align;
import com.craftingdead.immerse.client.gui.component.type.FlexDirection;
import com.craftingdead.immerse.client.gui.component.type.Justify;
import com.craftingdead.immerse.client.gui.component.type.Overflow;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.craftingdead.immerse.util.ModSoundEvents;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.util.DefaultUncaughtExceptionHandler;
import net.minecraft.util.text.TranslationTextComponent;

public class ServerListComponent extends ParentComponent<ServerListComponent> {

  private static final Logger logger = LogManager.getLogger();

  private final IServerEntryReader serverEntryProvider;

  private final ContainerComponent listContainer;

  private static final Executor executor = Executors.newFixedThreadPool(5,
      new ThreadFactoryBuilder()
          .setNameFormat("Server Provider #%d")
          .setDaemon(true)
          .setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(logger))
          .build());

  public ServerListComponent(IServerEntryReader serverEntryProvider) {
    this.serverEntryProvider = serverEntryProvider;

    this.setFlexGrow(1F);
    this.setFlexDirection(FlexDirection.COLUMN);

    this.listContainer = new ContainerComponent()
        .addChild(this.firstTableRow())
        .setFlexDirection(FlexDirection.COLUMN)
        .setAlignItems(Align.CENTER)
        .setOverflow(Overflow.SCROLL)
        .setBottomPadding(7F)
        .setHeight(60F)
        .setFlexGrow(1F);
    this.addChild(this.listContainer);

    this.addChild(this.controlsContainer());

    executor.execute(() -> this.serverEntryProvider.read(entry -> this.minecraft.execute(() -> {
      this.listContainer.addChild(new ServerItemComponent(entry));
      this.listContainer.layout();
    })));
  }

  protected Component<?> controlsContainer() {
    return new ContainerComponent()
        .setHeight(30F)
        .setJustifyContent(Justify.CENTER)
        .setAlignItems(Align.CENTER)
        .setFlexDirection(FlexDirection.ROW)
        .setBackgroundColour(new Colour(0x40121212))
        .addChild(new ContainerComponent()
            .addChild(new TextBlockComponent(
                new TranslationTextComponent("menu.play.server_list.button.refresh"))
                    .setShadow(false)
                    .setHeight(8F))
            .setJustifyContent(Justify.CENTER)
            .setAlignItems(Align.CENTER)
            .setBackgroundColour(new Colour(PlayComponent.BLUE))
            .addHoverAnimation(Component.BACKGROUND_COLOUR,
                RenderUtil.getColour4f(RenderUtil.getColour4i(PlayComponent.BLUE_HIGHLIGHTED)), 60F)
            .setHeight(21F)
            .setRightMargin(15F)
            .setWidth(70F)
            .setTopPadding(1F)
            .addActionSound(ModSoundEvents.BUTTON_CLICK.get())
            .setFocusable(true)
            .addListener(ActionEvent.class, (c, e) -> this.refresh()))
        .addChild(new ContainerComponent()
            .addChild(new TextBlockComponent(
                new TranslationTextComponent("menu.play.server_list.button.play"))
                    .setShadow(false)
                    .setHeight(8F))
            .setJustifyContent(Justify.CENTER)
            .setAlignItems(Align.CENTER)
            .setBackgroundColour(new Colour(PlayComponent.GREEN))
            .addHoverAnimation(Component.BACKGROUND_COLOUR,
                RenderUtil.getColour4f(RenderUtil.getColour4i(PlayComponent.GREEN_HIGHLIGHTED)),
                60F)
            .setHeight(21F)
            .setWidth(70F)
            .setTopPadding(1F)
            .addActionSound(ModSoundEvents.BUTTON_CLICK.get())
            .setFocusable(true)
            .addListener(ActionEvent.class, (c, e) -> this.connectToSelected()));
  }

  private Component<?> firstTableRow() {
    return new ContainerComponent()
        .setTopMargin(5F)
        .setBottomMargin(1F)
        .setLeftMargin(7F)
        .setRightMargin(7F)
        .setLeftPadding(10F)
        .setRightPadding(20F)
        .setHeight(22F)
        .setBackgroundColour(new Colour(0x88121212))
        .setMaxWidth(520F)
        .setFlexDirection(FlexDirection.ROW)
        .setAlignItems(Align.CENTER)
        .addChild(new TextBlockComponent(new TranslationTextComponent("menu.play.server_list.motd"))
            .setFlex(2F)
            .setShadow(false)
            .setCentered(true)
            .setHeight(8))
        .addChild(new TextBlockComponent(new TranslationTextComponent("menu.play.server_list.map"))
            .setFlex(1)
            .setShadow(false)
            .setCentered(true)
            .setHeight(8))
        .addChild(new TextBlockComponent(new TranslationTextComponent("menu.play.server_list.ping"))
            .setWidth(60F)
            .setShadow(false)
            .setCentered(true)
            .setLeftMargin(10F)
            .setHeight(8))
        .addChild(
            new TextBlockComponent(new TranslationTextComponent("menu.play.server_list.players"))
                .setWidth(60F)
                .setShadow(false)
                .setCentered(true)
                .setLeftMargin(10F)
                .setHeight(8));
  }

  private void connectToSelected() {
    this.streamItems()
        .filter(ServerItemComponent::isSelected)
        .findFirst()
        .ifPresent(ServerItemComponent::connect);
  }

  private void refresh() {
    this.streamItems().forEach(ServerItemComponent::ping);
  }

  private Stream<ServerItemComponent> streamItems() {
    return this.listContainer.getChildComponents().stream()
        .filter(child -> child instanceof ServerItemComponent)
        .map(child -> (ServerItemComponent) child);
  }
}
