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
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.Animator.RepeatBehavior;
import org.jdesktop.core.animation.timing.KeyFrames;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.sounds.ImmerseSoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import sm0keysa1m0n.bliss.Animation;
import sm0keysa1m0n.bliss.Color;
import sm0keysa1m0n.bliss.minecraft.MinecraftUtil;
import sm0keysa1m0n.bliss.view.ImageView;
import sm0keysa1m0n.bliss.view.ParentView;
import sm0keysa1m0n.bliss.view.TextView;
import sm0keysa1m0n.bliss.view.View;
import sm0keysa1m0n.bliss.view.View.Properties;
import sm0keysa1m0n.bliss.view.event.ActionEvent;
import sm0keysa1m0n.bliss.view.event.AddedEvent;
import sm0keysa1m0n.bliss.view.event.RemovedEvent;

public class Theme {

  public static final String RED_BUTTON_CLASS = "red-button";
  public static final String GREEN_BUTTON_CLASS = "green-button";
  public static final String BLUE_BUTTON_CLASS = "blue-button";

  public static final Color ONLINE = Color.create(0xFF90BA3C);
  public static final Color OFFLINE = Color.GRAY;

  public static View newSeparator() {
    return new View(new Properties().styleClasses("seperator").unscaleHeight(true));
  }

  public static View createRedButton(Component text, Runnable actionListener) {
    return createRedButton(text, actionListener, null);
  }

  public static View createRedButton(Component text, Runnable actionListener, @Nullable String id) {
    return createButton(text, actionListener,
        new ParentView.Properties().id(id).styleClasses(RED_BUTTON_CLASS));
  }

  public static View createGreenButton(Component text, Runnable actionListener) {
    return createGreenButton(text, actionListener, null);
  }

  public static View createGreenButton(Component text, Runnable actionListener,
      @Nullable String id) {
    return createButton(text, actionListener,
        new ParentView.Properties().id(id).styleClasses(GREEN_BUTTON_CLASS));
  }

  public static View createBlueButton(Component text, Runnable actionListener) {
    return createBlueButton(text, actionListener, null);
  }

  public static View createBlueButton(Component text, Runnable actionListener,
      @Nullable String id) {
    return createButton(text, actionListener,
        new ParentView.Properties().id(id).styleClasses(BLUE_BUTTON_CLASS));
  }

  public static View createButton(Component text, Runnable actionListener,
      View.Properties properties) {
    var view = new ParentView(properties.styleClasses("button").focusable(true));

    view.addChild(new TextView(new View.Properties().id("label")).setText(text));

    view.addActionSound(ImmerseSoundEvents.BUTTON_CLICK.get());
    view.addListener(ActionEvent.class, event -> actionListener.run());

    return view;
  }

  public static ImageView createImageButton(ResourceLocation image, Runnable actionListener,
      View.Properties properties) {
    var view = new ImageView(properties.styleClasses("image-button").focusable(true));
    view.setImage(MinecraftUtil.createImageAccess(image));
    view.addActionSound(ImmerseSoundEvents.BUTTON_CLICK.get());
    view.addHoverSound(ImmerseSoundEvents.MAIN_MENU_HOVER.get());
    view.addListener(ActionEvent.class, event -> actionListener.run());
    return view;
  }

  public static View createBackground() {
    var view = new ImageView(new View.Properties().styleClasses("background"))
        .setImage(MinecraftUtil.createImageAccess(
            new ResourceLocation(CraftingDeadImmerse.ID, "textures/gui/background.png")));

    var animator = new Animator.Builder()
        .addTarget(Animation.forProperty(view.getStyle().xTranslation)
            .keyFrames(new KeyFrames.Builder<Float>()
                .addFrames(1.0F, -3.0F, 5.0F, 15.0F)
                .build())
            .build())
        .addTarget(Animation.forProperty(view.getStyle().yTranslation)
            .keyFrames(new KeyFrames.Builder<Float>()
                .addFrames(10.0F, 5.0F, 1.0F, 3.0F, 5.0F, 1.0F, -10.0F, -7.0F, -5.0F)
                .build())
            .build())
        .addTarget(Animation.forProperty(view.getStyle().xScale)
            .keyFrames(new KeyFrames.Builder<Float>()
                .addFrames(1.3F, 1.2F, 1.25F, 1.15F)
                .build())
            .build())
        .addTarget(Animation.forProperty(view.getStyle().yScale)
            .keyFrames(new KeyFrames.Builder<Float>()
                .addFrames(1.3F, 1.2F, 1.25F, 1.15F)
                .build())
            .build())
        .setInterpolator(new SplineInterpolator(0.25, 0.1, 0.25, 1))
        .setDuration(20L, TimeUnit.SECONDS)
        .setRepeatCount(Animator.INFINITE)
        .setRepeatBehavior(RepeatBehavior.REVERSE)
        .build();

    view.addListener(AddedEvent.class, __ -> animator.start());
    view.addListener(RemovedEvent.class, __ -> animator.stop());

    return view;
  }
}
