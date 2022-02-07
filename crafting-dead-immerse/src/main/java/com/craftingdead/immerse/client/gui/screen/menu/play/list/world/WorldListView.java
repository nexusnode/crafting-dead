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
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.KeyFrames;
import com.craftingdead.immerse.client.gui.screen.Theme;
import com.craftingdead.immerse.client.gui.view.Animation;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.View;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.storage.LevelStorageException;
import net.minecraft.world.level.storage.LevelSummary;

public class WorldListView extends ParentView {

  private static final Logger logger = LogManager.getLogger();

  private final ParentView listView;

  @Nullable
  private WorldItemView selectedItem;

  private final View playButton;
  private final View editButton;
  private final View deleteButton;
  private final View recreateButton;

  public WorldListView() {
    super(new Properties<>());

    this.listView = new ParentView(new Properties<>().id("content"));
    this.loadWorlds();

    this.playButton = Theme.createGreenButton(
        new TranslatableComponent("view.world_list.button.play"),
        () -> this.getSelectedItem().ifPresent(WorldItemView::joinWorld));
    this.playButton.setEnabled(false);

    var createButton = Theme.createBlueButton(
        new TranslatableComponent("view.world_list.button.create"),
        () -> this.getScreen()
            .keepOpenAndSetScreen(CreateWorldScreen.create(this.getScreen())));

    this.editButton = Theme.createBlueButton(
        new TranslatableComponent("view.world_list.button.edit"),
        () -> this.getSelectedItem().ifPresent(WorldItemView::editWorld));
    this.editButton.setEnabled(false);

    this.deleteButton = Theme.createRedButton(
        new TranslatableComponent("view.world_list.button.delete"),
        () -> this.getSelectedItem().ifPresent(WorldItemView::deleteWorld));
    this.deleteButton.setEnabled(false);

    this.recreateButton = Theme.createBlueButton(
        new TranslatableComponent("view.world_list.button.recreate"),
        () -> this.getSelectedItem().ifPresent(WorldItemView::recreateWorld));
    this.recreateButton.setEnabled(false);

    var controls = new ParentView(new Properties<>().id("controls"));

    var firstRow = new ParentView(new Properties<>());
    firstRow.addChild(this.playButton);
    firstRow.addChild(createButton);

    var secondRow = new ParentView(new Properties<>());
    secondRow.addChild(this.deleteButton);
    secondRow.addChild(this.editButton);
    secondRow.addChild(this.recreateButton);

    controls.addChild(firstRow);
    controls.addChild(secondRow);

    this.addChild(this.listView);
    this.addChild(controls);

  }

  @Override
  protected void added() {
    super.added();
    int delay = 0;
    for (var view : this.listView.getChildViews()) {
      new Animator.Builder()
          .addTarget(Animation.forProperty(view.getAlphaProperty())
              .keyFrames(new KeyFrames.Builder<>(0.0F)
                  .addFrame(1.0F)
                  .build())
              .build())
          .setStartDelay(delay, TimeUnit.MILLISECONDS)
          .setDuration(250L, TimeUnit.MILLISECONDS)
          .build()
          .start();
      delay += 150;
    }
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    boolean result = super.mouseClicked(mouseX, mouseY, button);
    this.updateSelected();
    return result;
  }

  protected void updateSelected() {
    this.selectedItem = this.listView.getChildViews().stream()
        .filter(child -> child instanceof WorldItemView)
        .map(child -> (WorldItemView) child)
        .filter(WorldItemView::isFocused)
        .findAny()
        .orElse(null);

    boolean enabled = this.selectedItem != null;
    this.playButton.setEnabled(enabled);
    this.editButton.setEnabled(enabled);
    this.deleteButton.setEnabled(enabled);
    this.recreateButton.setEnabled(enabled);
  }

  private void loadWorlds() {
    try {
      List<LevelSummary> saveList = this.minecraft.getLevelSource().getLevelList();
      Collections.sort(saveList);
      for (LevelSummary worldSummary : saveList) {
        this.listView.addChild(new WorldItemView(worldSummary, this));
      }
    } catch (LevelStorageException e) {
      logger.error("Unable to load save list", e);
    }
  }

  public void reloadWorlds() {
    this.listView.clearChildren();
    this.selectedItem = null;
    this.updateSelected();
    this.loadWorlds();
  }

  private Optional<WorldItemView> getSelectedItem() {
    return Optional.ofNullable(this.selectedItem);
  }
}
