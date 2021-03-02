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

package com.craftingdead.core.capability.living;

import java.util.Random;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.clothing.IClothing;
import com.craftingdead.core.event.OpenEquipmentMenuEvent;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.inventory.container.EquipmentContainer;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.KillFeedMessage;
import com.craftingdead.core.network.util.NetworkDataManager;
import com.craftingdead.core.potion.ModEffects;
import com.craftingdead.core.util.IKillFeedProvider;
import com.craftingdead.core.util.ModDamageSource;
import com.google.common.primitives.Ints;
import com.tiviacz.travelersbackpack.capability.CapabilityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * The abstracted player class - represents a Crafting Dead player.<br>
 * Subclasses are attached to the appropriate {@link L} via Forge capabilities.
 *
 * @param <L> - the associated {@link PlayerEntity}
 * @author Sm0keySa1m0n
 */
public class PlayerImpl<L extends PlayerEntity> extends LivingImpl<L, IPlayerExtension>
    implements IPlayer<L> {

  /**
   * The % chance of getting infected by a zombie.
   */
  private static final float INFECTION_CHANCE = 0.1F;

  private static final int WATER_DAMAGE_DELAY_TICKS = 20 * 6;

  private static final int WATER_DELAY_TICKS = 20 * 40;

  private static final Random random = new Random();

  private final NetworkDataManager dataManager = new NetworkDataManager();

  private static final DataParameter<Integer> WATER =
      new DataParameter<>(0x00, DataSerializers.VARINT);

  private static final DataParameter<Integer> MAX_WATER =
      new DataParameter<>(0x01, DataSerializers.VARINT);

  private static final DataParameter<Boolean> COMBAT_MODE_ENABLED =
      new DataParameter<>(0x02, DataSerializers.BOOLEAN);

  private int waterTicks;

  private boolean cachedCombatModeEnabled;

  public PlayerImpl(L entity) {
    super(entity);
    this.dataManager.register(WATER, 20);
    this.dataManager.register(MAX_WATER, 20);
    this.dataManager.register(COMBAT_MODE_ENABLED, false);
  }

  @Override
  public void tick() {
    this.cachedCombatModeEnabled = false;
    super.tick();
  }

  @Override
  protected void tickExtension(ResourceLocation extensionId, IPlayerExtension extension) {
    super.tickExtension(extensionId, extension);
    if (extension.isCombatModeEnabled()) {
      this.cachedCombatModeEnabled = true;
    }
  }

  @Override
  public void playerTick() {
    if (!this.entity.getEntityWorld().isRemote()) {
      this.updateWater();
      this.updateEffects();
      this.updateBrokenLeg();
    }
    if (this.isCrouching()) {
      this.getEntity().setPose(Pose.SWIMMING);
    }
  }

  private void updateWater() {
    if (this.getEntity().getEntityWorld().getDifficulty() != Difficulty.PEACEFUL
        && !this.getEntity().abilities.disableDamage) {
      this.waterTicks++;
      if (this.getWater() <= 0) {
        if (this.waterTicks >= WATER_DAMAGE_DELAY_TICKS && this.getWater() == 0) {
          this.getEntity().attackEntityFrom(ModDamageSource.DEHYDRATION, 1.0F);
          this.waterTicks = 0;
        }
      } else if (this.waterTicks >= WATER_DELAY_TICKS) {
        this.setWater(this.getWater() - 1);
        if (this.getEntity().isSprinting()) {
          this.setWater(this.getWater() - 1);
        }
        if (CraftingDead.getInstance().isTravelersBackpacksLoaded()
            && CapabilityUtils.isWearingBackpack(this.getEntity())) {
          this.setWater(this.getWater() - 1);
        }
        if (this.getItemHandler().getStackInSlot(InventorySlotType.CLOTHING.getIndex())
            .getCapability(ModCapabilities.CLOTHING).map(IClothing::hasEnhancedProtection)
            .orElse(false)) {
          this.setWater(this.getWater() - 1);
        }
        this.waterTicks = 0;
      }
    }
  }

  private void updateEffects() {
    if (this.getEntity().isCreative()
        || this.getEntity().getEntityWorld().getDifficulty() == Difficulty.PEACEFUL) {
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
        && this.getEntity().getEntityWorld().getDifficulty() != Difficulty.PEACEFUL
        && !this.getEntity().isPotionActive(ModEffects.BROKEN_LEG.get())
        && this.getEntity().isOnGround() && !this.getEntity().isInWater()
        && ((this.getEntity().fallDistance > 4F && random.nextInt(3) == 0)
            || this.getEntity().fallDistance > 10F)) {
      this.getEntity()
          .sendStatusMessage(new TranslationTextComponent("message.broken_leg")
              .setStyle(Style.EMPTY.createStyleFromFormattings(TextFormatting.RED).setBold(true)),
              true);
      this.getEntity().addPotionEffect(new EffectInstance(ModEffects.BROKEN_LEG.get(), 9999999, 4));
      this.getEntity().addPotionEffect(new EffectInstance(Effects.BLINDNESS, 100, 1));
    }
  }

  @Override
  public boolean isMovementBlocked() {
    return !this.entity.isSpectator() && super.isMovementBlocked();
  }

  @Override
  public boolean isCombatModeEnabled() {
    return !this.entity.isSpectator()
        && (this.cachedCombatModeEnabled || this.dataManager.get(COMBAT_MODE_ENABLED));
  }

  @Override
  public void setCombatModeEnabled(boolean combatModeEnabled) {
    this.dataManager.set(COMBAT_MODE_ENABLED, combatModeEnabled);
  }

  @Override
  public void openEquipmentMenu() {
    if (MinecraftForge.EVENT_BUS.post(new OpenEquipmentMenuEvent(this))) {
      return;
    }
    this.getEntity()
        .openContainer(new SimpleNamedContainerProvider((windowId, playerInventory,
            playerEntity) -> new EquipmentContainer(windowId, this.getEntity().inventory),
            new TranslationTextComponent("container.equipment")));
  }

  @Override
  public void openStorage(InventorySlotType slotType) {
    ItemStack storageStack = this.getItemHandler().getStackInSlot(slotType.getIndex());
    storageStack
        .getCapability(ModCapabilities.STORAGE)
        .ifPresent(storage -> this.getEntity()
            .openContainer(
                new SimpleNamedContainerProvider(storage, storageStack.getDisplayName())));
  }


  @Override
  public boolean onDeath(DamageSource source) {
    if (super.onDeath(source)) {
      return true;
    } else if (source instanceof IKillFeedProvider) {
      NetworkChannel.PLAY.getSimpleChannel().send(PacketDistributor.ALL.noArg(),
          new KillFeedMessage(((IKillFeedProvider) source).createKillFeedEntry(this.entity)));
    }
    return false;
  }

  @Override
  public float onDamaged(DamageSource source, float amount) {
    amount = super.onDamaged(source, amount);
    // Can be null
    Entity immediateAttacker = source.getImmediateSource();

    boolean isValidSource = immediateAttacker != null || source.isExplosion();
    if (isValidSource && !this.entity.isCreative()
        && this.entity.getEntityWorld().getDifficulty() != Difficulty.PEACEFUL) {
      float bleedChance = 0.1F * amount
          * this.getItemHandler().getStackInSlot(InventorySlotType.CLOTHING.getIndex())
              .getCapability(ModCapabilities.CLOTHING)
              .map(clothing -> clothing.hasEnhancedProtection() ? 0.5F : 0.0F)
              .orElse(1.0F);
      if (random.nextFloat() < bleedChance
          && !this.getEntity().isPotionActive(ModEffects.BLEEDING.get())) {
        this.getEntity()
            .sendStatusMessage(new TranslationTextComponent("message.bleeding")
                .setStyle(Style.EMPTY.createStyleFromFormattings(TextFormatting.RED).setBold(true)),
                true);
        this.getEntity().addPotionEffect(new EffectInstance(ModEffects.BLEEDING.get(), 9999999));
      }
    }
    return amount;
  }

  @Override
  public boolean onAttacked(DamageSource source, float amount) {
    if (!super.onAttacked(source, amount)) {
      if (!source.isProjectile() && source.getTrueSource() instanceof ZombieEntity) {
        this.infect(INFECTION_CHANCE);
      }
      return false;
    }
    return true;
  }

  @Override
  public void infect(float chance) {
    if (!this.entity.isCreative()
        && this.entity.getEntityWorld().getDifficulty() != Difficulty.PEACEFUL
        && random.nextFloat() < chance
        && !this.getEntity().isPotionActive(ModEffects.INFECTION.get())) {
      this.getEntity()
          .sendStatusMessage(new TranslationTextComponent("message.infected")
              .setStyle(Style.EMPTY.createStyleFromFormattings(TextFormatting.RED).setBold(true)),
              true);
      this.getEntity().addPotionEffect(new EffectInstance(ModEffects.INFECTION.get(), 9999999));
    }
  }

  @Override
  public void copyFrom(IPlayer<?> that, boolean wasDeath) {
    // Copies the inventory. Doesn't actually matter if it was death or not.
    // Death drops from 'that' should be cleared on death drops to prevent item duplication.
    for (int i = 0; i < that.getItemHandler().getSlots() - 1; i++) {
      this.getItemHandler().setStackInSlot(i, that.getItemHandler().getStackInSlot(i));
    }

    for (IPlayerExtension extension : this.extensions.values()) {
      extension.copyFrom(that, wasDeath);
    }
  }

  @Override
  public int getWater() {
    return this.dataManager.get(WATER);
  }

  @Override
  public void setWater(int water) {
    this.dataManager.set(WATER, Ints.constrainToRange(water, 0, this.getMaxWater()));
  }

  @Override
  public int getMaxWater() {
    return this.dataManager.get(MAX_WATER);
  }

  @Override
  public void setMaxWater(int maxWater) {
    this.dataManager.set(MAX_WATER, maxWater);
  }

  public NetworkDataManager getDataManager() {
    return this.dataManager;
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = super.serializeNBT();
    nbt.putInt("water", this.getWater());
    nbt.putInt("maxWater", this.getMaxWater());
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    super.deserializeNBT(nbt);
    this.setWater(nbt.getInt("water"));
    this.setMaxWater(nbt.getInt("maxWater"));
  }

  @Override
  public void encode(PacketBuffer out, boolean writeAll) {
    super.encode(out, writeAll);
    NetworkDataManager
        .writeEntries(writeAll ? this.dataManager.getAll() : this.dataManager.getDirty(), out);
  }

  @Override
  public void decode(PacketBuffer in) {
    super.decode(in);
    this.dataManager.setEntryValues(NetworkDataManager.readEntries(in));
  }

  @Override
  public boolean requiresSync() {
    return super.requiresSync() || this.dataManager.isDirty();
  }
}
