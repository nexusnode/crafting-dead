package com.craftingdead.mod.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class ModDamageSource {

  public static final DamageSource DEHYDRATION =
      new DamageSource("dehydration").setDamageBypassesArmor();
  public static final DamageSource BLEEDING = new DamageSource("bleeding").setDamageBypassesArmor();
  public static final DamageSource INFECTION =
      new DamageSource("infection").setDamageBypassesArmor();

  public static DamageSource causeGunDamage(Entity source, boolean headshot) {
    return new EntityDamageSource(headshot ? "bullet.headshot" : "bullet", source)
        .setDamageBypassesArmor()
        .setProjectile();
  }
}
