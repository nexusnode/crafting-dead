package com.craftingdead.mod.item;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.SerializableProvider;
import com.craftingdead.mod.capability.paint.DefaultPaint;
import com.craftingdead.mod.capability.paint.IPaint;
import com.craftingdead.mod.util.Text;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class PaintItem extends Item {

  private final Optional<Supplier<Integer>> colour;
  private final boolean hasSkin;

  public PaintItem(Properties properties) {
    super(properties);
    this.colour = Optional.ofNullable(properties.colour);
    this.hasSkin = properties.hasSkin;
  }

  public boolean hasSkin() {
    return this.hasSkin;
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> lines,
      ITooltipFlag tooltipFlag) {
    stack
        .getCapability(ModCapabilities.PAINT)
        .map(IPaint::getColour)
        .orElse(Optional.empty())
        .ifPresent(colour -> lines
            .add(Text.of("#" + Integer.toHexString(colour)).applyTextStyle(TextFormatting.GRAY)));
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SerializableProvider<>(new DefaultPaint(this.hasSkin ? this.getRegistryName() : null,
        this.colour.map(Supplier::get).orElse(null)), () -> ModCapabilities.PAINT);
  }

  public static class Properties extends Item.Properties {

    private Supplier<Integer> colour;
    private boolean hasSkin = true;

    public Properties setColour() {
      this.colour = () -> new Random().nextInt(0xFFFFFF + 1);
      return this;
    }

    public Properties setColour(Supplier<Integer> colour) {
      this.colour = colour;
      return this;
    }

    public Properties setHasSkin(boolean hasSkin) {
      this.hasSkin = hasSkin;
      return this;
    }
  }
}
