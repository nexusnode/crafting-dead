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
import com.craftingdead.immerse.client.gui.view.Transition;
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
        new ParentView.Properties<>().styleClasses(RED_BUTTON_CLASS));
  }

  public static View createGreenButton(Component text, Runnable actionListener) {
    return createGreenButton(text, actionListener, null);
  }

  public static View createGreenButton(Component text, Runnable actionListener,
      @Nullable String id) {
    return createButton(text, actionListener,
        new ParentView.Properties<>().styleClasses(GREEN_BUTTON_CLASS));
  }

  public static View createBlueButton(Component text, Runnable actionListener) {
    return createBlueButton(text, actionListener, null);
  }

  public static View createBlueButton(Component text, Runnable actionListener,
      @Nullable String id) {
    return createButton(text, actionListener,
        new ParentView.Properties<>().styleClasses(BLUE_BUTTON_CLASS));
  }

  public static View createButton(Component text, Runnable actionListener,
      ParentView.Properties<?> properties) {
    var view = new ParentView(properties.styleClasses("button")) {
      @Override
      protected boolean isFocusable() {
        return true;
      }
    };

    view.addChild(new TextView(new TextView.Properties<>().id("label"))
        .setText(text)
        .setShadow(false)
        .setCentered(true));

    view.getBackgroundColorProperty().setTransition(Transition.linear(100L));

    view.addActionSound(ImmerseSoundEvents.BUTTON_CLICK.get());
    view.addListener(ActionEvent.class, event -> actionListener.run());

    return view;
  }

  public static ImageView createImageButton(ResourceLocation image, Runnable actionListener,
      ParentView.Properties<?> properties) {
    var view = new ImageView(properties) {
      @Override
      protected boolean isFocusable() {
        return true;
      }
    };
    view.setImage(image);
    view.addActionSound(ImmerseSoundEvents.BUTTON_CLICK.get());
    view.addHoverSound(ImmerseSoundEvents.MAIN_MENU_HOVER.get());
    view.addListener(ActionEvent.class, event -> actionListener.run());
    view.getColorProperty().setTransition(Transition.linear(150L));
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
