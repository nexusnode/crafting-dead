package com.craftingdead.mod.capability.player;

import java.util.Random;
import java.util.UUID;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.inventory.container.ModPlayerContainer;
import com.craftingdead.mod.potion.ModEffects;
import com.google.common.primitives.Ints;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
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
   * The % chance of getting infected by a zombie.
   */
  private static final float INFECTION_CHANCE = 0.1F;

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

  private boolean aiming;

  private final IInventory inventory = new Inventory(11);

  public DefaultPlayer() {
    this(null);
  }

  public DefaultPlayer(E entity) {
    this.entity = entity;
  }

  @Override
  public void tick() {
    ItemStack heldStack = this.entity.getHeldItemMainhand();
    if (heldStack != this.lastHeldStack) {
      heldStack.getCapability(ModCapabilities.GUN_CONTROLLER).ifPresent(gunController -> {
        gunController.stopReloading();
        gunController.setTriggerPressed(this.entity, heldStack, false);
      });
      this.lastHeldStack = heldStack;
    }

    heldStack
        .getCapability(ModCapabilities.GUN_CONTROLLER)
        .ifPresent(gunController -> gunController.tick(this.entity, heldStack));

    this.updateBrokenLeg();
  }

  private void updateBrokenLeg() {
    if (!this.entity.isCreative() && !this.entity.isPotionActive(ModEffects.BROKEN_LEG.get())
        && this.entity.onGround && !this.entity.isInWater()
        && ((this.entity.fallDistance > 4F && random.nextInt(3) == 0)
            || this.entity.fallDistance > 10F)) {
      this.entity
          .sendStatusMessage(new TranslationTextComponent("message.broken_leg")
              .setStyle(new Style().setColor(TextFormatting.RED).setBold(true)), true);
      this.entity.addPotionEffect(new EffectInstance(ModEffects.BROKEN_LEG.get(), 9999999, 4));
      this.entity.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 100, 1));
    }
  }

  @Override
  public float onDamaged(DamageSource source, float amount) {
    float bleedChance = 0.1F * amount;
    if (random.nextFloat() < bleedChance
        && !this.entity.isPotionActive(ModEffects.BLEEDING.get())) {
      this.entity
          .sendStatusMessage(new TranslationTextComponent("message.bleeding")
              .setStyle(new Style().setColor(TextFormatting.RED).setBold(true)), true);
      this.entity.addPotionEffect(new EffectInstance(ModEffects.BLEEDING.get(), 9999999));
    }
    return amount;
  }

  @Override
  public boolean onAttacked(DamageSource source, float amount) {
    if (source.getTrueSource() instanceof ZombieEntity && random.nextFloat() < INFECTION_CHANCE
        && !this.entity.isPotionActive(ModEffects.INFECTION.get())) {
      this.entity
          .sendStatusMessage(new TranslationTextComponent("message.infected")
              .setStyle(new Style().setColor(TextFormatting.RED).setBold(true)), true);
      this.entity.addPotionEffect(new EffectInstance(ModEffects.INFECTION.get(), 9999999));
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

  @Override
  public boolean onDeath(DamageSource cause) {
    return false;
  }

  @Override
  public void setTriggerPressed(boolean triggerPressed, boolean sendUpdate) {
    ItemStack heldStack = this.entity.getHeldItemMainhand();
    heldStack
        .getCapability(ModCapabilities.GUN_CONTROLLER)
        .ifPresent(gunController -> gunController
            .setTriggerPressed(this.entity, heldStack, triggerPressed));
  }

  @Override
  public void toggleAiming(boolean sendUpdate) {
    this.aiming = !this.aiming;
  }

  @Override
  public void toggleFireMode(boolean sendUpdate) {
    this.entity
        .getHeldItemMainhand()
        .getCapability(ModCapabilities.GUN_CONTROLLER)
        .ifPresent(gunController -> gunController.toggleFireMode(this.entity));
  }

  @Override
  public boolean isAiming() {
    return this.aiming;
  }

  @Override
  public void reload(boolean sendUpdate) {
    ItemStack heldStack = this.entity.getHeldItemMainhand();
    heldStack
        .getCapability(ModCapabilities.GUN_CONTROLLER)
        .ifPresent(gunController -> gunController.startReloading(this.entity, heldStack));
  }

  @Override
  public void openPlayerContainer() {
    this.entity
        .openContainer(new SimpleNamedContainerProvider((windowId, playerInventory,
            playerEntity) -> new ModPlayerContainer(windowId, this.entity.inventory),
            new TranslationTextComponent("container.player")));
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

    NonNullList<ItemStack> items =
        NonNullList.withSize(this.inventory.getSizeInventory(), ItemStack.EMPTY);
    for (int i = 0; i < this.inventory.getSizeInventory(); i++) {
      items.set(i, this.inventory.getStackInSlot(i));
    }
    nbt.put("inventory", ItemStackHelper.saveAllItems(nbt.getCompound("inventory"), items));
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

    NonNullList<ItemStack> items =
        NonNullList.withSize(this.inventory.getSizeInventory(), ItemStack.EMPTY);
    ItemStackHelper.loadAllItems(nbt.getCompound("inventory"), items);
    for (int i = 0; i < items.size(); i++) {
      this.inventory.setInventorySlotContents(i, items.get(i));
    }
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
  public IInventory getInventory() {
    return this.inventory;
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
