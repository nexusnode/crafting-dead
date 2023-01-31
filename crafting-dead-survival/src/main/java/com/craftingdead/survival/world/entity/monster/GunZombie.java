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

package com.craftingdead.survival.world.entity.monster;

import com.craftingdead.core.world.entity.ai.FollowAttractiveGrenadeGoal;
import com.craftingdead.core.world.entity.ai.LookAtEntityGoal;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.grenade.FlashGrenadeEntity;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.survival.world.entity.SurvivalPlayerHandler;
import net.minecraft.Util;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class GunZombie extends Zombie implements RangedAttackMob {

  private RangedAttackGoal rangedAttackGoal;

  private long triggerPressedStartTime;

  public GunZombie(EntityType<? extends GunZombie> type, Level world) {
    super(type, world);
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    this.rangedAttackGoal = new RangedAttackGoal(this, 1.0D, 40, 20F) {
      @Override
      public boolean canUse() {
        return super.canUse() && GunZombie.this.getMainHandItem()
            .getCapability(Gun.CAPABILITY).isPresent();
      }
    };
    this.goalSelector.addGoal(2, this.rangedAttackGoal);
    this.goalSelector.addGoal(1, new FollowAttractiveGrenadeGoal(this, 1.15F));
    this.goalSelector.addGoal(4,
        new LookAtEntityGoal<>(this, FlashGrenadeEntity.class, 20.0F, 0.35F));
  }

  @Override
  protected void addBehaviourGoals() {
    super.addBehaviourGoals();
    this.targetSelector.addGoal(2,
        new NearestAttackableTargetGoal<>(this, Player.class, 5, false, false,
            targetEntity -> targetEntity.getCapability(LivingExtension.CAPABILITY)
                .resolve()
                .flatMap(extension -> extension.getHandler(SurvivalPlayerHandler.TYPE))
                .map(handler -> handler.getSoundLevel() >= targetEntity.distanceTo(this))
                .orElse(false)) {
          @Override
          public double getFollowDistance() {
            return 100.0D;
          }
        });
  }

  @Override
  public void tick() {
    super.tick();
    if (!this.level.isClientSide()) {
      this.getCapability(LivingExtension.CAPABILITY)
          .ifPresent(living -> living.mainHandGun().ifPresent(gun -> {
            if (gun.isTriggerPressed()
                && (!this.rangedAttackGoal.canContinueToUse() || (Util.getMillis()
                    - this.triggerPressedStartTime > 1000 + this.random.nextInt(2000)))) {
              gun.setTriggerPressed(living, false, true);
            }
          }));
    }
  }

  @Override
  public void performRangedAttack(LivingEntity livingEntity, float distance) {
    if (!this.level.isClientSide()) {
      this.getCapability(LivingExtension.CAPABILITY)
          .ifPresent(living -> living.mainHandGun().ifPresent(gun -> {
            this.triggerPressedStartTime = Util.getMillis();
            gun.setTriggerPressed(living, true, true);
          }));
    }
  }
}
