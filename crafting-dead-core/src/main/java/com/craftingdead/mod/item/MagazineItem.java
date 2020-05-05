package com.craftingdead.mod.item;

import javax.annotation.Nullable;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.SerializableProvider;
import com.craftingdead.mod.capability.magazine.IMagazine;
import com.craftingdead.mod.capability.magazine.ItemMagazine;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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
    return new SerializableProvider<>(new ItemMagazine(this), () -> ModCapabilities.MAGAZINE);
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
