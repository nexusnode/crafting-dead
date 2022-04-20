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

package com.craftingdead.immerse.client.gui.screen;

import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.core.animation.timing.KeyFrames;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.client.gui.view.Animation;
import com.craftingdead.immerse.client.gui.view.Color;
import com.craftingdead.immerse.client.gui.view.ImageView;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.View.Properties;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.sounds.ImmerseSoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class Theme {

  public static final String RED_BUTTON_CLASS = "red-button";
  public static final String GREEN_BUTTON_CLASS = "green-button";
  public static final String BLUE_BUTTON_CLASS = "blue-button";

  public static final Color ONLINE = new Color(0xFF90ba3c);
  public static final Color OFFLINE = Color.GRAY;

  public static View newSeparator() {
    return new View(new Properties<>().styleClasses("seperator").unscaleHeight(true));
  }

  public static View createRedButton(Component text, Runnable actionListener) {
    return createRedButton(text, actionListener, null);
  }

  public static View createRedButton(Component text, Runnable actionListener, @Nullable String id) {
    return createButton(text, actionListener,
        new ParentView.Properties<>().id(id).styleClasses(RED_BUTTON_CLASS));
  }

  public static View createGreenButton(Component text, Runnable actionListener) {
    return createGreenButton(text, actionListener, null);
  }

  public static View createGreenButton(Component text, Runnable actionListener,
      @Nullable String id) {
    return createButton(text, actionListener,
        new ParentView.Properties<>().id(id).styleClasses(GREEN_BUTTON_CLASS));
  }

  public static View createBlueButton(Component text, Runnable actionListener) {
    return createBlueButton(text, actionListener, null);
  }

  public static View createBlueButton(Component text, Runnable actionListener,
      @Nullable String id) {
    return createButton(text, actionListener,
        new ParentView.Properties<>().id(id).styleClasses(BLUE_BUTTON_CLASS));
  }

  public static View createButton(Component text, Runnable actionListener,
      ParentView.Properties<?> properties) {
    var view = new ParentView(properties.styleClasses("button").focusable(true));

    view.addChild(new TextView(new TextView.Properties<>().id("label"))
        .setText(text)
        .setShadow(false)
        .setCentered(true));

    view.addActionSound(ImmerseSoundEvents.BUTTON_CLICK.get());
    view.addListener(ActionEvent.class, event -> actionListener.run());

    return view;
  }

  public static ImageView createImageButton(ResourceLocation image, Runnable actionListener,
      ParentView.Properties<?> properties) {
    var view = new ImageView(properties.styleClasses("image-button").focusable(true));
    view.setImage(image);
    view.addActionSound(ImmerseSoundEvents.BUTTON_CLICK.get());
    view.addHoverSound(ImmerseSoundEvents.MAIN_MENU_HOVER.get());
    view.addListener(ActionEvent.class, event -> actionListener.run());
    return view;
  }

  public static View createBackground() {
    var view = new ImageView(new View.Properties<>().styleClasses("background"))
        .setImage(new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/background.png"))
        .setBilinearFiltering(true);

    new Animator.Builder()
        .addTarget(Animation.forProperty(view.getXTranslationProperty())
            .keyFrames(new KeyFrames.Builder<Float>()
                .addFrames(1.0F, -3.0F, 5.0F, 15.0F)
                .build())
            .build())
        .addTarget(Animation.forProperty(view.getYTranslationProperty())
            .keyFrames(new KeyFrames.Builder<Float>()
                .addFrames(10.0F, 5.0F, 1.0F, 3.0F, 5.0F, 1.0F, -10.0F, -7.0F, -5.0F)
                .build())
            .build())
        .addTarget(Animation.forProperty(view.getXScaleProperty())
            .keyFrames(new KeyFrames.Builder<Float>()
                .addFrames(1.3F, 1.2F, 1.25F, 1.15F)
                .build())
            .build())
        .addTarget(Animation.forProperty(view.getYScaleProperty())
            .keyFrames(new KeyFrames.Builder<Float>()
                .addFrames(1.3F, 1.2F, 1.25F, 1.15F)
                .build())
            .build())
        .setInterpolator(new SplineInterpolator(0.25, 0.1, 0.25, 1))
        .setDuration(20L, TimeUnit.SECONDS)
        .setRepeatCount(Animator.INFINITE)
        .setRepeatBehavior(RepeatBehavior.REVERSE)
        .build()
        .start();
    return view;
  }
}
