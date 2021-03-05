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
import com.craftingdead.immerse.client.gui.component.type.Align;
import com.craftingdead.immerse.client.gui.component.type.FlexDirection;
import com.craftingdead.immerse.client.gui.component.type.Justify;
import com.craftingdead.immerse.client.gui.component.type.Overflow;
import com.craftingdead.immerse.client.util.RenderUtil;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.storage.WorldSummary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class WorldListComponent extends ContainerComponent {

  private static final Logger LOGGER = LogManager.getLogger();

  private final ContainerComponent worldListContainer;

  public WorldListComponent() {
    this.setHeight(250F)
        .setFlexGrow(1F)
        .setTopMargin(1F)
        .setFlexDirection(FlexDirection.COLUMN)
        .setAlignItems(Align.CENTER);

    this.worldListContainer = new ContainerComponent()
        .setHeight(50F)
        .setFlexGrow(1F)
        .setFlexDirection(FlexDirection.COLUMN)
        .setAlignItems(Align.CENTER)
        .setTopPadding(4F)
        .setBottomPadding(10F)
        .setOverflow(Overflow.SCROLL)
        .addActionListener(this::resetSelection);
    this.loadWorlds(this.worldListContainer);


    ContainerComponent playButton = this.newDefaultStyleButton()
        .setRightMargin(7F)
        .setBackgroundColour(new Colour(PlayComponent.GREEN))
        .addHoverAnimation(Component.BACKGROUND_COLOUR,
            RenderUtil.getColour4f(RenderUtil.getColour4i(PlayComponent.GREEN_HIGHLIGHTED)), 60F)
        .addChild(newDefaultStyleButtonText("menu.play.world_list.button.play"))
        .addActionListener(button -> {
          this.worldItemsStream(this.worldListContainer)
              .filter(WorldItemComponent::isSelected)
              .findAny()
              .ifPresent(WorldItemComponent::joinWorld);
        });

    ContainerComponent createButton = this.newDefaultStyleButton()
        .addChild(newDefaultStyleButtonText("menu.play.world_list.button.create"))
        .addActionListener(button -> {
          this.minecraft.displayGuiScreen(CreateWorldScreen.func_243425_a(this.getScreen()));
        });

    ContainerComponent editButton = this.newDefaultStyleButton()
        .setRightMargin(5.7F)
        .addChild(newDefaultStyleButtonText("menu.play.world_list.button.edit"))
        .addActionListener(button -> {
          this.worldItemsStream(worldListContainer)
              .filter(WorldItemComponent::isSelected)
              .findAny()
              .ifPresent(WorldItemComponent::editWorld);
        });
    ContainerComponent deleteButton = this.newDefaultStyleButton()
        .setRightMargin(5.7F)
        .setBackgroundColour(new Colour(PlayComponent.RED))
        .addHoverAnimation(Component.BACKGROUND_COLOUR,
            RenderUtil.getColour4f(RenderUtil.getColour4i(PlayComponent.RED_HIGHLIGHTED)), 60F)
        .addChild(newDefaultStyleButtonText("menu.play.world_list.button.delete"))
        .addActionListener(button -> {
          this.worldItemsStream(worldListContainer)
              .filter(WorldItemComponent::isSelected)
              .findAny()
              .ifPresent(WorldItemComponent::deleteWorld);
        });
    ContainerComponent recreateButton = this.newDefaultStyleButton()
        .addChild(newDefaultStyleButtonText("menu.play.world_list.button.recreate"))
        .addActionListener(button -> {
          this.worldItemsStream(worldListContainer)
              .filter(WorldItemComponent::isSelected)
              .findAny()
              .ifPresent(WorldItemComponent::recreateWorld);
        });

    ContainerComponent controlsContainer = new ContainerComponent()
        .setFlexDirection(FlexDirection.COLUMN)
        .setBackgroundColour(new Colour(0x40121212))
        .setHeight(56F)
        .setAlignItems(Align.CENTER)
        .addChild(new ContainerComponent()
            .setFlexDirection(FlexDirection.ROW)
            .setAlignItems(Align.CENTER)
            .setWidth(220F)
            .setFlexShrink(1F)
            .setTopPadding(2F)
            .addChild(playButton)
            .addChild(createButton))
        .addChild(new ContainerComponent()
            .setFlexDirection(FlexDirection.ROW)
            .setAlignItems(Align.CENTER)
            .setWidth(220F)
            .setFlexShrink(1F)
            .setBottomPadding(2F)
            .addChild(deleteButton)
            .addChild(editButton)
            .addChild(recreateButton));

    this.addChild(worldListContainer);
    this.addChild(controlsContainer);
  }

  private ContainerComponent newDefaultStyleButton() {
    return new ContainerComponent()
        .setWidth(30F)
        .setHeight(20F)
        .setFlexGrow(1F)
        .setJustifyContent(Justify.CENTER)
        .setAlignItems(Align.CENTER)
        .setBackgroundColour(new Colour(PlayComponent.BLUE))
        .addHoverAnimation(Component.BACKGROUND_COLOUR,
            RenderUtil.getColour4f(RenderUtil.getColour4i(PlayComponent.BLUE_HIGHLIGHTED)), 60F);
  }

  private TextBlockComponent newDefaultStyleButtonText(String translationKey) {
    return new TextBlockComponent(new TranslationTextComponent(translationKey))
        .setShadow(false)
        .setTopMargin(1F);
  }

  private void resetSelection(ParentComponent<?> parent) {
    for (Component<?> child : parent.getChildrenComponents()) {
      if (child instanceof WorldItemComponent) {
        ((WorldItemComponent) child).removeSelect();
      }
    }
  }

  private void loadWorlds(ContainerComponent worldListContainer) {
    try {
      List<WorldSummary> saveList = this.minecraft.getSaveLoader().getSaveList();
      Collections.sort(saveList);
      for (WorldSummary worldSummary : saveList) {
        worldListContainer.addChild(new WorldItemComponent(worldSummary, this));
      }
    } catch (AnvilConverterException e) {
      LOGGER.error("Unable to load save list", e);
    }
  }

  public void reloadWorlds() {
    this.worldListContainer.clearChildren();
    this.loadWorlds(this.worldListContainer);
  }

  private Stream<WorldItemComponent> worldItemsStream(ParentComponent<?> worldItemsParent) {
    return worldItemsParent.getChildren().stream()
        .filter(child -> child.getComponent() instanceof WorldItemComponent)
        .map(child -> (WorldItemComponent) child.getComponent());
  }
}
