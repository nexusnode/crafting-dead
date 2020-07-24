package com.craftingdead.core.item;

import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.capability.magazine.DefaultMagazine;
import com.craftingdead.core.capability.magazine.IMagazine;
import com.craftingdead.core.util.Text;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.DistExecutor;

public class MagazineItem extends Item {

  private final float armorPenetration;
  private final int size;
  private final Supplier<? extends Item> nextTier;
  private final Pair<Model, ResourceLocation> model;

  public MagazineItem(Properties properties) {
    super(properties);
    this.size = properties.size;
    this.armorPenetration = properties.armorPenetration;
    this.nextTier = properties.nextTier;
    this.model = properties.model == null ? null
        : Pair.of(DistExecutor.safeCallWhenOn(Dist.CLIENT, properties.model.getLeft()),
            properties.model.getRight());
  }

  public float getArmorPenetration() {
    return this.armorPenetration;
  }

  public int getSize() {
    return this.size;
  }

  public Supplier<? extends Item> getNextTier() {
    return this.nextTier;
  }

  public Pair<Model, ResourceLocation> getModel() {
    return this.model;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SerializableCapabilityProvider<>(new DefaultMagazine(this),
        () -> ModCapabilities.MAGAZINE);
  }

  @Override
  public boolean getIsRepairable(ItemStack itemStack, ItemStack materialStack) {
    return Tags.Items.GUNPOWDER.contains(materialStack.getItem())
        || super.getIsRepairable(itemStack, materialStack);
  }

  @Override
  public int getMaxDamage(ItemStack itemStack) {
    return this.size;
  }

  @Override
  public int getDamage(ItemStack itemStack) {
    return this.size - itemStack
        .getCapability(ModCapabilities.MAGAZINE)
        .map(IMagazine::getSize)
        .orElse(this.size);
  }

  @Override
  public void setDamage(ItemStack itemStack, int damage) {
    itemStack
        .getCapability(ModCapabilities.MAGAZINE)
        .ifPresent(magazine -> magazine.setSize(Math.max(0, this.size - damage)));
  }

  @Override
  public boolean isDamageable() {
    return true;
  }

  @Override
  public void addInformation(ItemStack stack, World world, List<ITextComponent> lines,
      ITooltipFlag tooltipFlag) {
    super.addInformation(stack, world, lines, tooltipFlag);

    // Shows the current amount if the maximum size is higher than 1
    if (this.getSize() > 1) {
      int currentAmount =
          stack.getCapability(ModCapabilities.MAGAZINE).map(IMagazine::getSize).orElse(0);

      ITextComponent amountText =
          Text.of(currentAmount + "/" + this.getSize()).applyTextStyle(TextFormatting.RED);

      lines
          .add(Text
              .translate("item_lore.magazine_item.amount")
              .applyTextStyle(TextFormatting.GRAY)
              .appendSibling(amountText));
    }

    if (this.armorPenetration > 0) {
      lines
          .add(Text
              .translate("item_lore.magazine_item.armor_penetration")
              .applyTextStyle(TextFormatting.GRAY)
              .appendSibling(Text
                  .of(String.format("%.0f%%", this.armorPenetration) + "%")
                  .applyTextStyle(TextFormatting.RED)));
    }
  }

  public static class Properties extends Item.Properties {

    private float armorPenetration;
    private int size;
    private Supplier<? extends Item> nextTier;
    private Pair<Supplier<DistExecutor.SafeCallable<Model>>, ResourceLocation> model;

    public Properties setArmorPenetration(float armorPenetration) {
      this.armorPenetration = armorPenetration;
      return this;
    }

    public Properties setSize(int size) {
      this.size = size;
      return this;
    }

    public Properties setNextTier(Supplier<? extends Item> nextTier) {
      this.nextTier = nextTier;
      return this;
    }

    public Properties setModel(
        Pair<Supplier<DistExecutor.SafeCallable<Model>>, ResourceLocation> model) {
      this.model = model;
      return this;
    }
  }
}
