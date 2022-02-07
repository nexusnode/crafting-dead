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
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.ViewUtil;

public class HomeView extends ParentView {

  private final ParentView newsComponent;

  public HomeView() {
    super(new Properties<>());

    this.newsComponent = new ParentView(new Properties<>().id("news").backgroundBlur(50.0F));
    ViewUtil.addAll(this.newsComponent, new File("news.xml"));

    this.addChild(this.newsComponent);
  }

  @Override
  protected void added() {
    super.added();
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
