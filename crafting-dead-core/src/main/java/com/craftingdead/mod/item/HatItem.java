package com.craftingdead.mod.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class HatItem extends Item {

  private final double headshotReductionPercentage;

  /** In 1.6.4, flash-resistant helmets used to be actually immune. */
  private final boolean immuneToFlashes;

  private final boolean immuneToGas;

  private final boolean allowsNightVision;

  public HatItem(Properties properties) {
    super(properties);
    this.headshotReductionPercentage = properties.headshotReductionPercentage;
    this.immuneToFlashes = properties.immuneToFlashes;
    this.immuneToGas = properties.immuneToGas;
    this.allowsNightVision = properties.allowsNightVision;
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> lore,
      ITooltipFlag tooltipFlag) {
    if (this.hasHeadshotReduction()) {
      ITextComponent text = new TranslationTextComponent("attribute.hat.headshot_reduction",
          this.headshotReductionPercentage);
      text.getStyle().setColor(TextFormatting.GRAY);
      lore.add(text);
    }
    if (this.isImmuneToFlashes()) {
      ITextComponent text = new TranslationTextComponent("attribute.hat.immune_to_flashes");
      text.getStyle().setColor(TextFormatting.GRAY);
      lore.add(text);
    }
    if (this.isImmuneToGas()) {
      ITextComponent text = new TranslationTextComponent("attribute.hat.immune_to_gas");
      text.getStyle().setColor(TextFormatting.GRAY);
      lore.add(text);
    }
    if (this.hasNightVision()) {
      ITextComponent text = new TranslationTextComponent("attribute.hat.has_night_vision");
      text.getStyle().setColor(TextFormatting.GRAY);
      lore.add(text);
    }
  }

  /**
   * Checks whether this hat has night vision ability. Consider that could be more than one single
   * hat that have night vision ability.
   */
  public boolean hasNightVision() {
    return this.allowsNightVision;
  }

  /**
   * Checks whether the headshot reduction percentage is higher than zero.
   */
  public boolean hasHeadshotReduction() {
    return this.headshotReductionPercentage > 0D;
  }

  public double getHeadshotReductionPercentage() {
    return this.headshotReductionPercentage;
  }

  public boolean isImmuneToFlashes() {
    return this.immuneToFlashes;
  }

  public boolean isImmuneToGas() {
    return this.immuneToGas;
  }

  public static class Properties extends Item.Properties {

    private double headshotReductionPercentage;
    private boolean immuneToFlashes;
    private boolean immuneToGas;
    private boolean allowsNightVision;

    public Properties setHeadshotReductionPercentage(double headshotReductionPercentage) {
      this.headshotReductionPercentage = headshotReductionPercentage;
      return this;
    }

    public Properties setAllowsNightVision(boolean allowsNightVision) {
      this.allowsNightVision = allowsNightVision;
      return this;
    }

    public Properties setImmuneToFlashes(boolean immuneToFlashes) {
      this.immuneToFlashes = immuneToFlashes;
      return this;
    }

    public Properties setImmuneToGas(boolean immuneToGas) {
      this.immuneToGas = immuneToGas;
      return this;
    }
  }
}
