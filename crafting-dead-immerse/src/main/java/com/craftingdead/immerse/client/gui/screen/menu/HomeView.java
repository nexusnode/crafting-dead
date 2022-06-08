/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.screen.menu;

import java.io.File;
import java.util.concurrent.TimeUnit;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;
import sm0keysa1m0n.bliss.Animation;
import sm0keysa1m0n.bliss.view.ParentView;
import sm0keysa1m0n.bliss.view.ViewUtil;

public class HomeView extends ParentView implements TransitionView {

  private final ParentView newsComponent;

  private final Animator entranceAnimator;
  private final Animator exitAnimator;
  private Runnable removeCallback;

  public HomeView() {
    super(new Properties());

    this.newsComponent = new ParentView(new Properties().id("news").styleClasses("blur"));
    ViewUtil.addAll(this.newsComponent, new File("news.xml"));

    this.addChild(this.newsComponent);

    this.entranceAnimator = new Animator.Builder()
        .addTarget(Animation.forProperty(this.newsComponent.getStyle().xScale)
            .to(0.3F, 1.0F)
            .build())
        .addTarget(Animation.forProperty(this.newsComponent.getStyle().yScale)
            .to(0.3F, 1.0F)
            .build())
        .setDuration(250L, TimeUnit.MILLISECONDS)
        .build();
    this.exitAnimator = new Animator.Builder()
        .addTarget(Animation.forProperty(this.newsComponent.getStyle().xScale)
            .to(0.3F)
            .build())
        .addTarget(Animation.forProperty(this.newsComponent.getStyle().yScale)
            .to(0.3F)
            .build())
        .setDuration(250L, TimeUnit.MILLISECONDS)
        .addTarget(new TimingTargetAdapter() {
          @Override
          public void end(Animator source) {
            HomeView.this.removeCallback.run();
          }
        })
        .build();
  }

  @Override
  protected void added() {
    super.added();
    this.entranceAnimator.start();
  }

  @Override
  protected void removed() {
    super.removed();
    this.entranceAnimator.stop();
    this.exitAnimator.stop();
  }

  @Override
  public void transitionOut(Runnable removeCallback) {
    this.removeCallback = removeCallback;
    this.exitAnimator.start();
  }
}
