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

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.KeyFrames;
import com.craftingdead.immerse.client.gui.screen.Theme;
import com.craftingdead.immerse.client.gui.view.Animation;
import com.craftingdead.immerse.client.gui.view.Color;
import com.craftingdead.immerse.client.gui.view.Overflow;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.States;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Align;
import com.craftingdead.immerse.client.gui.view.layout.yoga.FlexDirection;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Justify;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import net.minecraft.util.text.TranslationTextComponent;

public class ServerListView<L extends Layout>
    extends ParentView<ServerListView<L>, L, YogaLayout> {

  protected final ServerList serverProvider;

  protected final ParentView<?, YogaLayout, YogaLayout> listView;

  protected final ParentView<?, YogaLayout, YogaLayout> controlsView;

  @Nullable
  private ServerItemView selectedItem;

  private final View<?, YogaLayout> playButton;

  @Nullable
  private CompletableFuture<Collection<ServerEntry>> refreshFuture;

  public ServerListView(L layout, ServerList serverEntryProvider) {
    super(layout, new YogaLayoutParent().setFlexDirection(FlexDirection.COLUMN));
    this.serverProvider = serverEntryProvider;


    this.listView = new ParentView<>(
        new YogaLayout()
            .setFlex(1)
            .setBottomPadding(7F)
            .setOverflow(Overflow.SCROLL),
        new YogaLayoutParent()
            .setFlexDirection(FlexDirection.COLUMN)
            .setAlignItems(Align.CENTER));
    this.addChild(this.listView);

    this.playButton = Theme.createGreenButton(
        new TranslationTextComponent("view.server_list.button.play"),
        () -> this.getSelectedItem().ifPresent(ServerItemView::connect))
        .configure(view -> view.getBackgroundColorProperty()
            .defineState(Theme.GREEN_DISABLED, States.DISABLED))
        .setEnabled(false);

    this.controlsView = new ParentView<>(
        new YogaLayout().setHeight(56),
        new YogaLayoutParent()
            .setJustifyContent(Justify.CENTER)
            .setAlignItems(Align.CENTER)
            .setFlexDirection(FlexDirection.COLUMN))
                .configure(
                    view -> view.getBackgroundColorProperty().setBaseValue(new Color(0x40121212)))
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
                .addChild(Theme.createBlueButton(
                    new TranslationTextComponent("view.server_list.button.quick_refresh"),
                    this::quickRefresh).configure(view -> view.getLayout().setMargin(3)))
                .addChild(Theme.createBlueButton(
                    new TranslationTextComponent("view.server_list.button.refresh"),
                    this::refresh).configure(view -> view.getLayout().setMargin(3)));
  }

  @Override
  protected void added() {
    int delay = 0;
    for (View<?, ?> view : this.listView.getChildViews()) {
      new Animator.Builder()
          .addTarget(Animation.forProperty(view.getXTranslationProperty())
              .keyFrames(new KeyFrames.Builder<>(-100.0F)
                  .addFrame(0.0F)
                  .build())
              .build())
          .addTarget(Animation.forProperty(view.getAlphaProperty())
              .keyFrames(new KeyFrames.Builder<>(0.0F)
                  .addFrame(1.0F)
                  .build())
              .build())
          .setStartDelay(delay, TimeUnit.MILLISECONDS)
          .setDuration(200L, TimeUnit.MILLISECONDS)
          .build()
          .start();
      delay += 100;
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
        .filter(child -> child instanceof ServerItemView)
        .map(child -> (ServerItemView) child)
        .filter(View::isSelected)
        .findAny()
        .orElse(null);

    this.playButton.setEnabled(this.selectedItem != null);
  }

  public Optional<ServerItemView> getSelectedItem() {
    return Optional.ofNullable(this.selectedItem);
  }

  private void refresh() {
    if (this.refreshFuture == null || this.refreshFuture.cancel(false)) {
      this.listView.clearChildren();
      this.selectedItem = null;
      this.updateSelected();
      this.serverProvider.load()
          .thenAcceptAsync(servers -> servers.forEach(this::addServer), this.minecraft);
    }
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
