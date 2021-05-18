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

package com.craftingdead.immerse.client.gui.screen.menu.play.list.server;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.immerse.client.gui.screen.menu.play.PlayView;
import com.craftingdead.immerse.client.gui.view.Colour;
import com.craftingdead.immerse.client.gui.view.Overflow;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Align;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Justify;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.craftingdead.immerse.util.ModSoundEvents;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.util.DefaultUncaughtExceptionHandler;
import net.minecraft.util.text.TranslationTextComponent;

public class ServerListView<L extends Layout>
    extends ParentView<ServerListView<L>, L, YogaLayout> {

  private static final Logger logger = LogManager.getLogger();

  private final ServerEntryReader serverEntryProvider;

  private final ParentView<?, YogaLayout, YogaLayout> listContainer;

  private static final Executor executor = Executors.newFixedThreadPool(5,
      new ThreadFactoryBuilder()
          .setNameFormat("Server Provider #%d")
          .setDaemon(true)
          .setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(logger))
          .build());

  public ServerListView(L layout, ServerEntryReader serverEntryProvider) {
    super(layout, new YogaLayoutParent().setFlexDirection(FlexDirection.COLUMN));
    this.serverEntryProvider = serverEntryProvider;

    this.listContainer = new ParentView<>(
        new YogaLayout()
            .setBottomPadding(7F)
            .setHeight(60F)
            .setFlexGrow(1F),
        new YogaLayoutParent()
            .setFlexDirection(FlexDirection.COLUMN)
            .setAlignItems(Align.CENTER))
                .setOverflow(Overflow.SCROLL)
                .addChild(this.firstTableRow());
    this.addChild(this.listContainer);

    this.addChild(this.controlsContainer());

    executor.execute(() -> this.serverEntryProvider.read(entry -> this.minecraft.execute(() -> {
      this.listContainer.addChild(new ServerItemView(entry));
      this.listContainer.layout();
    })));
  }

  protected View<?, YogaLayout> controlsContainer() {
    return new ParentView<>(
        new YogaLayout()
            .setHeight(30F),
        new YogaLayoutParent()
            .setJustifyContent(Justify.CENTER)
            .setAlignItems(Align.CENTER)
            .setFlexDirection(FlexDirection.ROW))
                .setBackgroundColour(new Colour(0x40121212))
                .addChild(new ParentView<>(
                    new YogaLayout()
                        .setHeight(21F)
                        .setRightMargin(15F)
                        .setWidth(70F)
                        .setTopPadding(1F),
                    new YogaLayoutParent()
                        .setJustifyContent(Justify.CENTER)
                        .setAlignItems(Align.CENTER))
                            .addChild(new TextView<>(new YogaLayout().setHeight(8F),
                                new TranslationTextComponent(
                                    "menu.play.server_list.button.refresh"))
                                        .setShadow(false)
                                        .setCentered(true))
                            .setBackgroundColour(new Colour(PlayView.BLUE))
                            .addHoverAnimation(View.BACKGROUND_COLOUR,
                                RenderUtil.getColour4f(
                                    RenderUtil.getColour4i(PlayView.BLUE_HIGHLIGHTED)),
                                60F)
                            .addActionSound(ModSoundEvents.BUTTON_CLICK.get())
                            .setFocusable(true)
                            .addListener(ActionEvent.class, (c, e) -> this.refresh()))
                .addChild(new ParentView<>(
                    new YogaLayout()
                        .setHeight(21F)
                        .setWidth(70F)
                        .setTopPadding(1F),
                    new YogaLayoutParent()
                        .setJustifyContent(Justify.CENTER)
                        .setAlignItems(Align.CENTER))
                            .addChild(new TextView<>(new YogaLayout().setHeight(8F),
                                new TranslationTextComponent("menu.play.server_list.button.play"))
                                    .setShadow(false)
                                    .setCentered(true))
                            .setBackgroundColour(new Colour(PlayView.GREEN))
                            .addHoverAnimation(View.BACKGROUND_COLOUR,
                                RenderUtil
                                    .getColour4f(
                                        RenderUtil.getColour4i(PlayView.GREEN_HIGHLIGHTED)),
                                60F)
                            .addActionSound(ModSoundEvents.BUTTON_CLICK.get())
                            .setFocusable(true)
                            .addListener(ActionEvent.class, (c, e) -> this.connectToSelected()));
  }

  private View<?, YogaLayout> firstTableRow() {
    return new ParentView<>(
        new YogaLayout()
            .setTopMargin(5F)
            .setBottomMargin(1F)
            .setLeftMargin(7F)
            .setRightMargin(7F)
            .setLeftPadding(10F)
            .setRightPadding(20F)
            .setHeight(22F)
            .setMaxWidth(520F),
        new YogaLayoutParent()
            .setFlexDirection(FlexDirection.ROW)
            .setAlignItems(Align.CENTER))
                .setBackgroundColour(new Colour(0x88121212))
                .addChild(new TextView<>(new YogaLayout().setFlex(2F).setHeight(8),
                    new TranslationTextComponent("menu.play.server_list.motd"))
                        .setShadow(false)
                        .setCentered(true))
                .addChild(new TextView<>(new YogaLayout().setFlex(1).setHeight(8),
                    new TranslationTextComponent("menu.play.server_list.map"))
                        .setShadow(false)
                        .setCentered(true))
                .addChild(new TextView<>(
                    new YogaLayout().setWidth(60F).setHeight(8).setLeftMargin(10F),
                    new TranslationTextComponent("menu.play.server_list.ping"))
                        .setShadow(false)
                        .setCentered(true))
                .addChild(new TextView<>(new YogaLayout()
                    .setWidth(60F)
                    .setHeight(8)
                    .setLeftMargin(10F),
                    new TranslationTextComponent("menu.play.server_list.players"))
                        .setShadow(false)
                        .setCentered(true));
  }

  private void connectToSelected() {
    this.streamItems()
        .filter(ServerItemView::isSelected)
        .findFirst()
        .ifPresent(ServerItemView::connect);
  }

  private void refresh() {
    this.streamItems().forEach(ServerItemView::ping);
  }

  private Stream<ServerItemView> streamItems() {
    return this.listContainer.getChildComponents().stream()
        .filter(child -> child instanceof ServerItemView)
        .map(child -> (ServerItemView) child);
  }
}
