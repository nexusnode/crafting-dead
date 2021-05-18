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

package com.craftingdead.immerse.client.gui.screen.menu;

import java.io.File;
import com.craftingdead.immerse.client.gui.view.Colour;
import com.craftingdead.immerse.client.gui.view.Overflow;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.ViewUtil;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Justify;
import com.craftingdead.immerse.client.gui.view.layout.yoga.PositionType;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import io.noties.tumbleweed.Timeline;
import io.noties.tumbleweed.Tween;
import io.noties.tumbleweed.TweenCallback;
import io.noties.tumbleweed.equations.Expo;
import io.noties.tumbleweed.equations.Sine;

public class HomeView extends ParentView<HomeView, YogaLayout, YogaLayout> {

  private final View<?, YogaLayout> newsComponent;

  public HomeView() {
    super(
        new YogaLayout()
            .setWidthPercent(100)
            .setHeightPercent(100)
            .setPositionType(PositionType.ABSOLUTE),
        new YogaLayoutParent()
            .setJustifyContent(Justify.SPACE_AROUND));

    this.newsComponent = new ParentView<>(
        new YogaLayout()
            .setWidthPercent(45.0F)
            .setHeightPercent(75.0F)
            .setPadding(10.0F)
            .setLeftMarginPercent(10.0F),
        new YogaLayoutParent())
            .setBackgroundColour(new Colour(0x70777777))
            .setBackgroundBlur(50.0F)
            .setOverflow(Overflow.SCROLL)
            .configure(c -> ViewUtil.addAll(c, new File("news.xml")));
    this.addChild(this.newsComponent);
  }

  @Override
  protected void added() {
    this.newsComponent.setScale(0.3F);
    Timeline.createParallel(800.0F)
        .push(Tween.to(this.newsComponent, X_SCALE)
            .ease(Expo.OUT)
            .target(1.0F))
        .push(Tween.to(this.newsComponent, Y_SCALE)
            .ease(Expo.OUT)
            .target(1.0F))
        .start(this.getTweenManager());
  }

  @Override
  protected void removed(Runnable remove) {
    Timeline.createParallel(600.0F)
        .push(Tween.to(this.newsComponent, X_SCALE)
            .ease(Sine.OUT)
            .target(0.1F))
        .push(Tween.to(this.newsComponent, Y_SCALE)
            .ease(Sine.OUT)
            .target(0.1F))
        .addCallback(TweenCallback.COMPLETE, (type, source) -> remove.run())
        .start(this.getTweenManager());
  }
}
