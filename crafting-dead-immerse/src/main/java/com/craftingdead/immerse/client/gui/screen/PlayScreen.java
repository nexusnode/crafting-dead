package com.craftingdead.immerse.client.gui.screen;

import com.craftingdead.immerse.client.gui.component.AbsoluteLocation;
import com.craftingdead.immerse.client.gui.component.Component;
import com.craftingdead.immerse.client.gui.component.PercentLocation;
import com.craftingdead.immerse.client.gui.component.RectangleComponent;
import com.craftingdead.immerse.client.gui.property.ColourProperty;
import net.minecraft.util.text.TranslationTextComponent;

public class PlayScreen extends MenuScreen {

  public PlayScreen() {
    super(new TranslationTextComponent("menu.play"));
    this
        .getRoot()
        .addChild(new RectangleComponent(new Component.RegionBuilder()
            .setX(new PercentLocation(0.075F).add(new AbsoluteLocation(100)))
            .setY("0px")
            .setWidth("100px")
            .setHeight("100%"), new ColourProperty(0x90000000)))
        .addChild(new RectangleComponent(new Component.RegionBuilder()
            .setX(new PercentLocation(0.075F).add(new AbsoluteLocation(200)))
            .setY("0px")
            .setWidth("100%")
            .setHeight("100%"), new ColourProperty(0x80000000)));
  }

  @Override
  public void init() {
    super.init();
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);

  }
}
