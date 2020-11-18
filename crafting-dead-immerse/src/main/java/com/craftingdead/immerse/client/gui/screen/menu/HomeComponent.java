/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
import com.craftingdead.immerse.client.gui.component.Colour;
import com.craftingdead.immerse.client.gui.component.ContainerComponent;
import com.craftingdead.immerse.client.gui.component.Overflow;

public class HomeComponent extends ContainerComponent {

  public HomeComponent() {
    // this.addChild(new TextBlockComponent(this.minecraft.fontRenderer,
    // new StringTextComponent("Hello World!"), false)
    // .setWidth(100)
    // .setHeight(100)
    // .addHoverAnimation(Component.X_SCALE, new float[] {2.0F}, 150.0F)
    // .addHoverAnimation(Component.Y_SCALE, new float[] {2.0F}, 150.0F));

    // final FakePlayerEntity fakePlayerEntity =
    // new FakePlayerEntity(this.minecraft.getSession().getProfile());
    // fakePlayerEntity.setHeldItem(Hand.MAIN_HAND, new ItemStack(ModItems.AK47.get()));
    // this.addChild(new EntityComponent(fakePlayerEntity)
    // .setLeftMarginPercent(1.0F)
    // .setTopMarginPercent(0.5F)
    // .setRight(75)
    // .setScale(5F)
    // .setMarginAuto());

    this.addChild(new ContainerComponent()
        .setLeftMargin(50)
        .setTopMargin(50)
        .setWidthPercent(50)
        .setBottomMargin(50)
        .setBackgroundColour(new Colour(0x40999999))
        .setOverflow(Overflow.SCROLL)
        .addAll(new File("news.xml")));
  }
}
