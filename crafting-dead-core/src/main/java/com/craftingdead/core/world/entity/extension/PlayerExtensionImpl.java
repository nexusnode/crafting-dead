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

package com.craftingdead.core.world.entity.extension;

import java.util.Random;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.event.OpenEquipmentMenuEvent;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.KillFeedMessage;
import com.craftingdead.core.network.util.NetworkDataManager;
import com.craftingdead.core.world.clothing.Clothing;
import com.craftingdead.core.world.damagesource.KillFeedProvider;
import com.craftingdead.core.world.damagesource.ModDamageSource;
import com.craftingdead.core.world.effect.ModMobEffects;
import com.craftingdead.core.world.inventory.EquipmentMenu;
import com.craftingdead.core.world.inventory.InventorySlotType;
import com.google.common.primitives.Ints;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Pose;
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

public class PlayerExtensionImpl<L extends PlayerEntity>
    extends LivingExtensionImpl<L, PlayerHandler> implements PlayerExtension<L> {

  private static final int WATER_DAMAGE_DELAY_TICKS = 20 * 6;

  private static final int WATER_DELAY_TICKS = 20 * 40;

  private static final Random random = new Random();

  private final NetworkDataManager dataManager = new NetworkDataManager();

  private static final DataParameter<Integer> WATER =
      new DataParameter<>(0x00, DataSerializers.INT);

  private static final DataParameter<Integer> MAX_WATER =
      new DataParameter<>(0x01, DataSerializers.INT);

  private static final DataParameter<Boolean> COMBAT_MODE_ENABLED =
      new DataParameter<>(0x02, DataSerializers.BOOLEAN);

  private int waterTicks;

  private boolean cachedCombatModeEnabled;

  public PlayerExtensionImpl(L entity) {
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
  protected void tickExtension(ResourceLocation extensionId, PlayerHandler extension) {
    super.tickExtension(extensionId, extension);
    if (extension.isCombatModeEnabled()) {
      this.cachedCombatModeEnabled = true;
    }
  }

  @Override
  public void playerTick() {
    if (!this.entity.level.isClientSide()) {
      this.updateHydration();
      this.updateEffects();
      this.updateBrokenLeg();
    }
    if (this.isCrouching()) {
      this.getEntity().setPose(Pose.SWIMMING);
    }
  }

  private void updateHydration() {
    if (CraftingDead.serverConfig.hydrationEnabled.get()
        && this.getEntity().level.getDifficulty() != Difficulty.PEACEFUL
        && !this.getEntity().abilities.invulnerable) {
      this.waterTicks++;
      if (this.getWater() <= 0) {
        if (this.waterTicks >= WATER_DAMAGE_DELAY_TICKS && this.getWater() == 0) {
          this.getEntity().hurt(ModDamageSource.DEHYDRATION, 1.0F);
          this.waterTicks = 0;
        }
      } else if (this.waterTicks >= WATER_DELAY_TICKS) {
        this.setWater(this.getWater() - 1);
        if (this.getEntity().isSprinting()) {
          this.setWater(this.getWater() - 1);
        }
        if (this.getItemHandler().getStackInSlot(InventorySlotType.CLOTHING.getIndex())
            .getCapability(ModCapabilities.CLOTHING).map(Clothing::hasEnhancedProtection)
            .orElse(false)) {
          this.setWater(this.getWater() - 1);
        }
        this.waterTicks = 0;
      }
    }
  }

  private void updateEffects() {
    boolean invulnerable = this.getEntity().abilities.invulnerable
        || this.getEntity().level.getDifficulty() == Difficulty.PEACEFUL;

    if ((invulnerable || !CraftingDead.serverConfig.bleedingEnabled.get())
        && this.getEntity().hasEffect(ModMobEffects.BLEEDING.get())) {
      this.getEntity().removeEffect(ModMobEffects.BLEEDING.get());
    }

    if ((invulnerable || !CraftingDead.serverConfig.brokenLegsEnabled.get())
        && this.getEntity().hasEffect(ModMobEffects.BROKEN_LEG.get())) {
      this.getEntity().removeEffect(ModMobEffects.BROKEN_LEG.get());
    }
  }

  private void updateBrokenLeg() {
    if (!this.getEntity().isCreative()
        && CraftingDead.serverConfig.brokenLegsEnabled.get()
        && this.getEntity().level.getDifficulty() != Difficulty.PEACEFUL
        && !this.getEntity().hasEffect(ModMobEffects.BROKEN_LEG.get())
        && this.getEntity().isOnGround() && !this.getEntity().isInWater()
        && ((this.getEntity().fallDistance > 4F && random.nextInt(3) == 0)
            || this.getEntity().fallDistance > 10F)) {
      this.getEntity().displayClientMessage(new TranslationTextComponent("message.broken_leg")
          .setStyle(Style.EMPTY.applyFormats(TextFormatting.RED).withBold(true)), true);
      this.getEntity().addEffect(new EffectInstance(ModMobEffects.BROKEN_LEG.get(), 9999999, 4));
      this.getEntity().addEffect(new EffectInstance(Effects.BLINDNESS, 100, 1));
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
    this.getEntity().openMenu(new SimpleNamedContainerProvider((windowId, playerInventory,
        playerEntity) -> new EquipmentMenu(windowId, this.getEntity().inventory),
        new TranslationTextComponent("container.equipment")));
  }

  @Override
  public void openStorage(InventorySlotType slotType) {
    ItemStack storageStack = this.getItemHandler().getStackInSlot(slotType.getIndex());
    storageStack
        .getCapability(ModCapabilities.STORAGE)
        .ifPresent(storage -> this.getEntity()
            .openMenu(
                new SimpleNamedContainerProvider(storage, storageStack.getHoverName())));
  }


  @Override
  public boolean onDeath(DamageSource source) {
    if (super.onDeath(source)) {
      return true;
    } else if (source instanceof KillFeedProvider) {
      NetworkChannel.PLAY.getSimpleChannel().send(PacketDistributor.ALL.noArg(),
          new KillFeedMessage(((KillFeedProvider) source).createKillFeedEntry(this.entity)));
    }
    return false;
  }

  @Override
  public float onDamaged(DamageSource source, float amount) {
    amount = super.onDamaged(source, amount);
    // Can be null
    Entity immediateAttacker = source.getDirectEntity();

    boolean isValidSource = immediateAttacker != null || source.isExplosion();
    boolean invulnerable = this.getEntity().abilities.invulnerable
        || this.getEntity().level.getDifficulty() == Difficulty.PEACEFUL;

    if (isValidSource && !invulnerable
        && CraftingDead.serverConfig.bleedingEnabled.get()) {
      float bleedChance = 0.1F * amount
          * this.getItemHandler().getStackInSlot(InventorySlotType.CLOTHING.getIndex())
              .getCapability(ModCapabilities.CLOTHING)
              .map(clothing -> clothing.hasEnhancedProtection() ? 0.5F : 0.0F)
              .orElse(1.0F);
      if (random.nextFloat() < bleedChance
          && !this.getEntity().hasEffect(ModMobEffects.BLEEDING.get())) {
        this.getEntity()
            .displayClientMessage(new TranslationTextComponent("message.bleeding")
                .setStyle(Style.EMPTY.applyFormats(TextFormatting.RED).withBold(true)),
                true);
        this.getEntity().addEffect(new EffectInstance(ModMobEffects.BLEEDING.get(), 9999999));
      }
    }
    return amount;
  }

  @Override
  public void copyFrom(PlayerExtension<?> that, boolean wasDeath) {
    // Copies the inventory. Doesn't actually matter if it was death or not.
    // Death drops from 'that' should be cleared on death drops to prevent item duplication.
    for (int i = 0; i < that.getItemHandler().getSlots() - 1; i++) {
      this.getItemHandler().setStackInSlot(i, that.getItemHandler().getStackInSlot(i));
    }

    for (PlayerHandler extension : this.handlers.values()) {
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
