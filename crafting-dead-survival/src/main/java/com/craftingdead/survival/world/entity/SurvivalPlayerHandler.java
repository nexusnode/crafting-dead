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

package com.craftingdead.survival.world.entity;

import java.util.Random;
import com.craftingdead.core.world.entity.extension.LivingHandlerType;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.entity.extension.PlayerHandler;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.world.effect.SurvivalMobEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Zombie;

public class SurvivalPlayerHandler implements PlayerHandler {

  public static final LivingHandlerType<SurvivalPlayerHandler> TYPE = new LivingHandlerType<>(
      new ResourceLocation(CraftingDeadSurvival.ID, "survival_player"));

  /**
   * The % chance of getting infected by a zombie.
   */
  private static final float ZOMBIE_INFECTION_CHANCE = 0.1F;

  private static final Random random = new Random();

  private final PlayerExtension<?> player;

  private int soundLevel;

  public SurvivalPlayerHandler(PlayerExtension<?> player) {
    this.player = player;
  }

  public PlayerExtension<?> getPlayer() {
    return this.player;
  }

  public int getSoundLevel() {
    return this.soundLevel;
  }

  public void addSoundLevel(int soundLevel) {
    this.soundLevel += soundLevel;
  }

  @Override
  public void playerTick() {
    if (!this.player.level().isClientSide()) {
      this.updateEffects();
      if (this.player.entity().tickCount % 5 == 0 && this.soundLevel > 0) {
        this.soundLevel--;
      }
    }
  }

  private void updateEffects() {
    boolean invulnerable = this.player.entity().getAbilities().invulnerable
        || this.player.level().getDifficulty() == Difficulty.PEACEFUL;

    if ((invulnerable || !CraftingDeadSurvival.serverConfig.bleedingEnabled.get())
        && this.player.entity().hasEffect(SurvivalMobEffects.BLEEDING.get())) {
      this.player.entity().removeEffect(SurvivalMobEffects.BLEEDING.get());
    }

    if ((invulnerable || !CraftingDeadSurvival.serverConfig.brokenLegsEnabled.get())
        && this.player.entity().hasEffect(SurvivalMobEffects.BROKEN_LEG.get())) {
      this.player.entity().removeEffect(SurvivalMobEffects.BROKEN_LEG.get());
    }

    if ((invulnerable || !CraftingDeadSurvival.serverConfig.infectionEnabled.get())
        && this.player.entity().hasEffect(SurvivalMobEffects.INFECTION.get())) {
      this.player.entity().removeEffect(SurvivalMobEffects.INFECTION.get());
    }
  }

  @Override
  public boolean handleHurt(DamageSource source, float amount) {
    if (source.getEntity() instanceof Zombie) {
      this.infect(ZOMBIE_INFECTION_CHANCE);
    }
    return false;
  }

  public void infect(float chance) {
    final var entity = this.player.entity();
    if (!entity.isCreative()
        && entity.getLevel().getDifficulty() != Difficulty.PEACEFUL
        && entity.getRandom().nextFloat() < chance
        && !entity.hasEffect(SurvivalMobEffects.INFECTION.get())
        && CraftingDeadSurvival.serverConfig.infectionEnabled.get()) {
      entity.displayClientMessage(new TranslatableComponent("message.infected")
          .withStyle(ChatFormatting.RED, ChatFormatting.BOLD), true);
      entity.addEffect(new MobEffectInstance(SurvivalMobEffects.INFECTION.get(), 9999999));
    }
  }

  @Override
  public float handleDamaged(DamageSource source, float amount) {
    var invulnerable = this.player.entity().getAbilities().invulnerable
        || this.player.level().getDifficulty() == Difficulty.PEACEFUL;

    if (!invulnerable
        && CraftingDeadSurvival.serverConfig.bleedingEnabled.get()
        && (source.getDirectEntity() != null || source.isExplosion())) {
      float bleedChance = 0.1F * amount;
      if (random.nextFloat() < bleedChance
          && !this.player.entity().hasEffect(SurvivalMobEffects.BLEEDING.get())) {
        this.player.entity()
            .displayClientMessage(new TranslatableComponent("message.bleeding")
                .withStyle(ChatFormatting.RED, ChatFormatting.BOLD), true);
        this.player.entity()
            .addEffect(new MobEffectInstance(SurvivalMobEffects.BLEEDING.get(), 9999999));
      }
    }

    var legBreakChance =
        0.25F * this.player.entity().fallDistance / this.player.entity().getMaxFallDistance();
    if (!invulnerable
        && CraftingDeadSurvival.serverConfig.brokenLegsEnabled.get()
        && !this.player.entity().hasEffect(SurvivalMobEffects.BROKEN_LEG.get())
        && source == DamageSource.FALL
        && random.nextFloat() < legBreakChance) {
      this.player.entity()
          .displayClientMessage(new TranslatableComponent("message.broken_leg")
              .withStyle(ChatFormatting.RED, ChatFormatting.BOLD), true);
      this.player.entity().addEffect(
          new MobEffectInstance(SurvivalMobEffects.BROKEN_LEG.get(), 9999999, 4));
      this.player.entity().addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 1));
    }

    return amount;
  }

  @Override
  public void encode(FriendlyByteBuf out, boolean writeAll) {}

  @Override
  public void decode(FriendlyByteBuf in) {}

  @Override
  public boolean requiresSync() {
    return false;
  }
}
