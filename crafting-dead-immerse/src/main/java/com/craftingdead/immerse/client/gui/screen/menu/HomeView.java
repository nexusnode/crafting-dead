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
import java.util.concurrent.TimeUnit;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;
import com.craftingdead.immerse.client.gui.view.Animation;
import com.craftingdead.immerse.client.gui.view.Color;
import com.craftingdead.immerse.client.gui.view.Overflow;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.ViewUtil;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Justify;
import com.craftingdead.immerse.client.gui.view.layout.yoga.PositionType;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;

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
            .setPadding(10)
            .setLeftMarginPercent(10.0F)
            .setOverflow(Overflow.SCROLL),
        new YogaLayoutParent())
            .configure(
                view -> view.getBackgroundColorProperty().setBaseValue(new Color(0x70777777)))
            .setBackgroundBlur(50.0F)
            .configure(view -> ViewUtil.addAll(view, new File("news.xml")));


    this.addChild(this.newsComponent);
  }

  @Override
  protected void added() {
    new Animator.Builder()
        .addTarget(Animation.forProperty(this.newsComponent.getXScaleProperty())
            .to(0.3F, 1.0F)
            .build())
        .addTarget(Animation.forProperty(this.newsComponent.getYScaleProperty())
            .to(0.3F, 1.0F)
            .build())
        .setDuration(250L, TimeUnit.MILLISECONDS)
        .build()
        .start();
  }

  @Override
  protected void queueRemoval(Runnable remove) {
    new Animator.Builder()
        .addTarget(Animation.forProperty(this.newsComponent.getXScaleProperty())
            .to(0.3F)
            .build())
        .addTarget(Animation.forProperty(this.newsComponent.getYScaleProperty())
            .to(0.3F)
            .build())
        .setDuration(250L, TimeUnit.MILLISECONDS)
        .addTarget(new TimingTargetAdapter() {
          @Override
          public void end(Animator source) {
            remove.run();
          }
        })
        .build()
        .start();
  }
}
