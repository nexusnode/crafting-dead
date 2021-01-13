package com.craftingdead.immerse.client.gui.screen.menu;

import com.craftingdead.core.util.Text;
import com.craftingdead.immerse.client.gui.component.Colour;
import com.craftingdead.immerse.client.gui.component.ContainerComponent;
import com.craftingdead.immerse.client.gui.component.DropdownComponent;
import com.craftingdead.immerse.client.gui.component.RectangleComponent;
import com.craftingdead.immerse.client.gui.component.TextBlockComponent;

public class PlayComponent extends ContainerComponent {

  public PlayComponent() {


    this.addChild(new ContainerComponent()
        .setFlex(25F)
        .setBackgroundColour(new Colour(0x50777777))
        .setBackgroundBlur(50.0F)
        .addChild(new TextBlockComponent(this.minecraft.fontRenderer, Text.of("Play"), true)
            .setMargin(5.5F))
        .addChild(new RectangleComponent()
            .setUnscaleHeight()
            .setHeight(1)
            .setWidthPercent(100.0F)
            .setBackgroundColour(new Colour(0X80B5B5B5)))
        .addChild(new DropdownComponent(0x50777777)
            .setWidth(100)
            .setHeight(20)
            .addItem(0, Text.of("Singleplayer"))));

    this.addChild(new ContainerComponent()
        .setFlex(75)
        .setBackgroundColour(new Colour(0x50555555))
        .setBackgroundBlur(50.0F));

  }
}
