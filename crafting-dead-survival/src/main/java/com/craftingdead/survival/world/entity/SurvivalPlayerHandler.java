package com.craftingdead.survival.world.entity;

import java.util.Random;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.entity.extension.PlayerHandler;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.world.effect.SurvivalMobEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
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
    if (!this.player.getEntity().level.isClientSide()) {
      this.updateEffects();
      this.updateBrokenLeg();
    }
  }

  private void updateEffects() {
    boolean invulnerable = this.player.getEntity().abilities.invulnerable
        || this.player.getEntity().level.getDifficulty() == Difficulty.PEACEFUL;

    if ((invulnerable || !CraftingDeadSurvival.serverConfig.bleedingEnabled.get())
        && this.player.getEntity().hasEffect(SurvivalMobEffects.BLEEDING.get())) {
      this.player.getEntity().removeEffect(SurvivalMobEffects.BLEEDING.get());
    }

    if ((invulnerable || !CraftingDeadSurvival.serverConfig.brokenLegsEnabled.get())
        && this.player.getEntity().hasEffect(SurvivalMobEffects.BROKEN_LEG.get())) {
      this.player.getEntity().removeEffect(SurvivalMobEffects.BROKEN_LEG.get());
    }

    if (invulnerable && this.player.getEntity().hasEffect(SurvivalMobEffects.INFECTION.get())) {
      this.player.getEntity().removeEffect(SurvivalMobEffects.INFECTION.get());
    }
  }

  private void updateBrokenLeg() {
    if (!this.player.getEntity().isCreative()
        && CraftingDeadSurvival.serverConfig.brokenLegsEnabled.get()
        && this.player.getEntity().level.getDifficulty() != Difficulty.PEACEFUL
        && !this.player.getEntity().hasEffect(SurvivalMobEffects.BROKEN_LEG.get())
        && this.player.getEntity().isOnGround() && !this.player.getEntity().isInWater()
        && ((this.player.getEntity().fallDistance > 4F && random.nextInt(3) == 0)
            || this.player.getEntity().fallDistance > 10F)) {
      this.player.getEntity()
          .displayClientMessage(new TranslationTextComponent("message.broken_leg")
              .setStyle(Style.EMPTY.applyFormats(TextFormatting.RED).withBold(true)), true);
      this.player.getEntity()
          .addEffect(new EffectInstance(SurvivalMobEffects.BROKEN_LEG.get(), 9999999, 4));
      this.player.getEntity().addEffect(new EffectInstance(Effects.BLINDNESS, 100, 1));
    }
  }

  @Override
  public boolean onAttacked(DamageSource source, float amount) {
    if (source.getEntity() instanceof ZombieEntity) {
      infect(ZOMBIE_INFECTION_CHANCE);
    }
    return false;
  }

  public void infect(float chance) {
    PlayerEntity playerEntity = this.player.getEntity();
    if (!playerEntity.isCreative()
        && playerEntity.getCommandSenderWorld().getDifficulty() != Difficulty.PEACEFUL
        && playerEntity.getRandom().nextFloat() < chance
        && !playerEntity.hasEffect(SurvivalMobEffects.INFECTION.get())) {
      playerEntity.displayClientMessage(new TranslationTextComponent("message.infected")
          .setStyle(Style.EMPTY.applyFormats(TextFormatting.RED).withBold(true)),
          true);
      playerEntity.addEffect(new EffectInstance(SurvivalMobEffects.INFECTION.get(), 9999999));
    }
  }

  @Override
  public float onDamaged(DamageSource source, float amount) {
    Entity immediateAttacker = source.getDirectEntity();

    boolean isValidSource = immediateAttacker != null || source.isExplosion();
    boolean invulnerable = this.player.getEntity().abilities.invulnerable
        || this.player.getEntity().level.getDifficulty() == Difficulty.PEACEFUL;

    if (isValidSource && !invulnerable
        && CraftingDeadSurvival.serverConfig.bleedingEnabled.get()) {
      float bleedChance = 0.1F * amount;
      if (random.nextFloat() < bleedChance
          && !this.player.getEntity().hasEffect(SurvivalMobEffects.BLEEDING.get())) {
        this.player.getEntity()
            .displayClientMessage(new TranslationTextComponent("message.bleeding")
                .setStyle(Style.EMPTY.applyFormats(TextFormatting.RED).withBold(true)),
                true);
        this.player.getEntity()
            .addEffect(new EffectInstance(SurvivalMobEffects.BLEEDING.get(), 9999999));
      }
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
