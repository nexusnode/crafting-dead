package com.craftingdead.mod.capability.living.player;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import com.craftingdead.mod.capability.living.DefaultLiving;
import com.craftingdead.mod.inventory.InventorySlotType;
import com.craftingdead.mod.item.ModItems;
import com.craftingdead.mod.potion.ModEffects;
import com.google.common.primitives.Ints;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;

/**
 * The abstracted player class - represents a Crafting Dead player.<br>
 * Subclasses are attached to the appropriate {@link E} via Forge capabilities.
 *
 * @param <E> - the associated {@link PlayerEntity}
 * @author Sm0keySa1m0n
 */
public class DefaultPlayer<E extends PlayerEntity> extends DefaultLiving<E> implements IPlayer<E> {

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

  public DefaultPlayer() {
    super();
  }

  public DefaultPlayer(E entity) {
    super(entity);
  }

  @Override
  public void tick() {
    super.tick();
    this.updateBrokenLeg();
    this.updateScubaClothing();
    this.updateScubaMask();
  }

  private void updateScubaClothing() {
    ItemStack clothingStack = this.getStackInSlot(InventorySlotType.CLOTHING.getIndex());
    if (clothingStack.getItem() == ModItems.SCUBA_CLOTHING.get()
        && this.entity.areEyesInFluid(FluidTags.WATER)) {
      this.entity
          .addPotionEffect(new EffectInstance(Effects.DOLPHINS_GRACE, 1, 0, false, false, true));
    }
  }

  private void updateScubaMask() {
    ItemStack headStack = this.getStackInSlot(InventorySlotType.HAT.getIndex());
    if (headStack.getItem() == ModItems.SCUBA_MASK.get()
        && this.entity.areEyesInFluid(FluidTags.WATER)) {
      this.entity
          .addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 1, 0, false, false, true));
    }
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
    // Can be null
    Entity immediateAttacker = source.getImmediateSource();

    boolean isValidSource = immediateAttacker != null || source.isExplosion();
    if (isValidSource) {
      float bleedChance = 0.1F * amount;
      if (random.nextFloat() < bleedChance
          && !this.entity.isPotionActive(ModEffects.BLEEDING.get())) {
        this.entity
            .sendStatusMessage(new TranslationTextComponent("message.bleeding")
                .setStyle(new Style().setColor(TextFormatting.RED).setBold(true)), true);
        this.entity.addPotionEffect(new EffectInstance(ModEffects.BLEEDING.get(), 9999999));
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

  @Override
  public boolean onDeathDrops(DamageSource cause, Collection<ItemEntity> drops) {
    boolean shouldKeepInventory =
        this.entity.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY);
    if (!shouldKeepInventory) {
      // Adds items from CD inventory
      for (int i = 0; i < this.getSlots(); i++) {
        ItemEntity itemEntity = new ItemEntity(this.entity.world, this.entity.getX(),
            this.entity.getY(), this.entity.getZ(), this.getStackInSlot(i));
        itemEntity.setDefaultPickupDelay();

        drops.add(itemEntity);
      }

      // Clears CD inventory
      this.stacks.clear();
    }
    return super.onDeathDrops(cause, drops);
  }

  @Override
  public void infect(float chance) {
    if (!this.entity.isCreative() && random.nextFloat() < chance
        && !this.entity.isPotionActive(ModEffects.INFECTION.get())) {
      this.entity
          .sendStatusMessage(new TranslationTextComponent("message.infected")
              .setStyle(new Style().setColor(TextFormatting.RED).setBold(true)), true);
      this.entity.addPotionEffect(new EffectInstance(ModEffects.INFECTION.get(), 9999999));
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
