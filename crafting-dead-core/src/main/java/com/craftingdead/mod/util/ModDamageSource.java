package com.craftingdead.mod.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
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

  public static void causeDamageWithoutKnockback(Entity entity, DamageSource source, float amount) {
    if (entity instanceof LivingEntity) {
      LivingEntity livingHit = (LivingEntity) entity;

      // Gets the KNOCKBACK RESISTANCE attribute of the victim
      IAttributeInstance livingResistance =
          livingHit.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
      // Saves the previous resistance value to be used after applying the hit
      double previousResistance = livingResistance.getBaseValue();

      // Sets the resistance to make the living not receive knockback
      livingResistance.setBaseValue(Integer.MAX_VALUE);
      // Finally attacks the entity without doing any knockback
      livingHit.attackEntityFrom(source, amount);
      // Restores the previous knockback resistance value, so the
      // entity can receive knockback again.
      livingResistance.setBaseValue(previousResistance);
    } else {
      entity.attackEntityFrom(source, amount);
    }
  }
}
