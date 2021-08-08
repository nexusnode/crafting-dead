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

import java.util.Optional;
import javax.annotation.Nullable;
import com.craftingdead.immerse.client.gui.screen.Theme;
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
import com.craftingdead.immerse.sounds.ImmerseSoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ServerListView<L extends Layout>
    extends ParentView<ServerListView<L>, L, YogaLayout> {

  protected final ServerProvider serverProvider;

  protected final ParentView<?, YogaLayout, YogaLayout> listView;

  protected final ParentView<?, YogaLayout, YogaLayout> controlsView;

  @Nullable
  private ServerItemView selectedItem;

  private final View<?, YogaLayout> playButton;

  public ServerListView(L layout, ServerProvider serverEntryProvider) {
    super(layout, new YogaLayoutParent().setFlexDirection(FlexDirection.COLUMN));
    this.serverProvider = serverEntryProvider;

    this.listView = new ParentView<>(
        new YogaLayout()
            .setBottomPadding(7F)
            .setHeight(60F)
            .setFlexGrow(1F),
        new YogaLayoutParent()
            .setFlexDirection(FlexDirection.COLUMN)
            .setAlignItems(Align.CENTER))
                .setOverflow(Overflow.SCROLL)
                .addChild(this.firstTableRow());
    this.addChild(this.listView);

    this.playButton =
        createButton(new Colour(Theme.GREEN), new Colour(Theme.GREEN_HIGHLIGHTED),
            new TranslationTextComponent("view.server_list.button.play"),
            () -> this.getSelectedItem().ifPresent(ServerItemView::connect))
                .setDisabledBackgroundColour(new Colour(Theme.GREEN_DISABLED), 150F)
                .setEnabled(false);

    this.controlsView = new ParentView<>(
        new YogaLayout().setHeight(56),
        new YogaLayoutParent()
            .setJustifyContent(Justify.CENTER)
            .setAlignItems(Align.CENTER)
            .setFlexDirection(FlexDirection.COLUMN))
                .setBackgroundColour(new Colour(0x40121212))
                .addChild(this.createTopRowControls())
                .addChild(this.createBottomRowControls());

    this.addChild(this.controlsView);

    this.refresh();
  }

  protected ParentView<?, YogaLayout, YogaLayout> createTopRowControls() {
    return new ParentView<>(
        new YogaLayout()
            .setFlex(1)
            .setWidth(220F),
        new YogaLayoutParent()
            .setFlexDirection(FlexDirection.ROW)
            .setAlignItems(Align.CENTER))
                .addChild(this.playButton.configure(view -> view.getLayout().setMargin(3)));
  }

  protected ParentView<?, YogaLayout, YogaLayout> createBottomRowControls() {
    return new ParentView<>(
        new YogaLayout()
            .setFlex(1)
            .setWidth(220F),
        new YogaLayoutParent()
            .setFlexDirection(FlexDirection.ROW)
            .setAlignItems(Align.CENTER))
                .addChild(createButton(new Colour(Theme.BLUE),
                    new Colour(Theme.BLUE_HIGHLIGHTED), new TranslationTextComponent(
                        "view.server_list.button.quick_refresh"),
                    this::quickRefresh)
                        .configure(view -> view.getLayout().setMargin(3)))
                .addChild(createButton(new Colour(Theme.BLUE),
                    new Colour(Theme.BLUE_HIGHLIGHTED), new TranslationTextComponent(
                        "view.server_list.button.refresh"),
                    this::refresh).configure(view -> view.getLayout().setMargin(3)));
  }

  protected static View<?, YogaLayout> createButton(Colour colour, Colour hoveredColour,
      ITextComponent text, Runnable actionListener) {
    return new ParentView<>(
        new YogaLayout()
            .setWidth(30F)
            .setHeight(20F)
            .setFlex(1F),
        new YogaLayoutParent()
            .setJustifyContent(Justify.CENTER)
            .setAlignItems(Align.CENTER))
                .addChild(new TextView<>(new YogaLayout().setHeight(8F), text)
                    .setShadow(false)
                    .setCentered(true))
                .setBackgroundColour(colour)
                .addBackgroundHoverAnimation(hoveredColour, 150F)
                .addActionSound(ImmerseSoundEvents.BUTTON_CLICK.get())
                .setFocusable(true)
                .addListener(ActionEvent.class, (c, e) -> actionListener.run());
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
                    new TranslationTextComponent("view.server_list.motd"))
                        .setShadow(false)
                        .setCentered(true))
                .addChild(new TextView<>(new YogaLayout().setFlex(1).setHeight(8),
                    new TranslationTextComponent("view.server_list.map"))
                        .setShadow(false)
                        .setCentered(true))
                .addChild(new TextView<>(
                    new YogaLayout().setWidth(60F).setHeight(8).setLeftMargin(10F),
                    new TranslationTextComponent("view.server_list.ping"))
                        .setShadow(false)
                        .setCentered(true))
                .addChild(new TextView<>(new YogaLayout()
                    .setWidth(60F)
                    .setHeight(8)
                    .setLeftMargin(10F),
                    new TranslationTextComponent("view.server_list.players"))
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
        .filter(child -> child instanceof ServerItemView)
        .map(child -> (ServerItemView) child)
        .filter(ServerItemView::isSelected)
        .findAny()
        .orElse(null);

    this.playButton.setEnabled(this.selectedItem != null);
  }

  public Optional<ServerItemView> getSelectedItem() {
    return Optional.ofNullable(this.selectedItem);
  }

  private void refresh() {
    this.listView.clearChildren();
    this.selectedItem = null;
    this.updateSelected();
    this.serverProvider.read(this::addServer);
  }

  private void quickRefresh() {
    for (View<?, ?> child : this.listView.getChildViews()) {
      if (child instanceof ServerItemView) {
        ((ServerItemView) child).ping();
      }
    }
  }

  protected void addServer(ServerEntry entry) {
    this.listView.addChild(new ServerItemView(entry));
    this.listView.layout();
  }
}
