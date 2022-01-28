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

import com.craftingdead.immerse.client.gui.view.Color;
import com.craftingdead.immerse.client.gui.view.ParentView;
import com.craftingdead.immerse.client.gui.view.States;
import com.craftingdead.immerse.client.gui.view.TextView;
import com.craftingdead.immerse.client.gui.view.Transition;
import com.craftingdead.immerse.client.gui.view.View;
import com.craftingdead.immerse.client.gui.view.event.ActionEvent;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Align;
import com.craftingdead.immerse.client.gui.view.layout.yoga.Justify;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayout;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;
import com.craftingdead.immerse.sounds.ImmerseSoundEvents;
import net.minecraft.util.text.ITextComponent;

public class Theme {

  public static final Color RED_DISABLED = new Color(0x3394434b);
  public static final Color RED = new Color(0x66ff7583);
  public static final Color RED_HIGHLIGHTED = new Color(0x66ff8c98);
  public static final Color GREEN_DISABLED = new Color(0x3330916e);
  public static final Color GREEN = new Color(0x6652F2B7);
  public static final Color GREEN_HIGHLIGHTED = new Color(0x6692F0CE);
  public static final Color BLUE_DISABLED = new Color(0x330761b0);
  public static final Color BLUE = new Color(0x6674b9f7);
  public static final Color BLUE_HIGHLIGHTED = new Color(0x6691cbff);

  public static final Color ONLINE = new Color(0xFF90ba3c);
  public static final Color OFFLINE = Color.GRAY;

  public static View<?, YogaLayout> createRedButton(ITextComponent text, Runnable actionListener) {
    return createButton(RED, RED_HIGHLIGHTED, text, actionListener);
  }

  public static View<?, YogaLayout> createGreenButton(ITextComponent text,
      Runnable actionListener) {
    return createButton(GREEN, GREEN_HIGHLIGHTED, text, actionListener);
  }

  public static View<?, YogaLayout> createBlueButton(ITextComponent text, Runnable actionListener) {
    return createButton(BLUE, BLUE_HIGHLIGHTED, text, actionListener);
  }

  public static View<?, YogaLayout> createButton(Color color, Color hoveredColor,
      ITextComponent text, Runnable actionListener) {
    return new ParentView<>(
        new YogaLayout()
            .setWidth(30F)
            .setHeight(20F)
            .setFlex(1F),
        new YogaLayoutParent()
            .setJustifyContent(Justify.CENTER)
            .setAlignItems(Align.CENTER))
                .addChild(new TextView<>(new YogaLayout().setHeight(8F))
                    .setText(text)
                    .setShadow(false)
                    .setCentered(true))
                .configure(view -> view.getBackgroundColorProperty()
                    .setBaseValue(color)
                    .defineState(hoveredColor, States.HOVERED, States.ENABLED)
                    .setTransition(Transition.linear(100L)))
                .addActionSound(ImmerseSoundEvents.BUTTON_CLICK.get())
                .setFocusable(true)
                .addListener(ActionEvent.class, (view, event) -> actionListener.run());
  }
}
