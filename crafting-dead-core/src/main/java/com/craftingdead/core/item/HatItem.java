package com.craftingdead.core.item;

import java.util.List;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SimpleCapabilityProvider;
import com.craftingdead.core.capability.hat.DefaultHat;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.util.Text;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class HatItem extends Item {

  private final float headshotReductionPercentage;

  private final boolean immuneToFlashes;

  private final boolean immuneToGas;

  private final boolean nightVision;

  public HatItem(Properties properties) {
    super(properties);
    this.headshotReductionPercentage = properties.headshotReductionPercentage;
    this.immuneToFlashes = properties.immuneToFlashes;
    this.immuneToGas = properties.immuneToGas;
    this.nightVision = properties.nightVision;
    this.addPropertyOverride(new ResourceLocation("wearing"),
        (itemStack, world, entity) -> entity.getCapability(ModCapabilities.LIVING)
            .map(living -> living.getStackInSlot(InventorySlotType.HAT.getIndex()) == itemStack
                ? 1.0F
                : 0.0F)
            .orElse(0.0F));
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> lore,
      ITooltipFlag tooltipFlag) {
    if (this.headshotReductionPercentage > 0.0F) {
      ITextComponent percentageText = Text
          .of(String.format("%.1f", this.headshotReductionPercentage) + "%")
          .applyTextStyle(TextFormatting.RED);

      lore
          .add(Text
              .translate("item_lore.hat_item.headshot_reduction")
              .applyTextStyle(TextFormatting.GRAY)
              .appendSibling(percentageText));
    }
    if (this.immuneToFlashes) {
      ITextComponent text = Text.translate("item_lore.hat_item.immune_to_flashes");
      text.getStyle().setColor(TextFormatting.GRAY);
      lore.add(text);
    }
    if (this.immuneToGas) {
      ITextComponent text = Text.translate("item_lore.hat_item.immune_to_gas");
      text.getStyle().setColor(TextFormatting.GRAY);
      lore.add(text);
    }
    if (this.nightVision) {
      ITextComponent text = Text.translate("item_lore.hat_item.has_night_vision");
      text.getStyle().setColor(TextFormatting.GRAY);
      lore.add(text);
    }
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SimpleCapabilityProvider<>(new DefaultHat(this.nightVision,
        this.headshotReductionPercentage, this.immuneToFlashes, this.immuneToGas),
        () -> ModCapabilities.HAT);
  }

  public static class Properties extends Item.Properties {

    private float headshotReductionPercentage;
    private boolean immuneToFlashes;
    private boolean immuneToGas;
    private boolean nightVision;

    public Properties setHeadshotReductionPercentage(float headshotReductionPercentage) {
      this.headshotReductionPercentage = headshotReductionPercentage;
      return this;
    }

    public Properties setNightVision(boolean nightVision) {
      this.nightVision = nightVision;
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
