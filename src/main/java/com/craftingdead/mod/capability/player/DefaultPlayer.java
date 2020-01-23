package com.craftingdead.mod.capability.player;

import java.util.Random;
import java.util.UUID;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.potion.ModEffects;
import com.google.common.primitives.Ints;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
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
public class DefaultPlayer<E extends PlayerEntity> implements IPlayer<E> {

  /**
   * Random.
   */
  private static final Random random = new Random();

  /**
   * The vanilla entity.
   */
  protected final E entity;

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

  /**
   * The last held {@link ItemStack} - used to check if the player has switched item.
   */
  private ItemStack lastHeldStack = null;

  private boolean triggerPressed;

  private boolean aiming;

  protected int reloadDurationTicks = 0;

  protected int totalReloadDurationTicks;

  public DefaultPlayer() {
    this(null);
  }

  public DefaultPlayer(E entity) {
    this.entity = entity;
  }

  @Override
  public void tick() {
    this.updateHeldStack();
    this.updateBrokenLeg();
    if (this.isReloading() && --this.reloadDurationTicks == 0) {
      ItemStack itemStack = this.entity.getHeldItemMainhand();
      itemStack
          .getCapability(ModCapabilities.SHOOTABLE)
          .ifPresent(shootable -> shootable.finishReloading(itemStack, this.entity));
    }
  }

  private void updateBrokenLeg() {
    if (!this.entity.isCreative() && !this.entity.isPotionActive(ModEffects.brokenLeg)
        && this.entity.onGround && !this.entity.isInWater()
        && ((this.entity.fallDistance > 4F && random.nextInt(3) == 0)
            || this.entity.fallDistance > 10F)) {
      this.entity
          .sendStatusMessage(new TranslationTextComponent("message.broken_leg")
              .setStyle(new Style().setColor(TextFormatting.RED).setBold(true)), true);
      this.entity.addPotionEffect(new EffectInstance(ModEffects.brokenLeg, 9999999, 4));
      this.entity.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 100, 1));
    }
  }

  private void updateHeldStack() {
    ItemStack heldStack = this.entity.getHeldItemMainhand();
    if (heldStack != this.lastHeldStack) {
      if (this.lastHeldStack != null) {
        this.lastHeldStack
            .getCapability(ModCapabilities.SHOOTABLE)
            .ifPresent(
                shootable -> shootable.setTriggerPressed(this.lastHeldStack, this.entity, false));
      }
      this.lastHeldStack = heldStack;
    }
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

  @Override
  public boolean onDeath(DamageSource cause) {
    return false;
  }

  @Override
  public void setTriggerPressed(boolean triggerPressed) {
    this.triggerPressed = triggerPressed;
    ItemStack itemStack = this.entity.getHeldItemMainhand();
    itemStack.getCapability(ModCapabilities.SHOOTABLE).ifPresent(shootable -> {
      if (!this.isReloading()) {
        shootable.setTriggerPressed(itemStack, this.entity, triggerPressed);
      }
    });
  }

  @Override
  public void setTriggerPressed(boolean triggerPressed, boolean sendUpdate) {
    this.setTriggerPressed(triggerPressed);
  }

  @Override
  public boolean isTriggerPressed() {
    return this.triggerPressed;
  }

  @Override
  public void toggleAiming() {
    this.aiming = !this.aiming;
  }

  @Override
  public void toggleAiming(boolean sendUpdate) {
    this.toggleAiming();
  }

  @Override
  public boolean isAiming() {
    return this.aiming;
  }

  @Override
  public void reload() {
    ItemStack itemStack = this.entity.getHeldItemMainhand();
    itemStack.getCapability(ModCapabilities.SHOOTABLE).ifPresent(shootable -> {
      if (!this.isReloading() && shootable.canReload(itemStack, this.entity)) {
        this.entity.playSound(shootable.getReloadSound(), 1.0F, 1.0F);
        this.reloadDurationTicks = this.totalReloadDurationTicks = shootable.getReloadDuration();
      }
    });
  }

  @Override
  public void reload(boolean sendUpdate) {
    this.reload();
  }

  @Override
  public boolean isReloading() {
    return this.reloadDurationTicks > 0;
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
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
    this.setZombiesKilled(nbt.getInt("zombiesKilled"));
    this.setPlayersKilled(nbt.getInt("playersKilled"));
    this.setWater(nbt.getInt("water"));
    this.setMaxWater(nbt.getInt("maxWater"));
    this.setStamina(nbt.getInt("stamina"));
    this.setMaxStamina(nbt.getInt("maxStamina"));
  }

  @Override
  public int getDaysSurvived() {
    return this.daysSurvived;
  }

  @Override
  public void setDaysSurvived(int daysSurvived) {
    this.daysSurvived = daysSurvived;
  }

  @Override
  public int getZombiesKilled() {
    return this.zombiesKilled;
  }

  @Override
  public void setZombiesKilled(int zombiesKilled) {
    this.zombiesKilled = zombiesKilled;
  }


  @Override
  public int getPlayersKilled() {
    return this.playersKilled;
  }

  @Override
  public void setPlayersKilled(int playersKilled) {
    this.playersKilled = playersKilled;
  }

  @Override
  public int getWater() {
    return this.water;
  }

  @Override
  public void setWater(int water) {
    this.water = Ints.constrainToRange(water, 0, this.getMaxWater());
  }

  @Override
  public int getMaxWater() {
    return this.maxWater;
  }

  @Override
  public void setMaxWater(int maxWater) {
    this.maxWater = maxWater;
  }

  @Override
  public int getStamina() {
    return this.stamina;
  }

  @Override
  public void setStamina(int stamina) {
    this.stamina = Ints.constrainToRange(stamina, 0, this.getMaxStamina());
  }

  @Override
  public int getMaxStamina() {
    return this.maxStamina;
  }

  @Override
  public void setMaxStamina(int maxStamina) {
    this.maxStamina = maxStamina;
  }

  @Override
  public E getEntity() {
    return this.entity;
  }

  @Override
  public UUID getId() {
    return this.entity != null ? this.entity.getUniqueID() : null;
  }
}
