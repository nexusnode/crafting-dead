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

package com.craftingdead.immerse.client.gui.screen.menu.play.list.world;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.immerse.client.gui.screen.menu.play.PlayView;
import com.craftingdead.immerse.client.gui.view.Colour;
import com.craftingdead.immerse.client.gui.view.Overflow;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Align;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Justify;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.craftingdead.immerse.util.ModSoundEvents;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.storage.WorldSummary;

public class WorldListView<L extends Layout>
    extends ParentView<WorldListView<L>, L, YogaLayout> {

  private static final Logger logger = LogManager.getLogger();

  private final ParentView<?, YogaLayout, YogaLayout> worldListContainer;

  public WorldListView(L layout) {
    super(layout, new YogaLayoutParent().setFlexDirection(FlexDirection.COLUMN));

    this.worldListContainer = new ParentView<>(
        new YogaLayout()
            .setFlex(1)
            .setTopPadding(4F)
            .setBottomPadding(10F),
        new YogaLayoutParent()
            .setFlexDirection(FlexDirection.COLUMN)
            .setAlignItems(Align.CENTER))
                .setOverflow(Overflow.SCROLL);
    this.loadWorlds();

    ParentView<?, YogaLayout, YogaLayout> playButton = this.newDefaultStyleButton()
        .configure(b -> b.getLayout().setRightMargin(7F))
        .setBackgroundColour(new Colour(PlayView.GREEN))
        .addHoverAnimation(View.BACKGROUND_COLOUR,
            RenderUtil.getColour4f(RenderUtil.getColour4i(PlayView.GREEN_HIGHLIGHTED)), 60F)
        .addChild(newDefaultStyleButtonText("menu.play.world_list.button.play"))
        .addListener(ActionEvent.class, (c, e) -> this.streamItems()
            .filter(WorldItemView::isSelected)
            .findAny()
            .ifPresent(WorldItemView::joinWorld));

    ParentView<?, YogaLayout, YogaLayout> createButton = this.newDefaultStyleButton()
        .addChild(newDefaultStyleButtonText("menu.play.world_list.button.create"))
        .addListener(ActionEvent.class, (c, e) -> this.getScreen()
            .keepOpenAndSetScreen(CreateWorldScreen.create(this.getScreen())));

    ParentView<?, YogaLayout, YogaLayout> editButton = this.newDefaultStyleButton()
        .configure(b -> b.getLayout().setRightMargin(5.7F))
        .addChild(newDefaultStyleButtonText("menu.play.world_list.button.edit"))
        .addListener(ActionEvent.class, (c, e) -> this.streamItems()
            .filter(WorldItemView::isSelected)
            .findAny()
            .ifPresent(WorldItemView::editWorld));
    ParentView<?, YogaLayout, YogaLayout> deleteButton = this.newDefaultStyleButton()
        .configure(b -> b.getLayout().setRightMargin(5.7F))
        .setBackgroundColour(new Colour(PlayView.RED))
        .addHoverAnimation(View.BACKGROUND_COLOUR,
            RenderUtil.getColour4f(RenderUtil.getColour4i(PlayView.RED_HIGHLIGHTED)), 60F)
        .addChild(newDefaultStyleButtonText("menu.play.world_list.button.delete"))
        .addListener(ActionEvent.class, (c, e) -> this.streamItems()
            .filter(WorldItemView::isSelected)
            .findAny()
            .ifPresent(WorldItemView::deleteWorld));
    ParentView<?, YogaLayout, YogaLayout> recreateButton = this.newDefaultStyleButton()
        .addChild(newDefaultStyleButtonText("menu.play.world_list.button.recreate"))
        .addListener(ActionEvent.class, (c, e) -> this.streamItems()
            .filter(WorldItemView::isSelected)
            .findAny()
            .ifPresent(WorldItemView::recreateWorld));

    ParentView<?, YogaLayout, YogaLayout> controlsContainer =
        new ParentView<>(
            new YogaLayout().setHeight(56),
            new YogaLayoutParent()
                .setJustifyContent(Justify.CENTER)
                .setAlignItems(Align.CENTER)
                .setFlexDirection(FlexDirection.COLUMN))
                    .setBackgroundColour(new Colour(0x40121212))
                    .addChild(new ParentView<>(
                        new YogaLayout()
                            .setFlex(1)
                            .setWidth(220F),
                        new YogaLayoutParent()
                            .setFlexDirection(FlexDirection.ROW)
                            .setAlignItems(Align.CENTER))
                                .addChild(playButton)
                                .addChild(createButton))
                    .addChild(new ParentView<>(
                        new YogaLayout()
                            .setFlex(1)
                            .setWidth(220F),
                        new YogaLayoutParent()
                            .setFlexDirection(FlexDirection.ROW)
                            .setAlignItems(Align.CENTER))
                                .addChild(deleteButton)
                                .addChild(editButton)
                                .addChild(recreateButton));

    this.addChild(this.worldListContainer);
    this.addChild(controlsContainer);

  }

  private ParentView<?, YogaLayout, YogaLayout> newDefaultStyleButton() {
    return new ParentView<>(
        new YogaLayout()
            .setWidth(30F)
            .setHeight(20F)
            .setFlex(1F),
        new YogaLayoutParent()
            .setJustifyContent(Justify.CENTER)
            .setAlignItems(Align.CENTER))
                .addActionSound(ModSoundEvents.BUTTON_CLICK.get())
                .setBackgroundColour(new Colour(PlayView.BLUE))
                .setFocusable(true)
                .addHoverAnimation(View.BACKGROUND_COLOUR,
                    RenderUtil.getColour4f(RenderUtil.getColour4i(PlayView.BLUE_HIGHLIGHTED)),
                    60F);
  }

  private TextView<YogaLayout> newDefaultStyleButtonText(String translationKey) {
    return new TextView<>(new YogaLayout().setTopMargin(1F),
        new TranslationTextComponent(translationKey))
            .setShadow(false).setCentered(true);
  }

  private void loadWorlds() {
    try {
      List<WorldSummary> saveList = this.minecraft.getLevelSource().getLevelList();
      Collections.sort(saveList);
      for (WorldSummary worldSummary : saveList) {
        this.worldListContainer.addChild(new WorldItemView(worldSummary, this));
      }
    } catch (AnvilConverterException e) {
      logger.error("Unable to load save list", e);
    }
  }

  public void reloadWorlds() {
    this.worldListContainer.clearChildren();
    this.loadWorlds();
  }

  private Stream<WorldItemView> streamItems() {
    return this.worldListContainer.getChildComponents().stream()
        .filter(child -> child instanceof WorldItemView)
        .map(child -> (WorldItemView) child);
  }
}
