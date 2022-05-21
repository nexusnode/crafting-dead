/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.mixin;

import org.jetbrains.annotations.Nullable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.craftingdead.core.world.entity.ExplosionSource;

@Mixin(Explosion.class)
public class ExplosionMixin {

  @Shadow
  @Final
  @Nullable
  private Entity source;

  @Redirect(at = @At(value = "INVOKE",
      target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),
      method = "explode")
  public boolean entityHurtProxy(Entity instance, DamageSource damageSource, float damage) {
    var damageMultiplier =
        (this.source instanceof ExplosionSource source) ? source.getDamageMultiplier() : 1.0F;
    return instance.hurt(damageSource, damage * damageMultiplier);
  }

  @Redirect(at = @At(value = "INVOKE",
      target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;"),
      method = "explode")
  public Vec3 vec3AddProxy(Vec3 instance, double x, double y, double z) {
    var knockbackMultiplier =
        (this.source instanceof ExplosionSource source) ? source.getKnockbackMultiplier() : 1D;
    return instance.add(x * knockbackMultiplier, y * knockbackMultiplier, z * knockbackMultiplier);
  }
}
