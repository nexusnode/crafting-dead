package com.craftingdead.mod.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SwordItem;

public class WeaponItem extends SwordItem {

  public WeaponItem(WeaponItem.Properties properties) {
    super(properties.weaponTier, properties.attackDamageln, properties.attackSpeedln, properties);
  }

  public static class Properties extends Item.Properties {

    public int attackDamageln;

    public float attackSpeedln;

    public WeaponTier weaponTier;

    public WeaponItem.Properties setMaxStackSize(int maxStackSize) {
      this.maxStackSize(maxStackSize);
      return this;
    }

    public WeaponItem.Properties setGroup(ItemGroup groupIn) {
      this.group(groupIn);
      return this;
    }

    public WeaponItem.Properties setAttackDamageln(int attackDamageln) {
      this.attackDamageln = attackDamageln;
      return this;
    }

    public WeaponItem.Properties setAttackSpeedln(float attackSpeedln) {
      this.attackSpeedln = attackSpeedln;
      return this;
    }

    public WeaponItem.Properties setTierItem(WeaponTier weaponTier) {
      this.weaponTier = weaponTier;
      return this;
    }

  }
}
