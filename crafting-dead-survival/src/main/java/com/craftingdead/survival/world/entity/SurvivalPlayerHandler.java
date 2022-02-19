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
    if (!this.player.getLevel().isClientSide()) {
      this.updateEffects();
      if (this.player.getEntity().tickCount % 5 == 0 && this.soundLevel > 0) {
        this.soundLevel--;
      }
    }
  }

  private void updateEffects() {
    boolean invulnerable = this.player.getEntity().getAbilities().invulnerable
        || this.player.getLevel().getDifficulty() == Difficulty.PEACEFUL;

    if ((invulnerable || !CraftingDeadSurvival.serverConfig.bleedingEnabled.get())
        && this.player.getEntity().hasEffect(SurvivalMobEffects.BLEEDING.get())) {
      this.player.getEntity().removeEffect(SurvivalMobEffects.BLEEDING.get());
    }

    if ((invulnerable || !CraftingDeadSurvival.serverConfig.brokenLegsEnabled.get())
        && this.player.getEntity().hasEffect(SurvivalMobEffects.BROKEN_LEG.get())) {
      this.player.getEntity().removeEffect(SurvivalMobEffects.BROKEN_LEG.get());
    }

    if ((invulnerable || !CraftingDeadSurvival.serverConfig.infectionEnabled.get())
        && this.player.getEntity().hasEffect(SurvivalMobEffects.INFECTION.get())) {
      this.player.getEntity().removeEffect(SurvivalMobEffects.INFECTION.get());
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
    final var entity = this.player.getEntity();
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
    var invulnerable = this.player.getEntity().getAbilities().invulnerable
        || this.player.getLevel().getDifficulty() == Difficulty.PEACEFUL;

    if (!invulnerable
        && CraftingDeadSurvival.serverConfig.bleedingEnabled.get()
        && (source.getDirectEntity() != null || source.isExplosion())) {
      float bleedChance = 0.1F * amount;
      if (random.nextFloat() < bleedChance
          && !this.player.getEntity().hasEffect(SurvivalMobEffects.BLEEDING.get())) {
        this.player.getEntity()
            .displayClientMessage(new TranslatableComponent("message.bleeding")
                .withStyle(ChatFormatting.RED, ChatFormatting.BOLD), true);
        this.player.getEntity()
            .addEffect(new MobEffectInstance(SurvivalMobEffects.BLEEDING.get(), 9999999));
      }
    }

    if (!invulnerable
        && CraftingDeadSurvival.serverConfig.brokenLegsEnabled.get()
        && !this.player.getEntity().hasEffect(SurvivalMobEffects.BROKEN_LEG.get())
        && source == DamageSource.FALL
        && ((amount > 0.0F && random.nextInt(3) == 0) || amount > 4.0F)) {
      this.player.getEntity()
          .displayClientMessage(new TranslatableComponent("message.broken_leg")
              .withStyle(ChatFormatting.RED, ChatFormatting.BOLD), true);
      this.player.getEntity().addEffect(
          new MobEffectInstance(SurvivalMobEffects.BROKEN_LEG.get(), 9999999, 4));
      this.player.getEntity().addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 1));
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
