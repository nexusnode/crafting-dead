package com.craftingdead.core.item;

import java.util.List;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.capability.magazine.DefaultMagazine;
import com.craftingdead.core.capability.magazine.IMagazine;
import com.craftingdead.core.util.Text;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class MagazineItem extends Item {

  private final float armorPenetration;
  private final float entityHitDropChance;
  private final float blockHitDropChance;
  private final int size;

  public MagazineItem(Properties properties) {
    super(properties);
    this.size = properties.size;
    this.armorPenetration = properties.armorPenetration;
    this.entityHitDropChance = properties.entityHitDropChance;
    this.blockHitDropChance = properties.blockHitDropChance;
  }

  public float getArmorPenetration() {
    return this.armorPenetration;
  }

  public float getEntityHitDropChance() {
    return this.entityHitDropChance;
  }

  public float getBlockHitDropChance() {
    return this.blockHitDropChance;
  }

  public int getSize() {
    return this.size;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SerializableCapabilityProvider<>(new DefaultMagazine(this), () -> ModCapabilities.MAGAZINE);
  }

  @Override
  public int getItemEnchantability() {
    return 1;
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
        .ifPresent(magazine -> magazine.setSize(Math.max(0, damage)));
  }

  @Override
  public void addInformation(ItemStack stack, World world,
      List<ITextComponent> lines, ITooltipFlag tooltipFlag) {
    super.addInformation(stack, world, lines, tooltipFlag);

    // Shows the current amount if the maximum size is higher than 1
    if (this.getSize() > 1) {
      int currentAmount =
          stack.getCapability(ModCapabilities.MAGAZINE).map(IMagazine::getSize).orElse(0);

      ITextComponent amountText =
          Text.of(currentAmount + "/" + this.getSize()).applyTextStyle(TextFormatting.RED);

      lines.add(Text.translate("item_lore.magazine_item.amount").applyTextStyle(TextFormatting.GRAY)
          .appendSibling(amountText));
    }

    if (this.armorPenetration > 0) {
      lines.add(Text.translate("item_lore.magazine_item.armor_penetration")
          .applyTextStyle(TextFormatting.GRAY)
          .appendSibling(Text.of(String.format("%.1f", this.armorPenetration) + "%")
              .applyTextStyle(TextFormatting.RED)));
    }

    if (this.entityHitDropChance > 0) {
      lines.add(Text.translate("item_lore.magazine_item.entity_hit_recovery_chance")
          .applyTextStyle(TextFormatting.GRAY)
          .appendSibling(Text.of(String.format("%.1f", this.entityHitDropChance) + "%")
              .applyTextStyle(TextFormatting.RED)));
    }

    if (this.blockHitDropChance > 0) {
      lines.add(Text.translate("item_lore.magazine_item.block_hit_recovery_chance")
          .applyTextStyle(TextFormatting.GRAY)
          .appendSibling(Text.of(String.format("%.1f", this.blockHitDropChance) + "%")
              .applyTextStyle(TextFormatting.RED)));
    }
  }

  public static class Properties extends Item.Properties {

    private float armorPenetration;
    private float entityHitDropChance;
    private float blockHitDropChance;
    private int size;

    public Properties setArmorPenetration(float armorPenetration) {
      this.armorPenetration = armorPenetration;
      return this;
    }

    public Properties setEntityHitDropChance(float entityHitDropChance) {
      this.entityHitDropChance = entityHitDropChance;
      return this;
    }

    public Properties setBlockHitDropChance(float blockHitDropChance) {
      this.blockHitDropChance = blockHitDropChance;
      return this;
    }

    public Properties setSize(int size) {
      this.size = size;
      return this;
    }
  }
}
