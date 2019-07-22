package com.craftingdead.mod.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class ModDamageSource {

  public static final DamageSource DEHYDRATION = new DamageSource("dehydration");

  public static DamageSource causeGunDamage(Entity source) {
    return new EntityDamageSource("bullet", source).setDamageBypassesArmor().setProjectile();
  }
}
