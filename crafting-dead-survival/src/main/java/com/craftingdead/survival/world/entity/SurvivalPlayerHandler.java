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
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.entity.extension.PlayerHandler;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.world.effect.SurvivalMobEffects;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;

public class SurvivalPlayerHandler implements PlayerHandler {

  public static final ResourceLocation ID =
      new ResourceLocation(CraftingDeadSurvival.ID, "player_handler");

  /**
   * The % chance of getting infected by a zombie.
   */
  private static final float ZOMBIE_INFECTION_CHANCE = 0.1F;

  private static final Random random = new Random();

  private final PlayerExtension<?> player;

  public SurvivalPlayerHandler(PlayerExtension<?> player) {
    this.player = player;
  }

  public PlayerExtension<?> getPlayer() {
    return this.player;
  }

  @Override
  public void playerTick() {
    if (!this.player.getLevel().isClientSide()) {
      this.updateEffects();
      // this.updateBrokenLeg();
    }
  }

  private void updateEffects() {
    boolean invulnerable = this.player.getEntity().abilities.invulnerable
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
    if (source.getEntity() instanceof ZombieEntity) {
      this.infect(ZOMBIE_INFECTION_CHANCE);
    }
    return false;
  }

  public void infect(float chance) {
    PlayerEntity playerEntity = this.player.getEntity();
    if (!playerEntity.isCreative()
        && playerEntity.level.getDifficulty() != Difficulty.PEACEFUL
        && playerEntity.getRandom().nextFloat() < chance
        && !playerEntity.hasEffect(SurvivalMobEffects.INFECTION.get())
        && CraftingDeadSurvival.serverConfig.infectionEnabled.get()) {
      playerEntity.displayClientMessage(new TranslationTextComponent("message.infected")
          .withStyle(TextFormatting.RED, TextFormatting.BOLD), true);
      playerEntity.addEffect(new EffectInstance(SurvivalMobEffects.INFECTION.get(), 9999999));
    }
  }

  @Override
  public float handleDamaged(DamageSource source, float amount) {
    boolean invulnerable = this.player.getEntity().abilities.invulnerable
        || this.player.getLevel().getDifficulty() == Difficulty.PEACEFUL;

    if (!invulnerable
        && CraftingDeadSurvival.serverConfig.bleedingEnabled.get()
        && (source.getDirectEntity() != null || source.isExplosion())) {
      float bleedChance = 0.1F * amount;
      if (random.nextFloat() < bleedChance
          && !this.player.getEntity().hasEffect(SurvivalMobEffects.BLEEDING.get())) {
        this.player.getEntity()
            .displayClientMessage(new TranslationTextComponent("message.bleeding")
                .withStyle(TextFormatting.RED, TextFormatting.BOLD), true);
        this.player.getEntity()
            .addEffect(new EffectInstance(SurvivalMobEffects.BLEEDING.get(), 9999999));
      }
    }

    if (!invulnerable
        && CraftingDeadSurvival.serverConfig.brokenLegsEnabled.get()
        && !this.player.getEntity().hasEffect(SurvivalMobEffects.BROKEN_LEG.get())
        && source == DamageSource.FALL
        && ((amount > 0.0F && random.nextInt(3) == 0) || amount > 4.0F)) {
      this.player.getEntity()
          .displayClientMessage(new TranslationTextComponent("message.broken_leg")
              .withStyle(TextFormatting.RED, TextFormatting.BOLD), true);
      this.player.getEntity().addEffect(
          new EffectInstance(SurvivalMobEffects.BROKEN_LEG.get(), 9999999, 4));
      this.player.getEntity().addEffect(new EffectInstance(Effects.BLINDNESS, 100, 1));
    }

    return amount;
  }

  @Override
  public void encode(PacketBuffer out, boolean writeAll) {}

  @Override
  public void decode(PacketBuffer in) {}

  @Override
  public boolean requiresSync() {
    return false;
  }
}
