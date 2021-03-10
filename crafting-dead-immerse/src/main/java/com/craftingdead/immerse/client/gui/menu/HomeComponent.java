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
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import com.craftingdead.core.item.GunItem;
import com.craftingdead.core.util.Text;
import com.craftingdead.immerse.client.gui.component.type.Align;
import com.craftingdead.immerse.client.gui.component.Colour;
import com.craftingdead.immerse.client.gui.component.ContainerComponent;
import com.craftingdead.immerse.client.gui.component.EntityComponent;
import com.craftingdead.immerse.client.gui.component.FakePlayerEntity;
import com.craftingdead.immerse.client.gui.component.type.FlexDirection;
import com.craftingdead.immerse.client.gui.component.type.Justify;
import com.craftingdead.immerse.client.gui.component.type.Overflow;
import com.craftingdead.immerse.client.gui.component.TextBlockComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.ForgeRegistries;

public class HomeComponent extends ContainerComponent {

  private static final Random random = new Random();

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
        new FakePlayerEntity(this.minecraft.getUser().getGameProfile());


    List<Item> gunItems = ForgeRegistries.ITEMS.getValues()
        .stream()
        .filter(item -> item instanceof GunItem)
        .collect(Collectors.toList());
    Item randomGunItem = gunItems.get(random.nextInt(gunItems.size()));
    fakePlayerEntity.setItemInHand(Hand.MAIN_HAND, new ItemStack(randomGunItem));

    this.addChild(new ContainerComponent()
        .setAlignItems(Align.CENTER)
        .setWidthPercent(30.0F)
        .setHeightPercent(45.0F)
        .addChild(new TextBlockComponent(
            Text.of(this.minecraft.getUser().getName()).withStyle(TextFormatting.BOLD,
                TextFormatting.DARK_RED)))
        .addChild(new EntityComponent(fakePlayerEntity)
            .setWidthPercent(100.0F)
            .setAspectRatio(0.95F)));

  }
}
