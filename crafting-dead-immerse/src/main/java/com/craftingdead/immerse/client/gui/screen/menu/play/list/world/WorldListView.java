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
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.immerse.client.gui.screen.Theme;
import com.craftingdead.immerse.client.gui.view.Color;
import com.craftingdead.immerse.client.gui.view.Overflow;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.States;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.Transition;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.ViewScreen;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Align;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Justify;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import com.craftingdead.immerse.sounds.ImmerseSoundEvents;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.storage.WorldSummary;

public class WorldListView<L extends Layout>
    extends ParentView<WorldListView<L>, L, YogaLayout> {

  private static final Logger logger = LogManager.getLogger();

  private final ParentView<?, YogaLayout, YogaLayout> listView;

  @Nullable
  private WorldItemView selectedItem;

  private final View<?, YogaLayout> playButton;
  private final View<?, YogaLayout> editButton;
  private final View<?, YogaLayout> deleteButton;
  private final View<?, YogaLayout> recreateButton;

  public WorldListView(L layout) {
    super(layout, new YogaLayoutParent().setFlexDirection(FlexDirection.COLUMN));

    this.listView = new ParentView<>(
        new YogaLayout()
            .setFlexBasis(1)
            .setFlexGrow(1)
            .setTopPadding(4F)
            .setBottomPadding(10F)
            .setOverflow(Overflow.SCROLL),
        new YogaLayoutParent()
            .setFlexDirection(FlexDirection.COLUMN)
            .setAlignItems(Align.CENTER));
    this.loadWorlds();

    this.playButton =
        createButton(Theme.GREEN, Theme.GREEN_HIGHLIGHTED,
            new TranslationTextComponent("view.world_list.button.play"),
            () -> this.getSelectedItem().ifPresent(WorldItemView::joinWorld))
                .configure(view -> view.getBackgroundColorProperty()
                    .registerState(Theme.GREEN_DISABLED, States.DISABLED))
                .configure(view -> view.getLayout().setMargin(3F))
                .setEnabled(false);

    View<?, YogaLayout> createButton =
        createButton(Theme.BLUE, Theme.BLUE_HIGHLIGHTED,
            new TranslationTextComponent("view.world_list.button.create"),
            () -> ((ViewScreen) this.getScreen())
                .keepOpenAndSetScreen(CreateWorldScreen.create((ViewScreen) this.getScreen())))
                    .configure(view -> view.getLayout().setMargin(3F));

    this.editButton =
        createButton(Theme.BLUE, Theme.BLUE_HIGHLIGHTED,
            new TranslationTextComponent("view.world_list.button.edit"),
            () -> this.getSelectedItem().ifPresent(WorldItemView::editWorld))
                .setDisabledBackgroundColor(Theme.BLUE_DISABLED)
                .configure(view -> view.getLayout().setMargin(3))
                .setEnabled(false);

    this.deleteButton =
        createButton(Theme.RED, Theme.RED_HIGHLIGHTED,
            new TranslationTextComponent("view.world_list.button.delete"),
            () -> this.getSelectedItem().ifPresent(WorldItemView::deleteWorld))
                .setDisabledBackgroundColor(Theme.RED_DISABLED)
                .configure(view -> view.getLayout().setMargin(3))
                .setEnabled(false);

    this.recreateButton =
        createButton(Theme.BLUE, Theme.BLUE_HIGHLIGHTED,
            new TranslationTextComponent("view.world_list.button.recreate"),
            () -> this.getSelectedItem().ifPresent(WorldItemView::recreateWorld))
                .setDisabledBackgroundColor(Theme.BLUE_DISABLED)
                .configure(view -> view.getLayout().setMargin(3))
                .setEnabled(false);

    ParentView<?, YogaLayout, YogaLayout> controlsContainer =
        new ParentView<>(
            new YogaLayout().setHeight(56),
            new YogaLayoutParent()
                .setJustifyContent(Justify.CENTER)
                .setAlignItems(Align.CENTER)
                .setFlexDirection(FlexDirection.COLUMN))
                    .configure(view -> view.getBackgroundColorProperty()
                        .setBaseValue(new Color(0x40121212)))
                    .addChild(new ParentView<>(
                        new YogaLayout()
                            .setFlex(1)
                            .setWidth(220F),
                        new YogaLayoutParent()
                            .setFlexDirection(FlexDirection.ROW)
                            .setAlignItems(Align.CENTER))
                                .addChild(this.playButton)
                                .addChild(createButton))
                    .addChild(new ParentView<>(
                        new YogaLayout()
                            .setFlex(1)
                            .setWidth(220F),
                        new YogaLayoutParent()
                            .setFlexDirection(FlexDirection.ROW)
                            .setAlignItems(Align.CENTER))
                                .addChild(this.deleteButton)
                                .addChild(this.editButton)
                                .addChild(this.recreateButton));

    this.addChild(this.listView);
    this.addChild(controlsContainer);

  }

  private static ParentView<?, YogaLayout, YogaLayout> createButton(Color color,
      Color hoveredColor, ITextComponent text, Runnable actionListener) {
    return new ParentView<>(
        new YogaLayout()
            .setWidth(30F)
            .setHeight(20F)
            .setFlex(1F),
        new YogaLayoutParent()
            .setJustifyContent(Justify.CENTER)
            .setAlignItems(Align.CENTER))
                .addActionSound(ImmerseSoundEvents.BUTTON_CLICK.get())
                .configure(view -> view.getBackgroundColorProperty()
                    .setBaseValue(color)
                    .registerState(hoveredColor, States.HOVERED, States.ENABLED)
                    .setTransition(Transition.linear(150L)))
                .setFocusable(true)
                .addListener(ActionEvent.class, (c, e) -> actionListener.run())
                .addChild(new TextView<>(new YogaLayout().setTopMargin(1F), text)
                    .setShadow(false)
                    .setCentered(true));
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
        .filter(WorldItemView::isSelected)
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
      List<WorldSummary> saveList = this.minecraft.getLevelSource().getLevelList();
      Collections.sort(saveList);
      for (WorldSummary worldSummary : saveList) {
        this.listView.addChild(new WorldItemView(worldSummary, this));
      }
    } catch (AnvilConverterException e) {
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
