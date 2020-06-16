package com.craftingdead.core.util;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class ModDamageSource {

  public static final String BULLET_HEADSHOT_DAMAGE_TYPE = "bullet.headshot";
  public static final String BULLET_BODY_DAMAGE_TYPE = "bullet";

  public static final DamageSource DEHYDRATION =
      new DamageSource("dehydration").setDamageBypassesArmor();
  public static final DamageSource BLEEDING = new DamageSource("bleeding").setDamageBypassesArmor();
  public static final DamageSource INFECTION =
      new DamageSource("infection").setDamageBypassesArmor();

  public static DamageSource causeGunDamage(Entity source, boolean headshot) {
    return new EntityDamageSource(headshot ? BULLET_HEADSHOT_DAMAGE_TYPE : BULLET_BODY_DAMAGE_TYPE, source)
        .setDamageBypassesArmor()
        .setProjectile();
  }

  /**
   * Creates an explosion damage source without difficulty scaling.
   */
  public static DamageSource causeUnscaledExplosionDamage(@Nullable Entity source) {
    return source != null
        ? new EntityDamageSource("explosion.player", source).setExplosion()
        : new DamageSource("explosion").setExplosion();
  }

  public static boolean isGunDamage(DamageSource source) {
    return source.getDamageType().equals(BULLET_HEADSHOT_DAMAGE_TYPE)
        || source.getDamageType().equals(BULLET_BODY_DAMAGE_TYPE);
  }

  /**
   * Causes a damage without doing knockback.
   *
   * @param victim - the victim
   * @param source - the source
   * @param amount - the amount of dmg
   * @return the result from <code>victim.attackEntityFrom()</code>
   */
  public static boolean causeDamageWithoutKnockback(Entity victim, DamageSource source, float amount) {
    if (victim instanceof LivingEntity) {
      LivingEntity livingHit = (LivingEntity) victim;

      // Gets the KNOCKBACK RESISTANCE attribute of the victim
      IAttributeInstance livingResistance =
          livingHit.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
      // Saves the previous resistance value to be used after applying the hit
      double previousResistance = livingResistance.getBaseValue();

      // Sets the resistance to make the living not receive knockback
      livingResistance.setBaseValue(Integer.MAX_VALUE);
      // Finally attacks the entity without doing any knockback
      boolean attackResult = livingHit.attackEntityFrom(source, amount);
      // Restores the previous knockback resistance value, so the
      // entity can receive knockback again.
      livingResistance.setBaseValue(previousResistance);
      return attackResult;
    }
    return victim.attackEntityFrom(source, amount);
  }
}
