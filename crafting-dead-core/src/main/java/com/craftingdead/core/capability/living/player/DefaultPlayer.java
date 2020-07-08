package com.craftingdead.core.capability.living.player;

import java.util.Random;
import com.craftingdead.core.capability.living.DefaultLiving;
import com.craftingdead.core.potion.ModEffects;
import com.google.common.primitives.Ints;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * The abstracted player class - represents a Crafting Dead player.<br>
 * Subclasses are attached to the appropriate {@link E} via Forge capabilities.
 *
 * @param <E> - the associated {@link PlayerEntity}
 * @author Sm0keySa1m0n
 */
public class DefaultPlayer<E extends PlayerEntity> extends DefaultLiving<E> {

  /**
   * The % chance of getting infected by a zombie.
   */
  private static final float INFECTION_CHANCE = 0.1F;

  /**
   * Random.
   */
  private static final Random random = new Random();

  /**
   * Days survived.
   */
  protected int daysSurvived;

  /**
   * Zombies killed.
   */
  protected int zombiesKilled;

  /**
   * Players killed.
   */
  protected int playersKilled;

  /**
   * Water.
   */
  protected int water = 20;

  /**
   * Maximum water.
   */
  protected int maxWater = 20;

  /**
   * Stamina.
   */
  protected int stamina = 1500;

  /**
   * Maximum water.
   */
  protected int maxStamina = 1500;

  public DefaultPlayer(E entity) {
    super(entity);
  }

  @Override
  public void tick() {
    super.tick();
    this.updateEffects();
    this.updateBrokenLeg();
  }

  private void updateEffects() {
    if (this.getEntity().isCreative()) {
      if (this.getEntity().isPotionActive(ModEffects.BLEEDING.get())) {
        this.getEntity().removePotionEffect(ModEffects.BLEEDING.get());
      }
      if (this.getEntity().isPotionActive(ModEffects.BROKEN_LEG.get())) {
        this.getEntity().removePotionEffect(ModEffects.BROKEN_LEG.get());

      }
      if (this.getEntity().isPotionActive(ModEffects.INFECTION.get())) {
        this.getEntity().removePotionEffect(ModEffects.INFECTION.get());
      }
    }
  }

  private void updateBrokenLeg() {
    if (!this.getEntity().isCreative()
        && !this.getEntity().isPotionActive(ModEffects.BROKEN_LEG.get())
        && this.getEntity().onGround && !this.getEntity().isInWater()
        && ((this.getEntity().fallDistance > 4F && random.nextInt(3) == 0)
            || this.getEntity().fallDistance > 10F)) {
      this.getEntity()
          .sendStatusMessage(new TranslationTextComponent("message.broken_leg")
              .setStyle(new Style().setColor(TextFormatting.RED).setBold(true)), true);
      this.getEntity().addPotionEffect(new EffectInstance(ModEffects.BROKEN_LEG.get(), 9999999, 4));
      this.getEntity().addPotionEffect(new EffectInstance(Effects.BLINDNESS, 100, 1));
    }
  }

  @Override
  public float onDamaged(DamageSource source, float amount) {
    // Can be null
    Entity immediateAttacker = source.getImmediateSource();

    boolean isValidSource = immediateAttacker != null || source.isExplosion();
    if (isValidSource) {
      float bleedChance = 0.1F * amount;
      if (random.nextFloat() < bleedChance
          && !this.getEntity().isPotionActive(ModEffects.BLEEDING.get())) {
        this.getEntity()
            .sendStatusMessage(new TranslationTextComponent("message.bleeding")
                .setStyle(new Style().setColor(TextFormatting.RED).setBold(true)), true);
        this.getEntity().addPotionEffect(new EffectInstance(ModEffects.BLEEDING.get(), 9999999));
      }
    }
    return amount;
  }

  @Override
  public boolean onAttacked(DamageSource source, float amount) {
    if (!source.isProjectile() && source.getTrueSource() instanceof ZombieEntity) {
      this.infect(INFECTION_CHANCE);
    }
    return false;
  }

  @Override
  public boolean onKill(Entity target) {
    if (target instanceof ZombieEntity) {
      this.setZombiesKilled(this.getZombiesKilled() + 1);
    } else if (target instanceof ServerPlayerEntity) {
      this.setPlayersKilled(this.getPlayersKilled() + 1);
    }
    return false;
  }

  public void infect(float chance) {
    if (!this.getEntity().isCreative() && random.nextFloat() < chance
        && !this.getEntity().isPotionActive(ModEffects.INFECTION.get())) {
      this.getEntity()
          .sendStatusMessage(new TranslationTextComponent("message.infected")
              .setStyle(new Style().setColor(TextFormatting.RED).setBold(true)), true);
      this.getEntity().addPotionEffect(new EffectInstance(ModEffects.INFECTION.get(), 9999999));
    }
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = super.serializeNBT();
    nbt.putInt("zombiesKilled", this.zombiesKilled);
    nbt.putInt("playersKilled", this.playersKilled);
    nbt.putInt("water", this.water);
    nbt.putInt("maxWater", this.maxWater);
    nbt.putInt("stamina", this.stamina);
    nbt.putInt("maxStamina", this.maxStamina);
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    super.deserializeNBT(nbt);
    this.setZombiesKilled(nbt.getInt("zombiesKilled"));
    this.setPlayersKilled(nbt.getInt("playersKilled"));
    this.setWater(nbt.getInt("water"));
    this.setMaxWater(nbt.getInt("maxWater"));
    this.setStamina(nbt.getInt("stamina"));
    this.setMaxStamina(nbt.getInt("maxStamina"));
  }

  public int getDaysSurvived() {
    return this.daysSurvived;
  }

  public void setDaysSurvived(int daysSurvived) {
    this.daysSurvived = daysSurvived;
  }

  public int getZombiesKilled() {
    return this.zombiesKilled;
  }

  public void setZombiesKilled(int zombiesKilled) {
    this.zombiesKilled = zombiesKilled;
  }

  public int getPlayersKilled() {
    return this.playersKilled;
  }

  public void setPlayersKilled(int playersKilled) {
    this.playersKilled = playersKilled;
  }

  public int getWater() {
    return this.water;
  }

  public void setWater(int water) {
    this.water = Ints.constrainToRange(water, 0, this.getMaxWater());
  }

  public int getMaxWater() {
    return this.maxWater;
  }

  public void setMaxWater(int maxWater) {
    this.maxWater = maxWater;
  }

  public int getStamina() {
    return this.stamina;
  }

  public void setStamina(int stamina) {
    this.stamina = Ints.constrainToRange(stamina, 0, this.getMaxStamina());
  }

  public int getMaxStamina() {
    return this.maxStamina;
  }

  public void setMaxStamina(int maxStamina) {
    this.maxStamina = maxStamina;
  }
}
