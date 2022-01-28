/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.world.damagesource;

import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;

public class ModDamageSource {

  public static final String BULLET_HEADSHOT_DAMAGE_TYPE = "bullet.headshot";
  public static final String BULLET_BODY_DAMAGE_TYPE = "bullet";

  public static DamageSource causeGunDamage(LivingEntity source, ItemStack gunStack,
      boolean headshot) {
    return new KillFeedDamageSource(
        headshot ? BULLET_HEADSHOT_DAMAGE_TYPE : BULLET_BODY_DAMAGE_TYPE, source, gunStack,
        headshot ? KillFeedEntry.Type.HEADSHOT : KillFeedEntry.Type.NONE)
            .bypassArmor()
            .setProjectile()
            .setExplosion();
  }

  /**
   * Creates an explosion damage source without difficulty scaling.
   */
  public static DamageSource causeUnscaledExplosionDamage(@Nullable LivingEntity source) {
    return (source != null
        ? new EntityDamageSource("explosion.player", source)
        : new DamageSource("explosion"))
            .setExplosion();
  }

  /**
   * Causes a damage without doing knockback.
   *
   * @param victim - the victim
   * @param source - the source
   * @param amount - the amount of dmg
   * @return the result from <code>victim.attackEntityFrom()</code>
   */
  public static boolean causeDamageWithoutKnockback(Entity victim, DamageSource source,
      float amount) {
    if (victim instanceof LivingEntity) {
      LivingEntity livingHit = (LivingEntity) victim;

      // Gets the KNOCKBACK RESISTANCE attribute of the victim
      AttributeInstance livingResistance =
          livingHit.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
      // Saves the previous resistance value to be used after applying the hit
      double previousResistance = livingResistance.getBaseValue();

      // Sets the resistance to make the living not receive knockback
      livingResistance.setBaseValue(Integer.MAX_VALUE);
      // Finally attacks the entity without doing any knockback
      boolean attackResult = livingHit.hurt(source, amount);
      // Restores the previous knockback resistance value, so the
      // entity can receive knockback again.
      livingResistance.setBaseValue(previousResistance);
      return attackResult;
    }
    return victim.hurt(source, amount);
  }
}
