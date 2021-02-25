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
import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.util.Text;
import com.craftingdead.immerse.client.gui.component.Align;
import com.craftingdead.immerse.client.gui.component.Colour;
import com.craftingdead.immerse.client.gui.component.ContainerComponent;
import com.craftingdead.immerse.client.gui.component.EntityComponent;
import com.craftingdead.immerse.client.gui.component.FakePlayerEntity;
import com.craftingdead.immerse.client.gui.component.FlexDirection;
import com.craftingdead.immerse.client.gui.component.Justify;
import com.craftingdead.immerse.client.gui.component.Overflow;
import com.craftingdead.immerse.client.gui.component.TextBlockComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;

public class HomeComponent extends ContainerComponent {

  public HomeComponent() {
    this.setFlexDirection(FlexDirection.ROW);
    this.setAlignItems(Align.CENTER);
    this.setJustifyContent(Justify.SPACE_AROUND);


    this.addChild(new ContainerComponent()
        .setWidthPercent(50.0F)
        .setHeightPercent(75.0F)
        .setBackgroundColour(new Colour(0x70777777))
        .setBackgroundBlur(50.0F)
        .addChild(new ContainerComponent()
            .setPadding(10.0F)
            .setOverflow(Overflow.SCROLL)
            .addAll(new File("news.xml"))));

    final FakePlayerEntity fakePlayerEntity =
        new FakePlayerEntity(this.minecraft.getSession().getProfile());
    fakePlayerEntity.setHeldItem(Hand.MAIN_HAND, new ItemStack(ModItems.AK47.get()));


    this.addChild(new ContainerComponent()
        .setAlignItems(Align.CENTER)
        .setWidthPercent(30.0F)
        .setHeightPercent(45.0F)
        .addChild(new TextBlockComponent(this.minecraft.fontRenderer,
            Text.of("Sm0keySa1m0n").mergeStyle(TextFormatting.BOLD),
            true).setWidth(80.0F))
        .addChild(new EntityComponent(fakePlayerEntity)
            .setWidthPercent(100.0F)
            .setAspectRatio(0.9F)));

  }
}
