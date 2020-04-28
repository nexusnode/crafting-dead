package com.craftingdead.mod.item;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.paint.IPaintColor;
import com.craftingdead.mod.capability.paint.ItemPaintColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

public class PaintItem extends Item {

  private final Optional<NonNullSupplier<Color>> colorGenerator;
  private final boolean hasSkin;

  public PaintItem(Properties properties) {
    super(properties);
    this.colorGenerator = Optional.ofNullable(properties.colorGenerator);
    this.hasSkin = properties.hasSkin;
  }

  public Optional<NonNullSupplier<Color>> getColorGenerator() {
    return this.colorGenerator;
  }

  public boolean hasSkin() {
    return this.hasSkin;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> lines,
      ITooltipFlag tooltipFlag) {
    stack.getCapability(ModCapabilities.PAINT_COLOR).ifPresent(paintColor -> {
      paintColor.getColor().ifPresent(color -> {
        lines.add(
            new StringTextComponent("R: " + color.getRed()).applyTextStyle(TextFormatting.RED));
        lines.add(
            new StringTextComponent("G: " + color.getGreen()).applyTextStyle(TextFormatting.GREEN));
        lines.add(
            new StringTextComponent("B: " + color.getBlue()).applyTextStyle(TextFormatting.BLUE));
      });
    });
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new ICapabilitySerializable<CompoundNBT>() {
      private final IPaintColor paintColor = new ItemPaintColor(colorGenerator);

      @Override
      public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == ModCapabilities.PAINT_COLOR
                ? LazyOptional.of(() -> paintColor).cast()
                : LazyOptional.empty();
      }

      @Override
      public CompoundNBT serializeNBT() {
        return this.paintColor.serializeNBT();
      }

      @Override
      public void deserializeNBT(CompoundNBT nbt) {
        this.paintColor.deserializeNBT(nbt);
      }
    };
  }

  public static class Properties extends Item.Properties {
    private NonNullSupplier<Color> colorGenerator;
    private boolean hasSkin = true;

    public Properties setColorGenerator(NonNullSupplier<Color> colorGenerator) {
      this.colorGenerator = colorGenerator;
      return this;
    }

    public Properties setHasSkin(boolean hasSkin) {
      this.hasSkin = hasSkin;
      return this;
    }
  }
}
