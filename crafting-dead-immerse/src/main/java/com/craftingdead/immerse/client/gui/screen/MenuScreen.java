package com.craftingdead.immerse.client.gui.screen;

import com.craftingdead.immerse.client.gui.component.AbsoluteLocation;
import com.craftingdead.immerse.client.gui.component.CentredPercentLocation;
import com.craftingdead.immerse.client.gui.component.Component;
import com.craftingdead.immerse.client.gui.component.LabelComponent;
import com.craftingdead.immerse.client.gui.component.PercentLocation;
import com.craftingdead.immerse.client.gui.property.ColourProperty;
import com.craftingdead.immerse.client.gui.property.FunctionalProperty;
import com.craftingdead.immerse.client.gui.property.IProperty;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.LanguageScreen;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MenuScreen extends ModScreen {

  protected MenuScreen(ITextComponent title) {
    super(title);

    final int spacing = 15;
    int y = -50;
    final IProperty<FontRenderer> fontRenderer =
        new FunctionalProperty<>(() -> this.font, (font) -> this.font = font);

    this
        .getRoot()
        .addChild(new LabelComponent(
            new Component.RegionBuilder()
                .setX(new PercentLocation(0.075F))
                .setY(new CentredPercentLocation(0.5F).add(new AbsoluteLocation(y += spacing))),
            fontRenderer, new TranslationTextComponent("menu.play"), new ColourProperty(0xCCCCCC),
            true)
                .addHoverAnimation(LabelComponent::getColourProperty,
                    new ColourProperty(0xFFFFFF).getAnimatedValues(), 400.0F)
                .addActionListener(component -> this.minecraft.displayGuiScreen(new PlayScreen())));

    this
        .getRoot()
        .addChild(new LabelComponent(
            new Component.RegionBuilder()
                .setX(new PercentLocation(0.075F))
                .setY(new CentredPercentLocation(0.5F).add(new AbsoluteLocation(y += spacing))),
            fontRenderer, new TranslationTextComponent("menu.options"),
            new ColourProperty(0xCCCCCC), true)
                .addHoverAnimation(LabelComponent::getColourProperty,
                    new ColourProperty(0xFFFFFF).getAnimatedValues(), 400.0F)
                .addActionListener(component -> this.minecraft
                    .displayGuiScreen(new OptionsScreen(this, this.minecraft.gameSettings))));

    this
        .getRoot()
        .addChild(new LabelComponent(
            new Component.RegionBuilder()
                .setX(new PercentLocation(0.075F))
                .setY(new CentredPercentLocation(0.5F).add(new AbsoluteLocation(y += spacing))),
            fontRenderer, new TranslationTextComponent("options.language"),
            new ColourProperty(0xCCCCCC), true)
                .addHoverAnimation(LabelComponent::getColourProperty,
                    new ColourProperty(0xFFFFFF).getAnimatedValues(), 400.0F)
                .addActionListener(component -> this.minecraft
                    .displayGuiScreen(new LanguageScreen(this, this.minecraft.gameSettings,
                        this.minecraft.getLanguageManager()))));

    this
        .getRoot()
        .addChild(new LabelComponent(
            new Component.RegionBuilder()
                .setX(new PercentLocation(0.075F))
                .setY(new CentredPercentLocation(0.5F).add(new AbsoluteLocation(y += spacing))),
            fontRenderer, new TranslationTextComponent("menu.quit"), new ColourProperty(0xFF6257),
            true)
                .addHoverAnimation(LabelComponent::getColourProperty,
                    new ColourProperty(0xE51C23).getAnimatedValues(), 400.0F)
                .addActionListener(component -> this.minecraft.shutdown()));
  }
}
