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

package com.craftingdead.immerse.client.gui.menu;

import java.io.File;
import com.craftingdead.immerse.client.gui.component.Colour;
import com.craftingdead.immerse.client.gui.component.Component;
import com.craftingdead.immerse.client.gui.component.ComponentUtil;
import com.craftingdead.immerse.client.gui.component.ParentComponent;
import com.craftingdead.immerse.client.gui.component.type.Justify;
import com.craftingdead.immerse.client.gui.component.type.Overflow;
import io.noties.tumbleweed.Timeline;
import io.noties.tumbleweed.Tween;
import io.noties.tumbleweed.TweenCallback;
import io.noties.tumbleweed.equations.Expo;

public class HomeComponent extends ParentComponent<HomeComponent> {

  private final Component<?> newsComponent;

  public HomeComponent() {
    this.setJustifyContent(Justify.SPACE_AROUND);
    this.newsComponent = new ParentComponent<>()
        .setWidthPercent(45.0F)
        .setHeightPercent(75.0F)
        .setBackgroundColour(new Colour(0x70777777))
        .setBackgroundBlur(50.0F)
        .setPadding(10.0F)
        .setLeftMarginPercent(10.0F)
        .setOverflow(Overflow.SCROLL)
        .configure(c -> ComponentUtil.addAll(c, new File("news.xml")));
    this.addChild(this.newsComponent);
  }

  @Override
  protected void added() {
    this.newsComponent.setScale(0.3F);
    Timeline.createParallel(1000.0F)
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
    Timeline.createParallel(800.0F)
        .push(Tween.to(this.newsComponent, X_SCALE)
            .ease(Expo.IN)
            .target(0.1F))
        .push(Tween.to(this.newsComponent, Y_SCALE)
            .ease(Expo.IN)
            .target(0.1F))
        .addCallback(TweenCallback.COMPLETE, (type, source) -> remove.run())
        .start(this.getTweenManager());
  }
}
