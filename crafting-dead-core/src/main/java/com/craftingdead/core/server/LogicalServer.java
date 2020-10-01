/*******************************************************************************
 * Copyright (C) 2020 Nexus Node
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
 ******************************************************************************/
package com.craftingdead.core.server;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.capability.SimpleCapabilityProvider;
import com.craftingdead.core.capability.hydration.DefaultHydration;
import com.craftingdead.core.capability.hydration.PresetHydration;
import com.craftingdead.core.capability.living.DefaultLiving;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.capability.living.Player;
import com.craftingdead.core.game.GameType;
import com.craftingdead.core.game.GameTypes;
import com.craftingdead.core.potion.ModEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class LogicalServer extends WorldSavedData {

  private final MinecraftServer minecraftServer;
  private GameManager<?, ?> gameManager;

  public LogicalServer(MinecraftServer minecraftServer) {
    super(CraftingDead.ID);
    this.minecraftServer = minecraftServer;
  }

  public void init() {
    this.minecraftServer.getWorld(DimensionType.OVERWORLD).getSavedData().getOrCreate(() -> this,
        CraftingDead.ID);
    // Default to survival
    this.gameManager = new GameManager<>(GameTypes.SURVIVAL.get().createGameServer(this));
  }

  public MinecraftServer getMinecraftServer() {
    return this.minecraftServer;
  }

  public GameManager<?, ?> getGameManager() {
    return this.gameManager;
  }

  @Override
  public void read(CompoundNBT nbt) {
    if (nbt.contains("gameType", Constants.NBT.TAG_STRING)) {
      GameType gameType = GameRegistry.findRegistry(GameType.class)
          .getValue(new ResourceLocation(nbt.getString("gameType")));
      if (gameType != null) {
        this.gameManager = new GameManager<>(gameType.createGameServer(this));
        this.gameManager.getGameServer().deserializeNBT(nbt.getCompound("game"));
      }
    }
  }

  @Override
  public CompoundNBT write(CompoundNBT nbt) {
    nbt.putString("gameType",
        this.gameManager.getGameServer().getGameType().getRegistryName().toString());
    nbt.put("game", this.gameManager.getGameServer().serializeNBT());
    return nbt;
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handleServerTick(TickEvent.ServerTickEvent event) {
    switch (event.phase) {
      case START:
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleLivingUpdate(LivingUpdateEvent event) {
    if (!(event.getEntityLiving() instanceof PlayerEntity)) {
      event.getEntityLiving().getCapability(ModCapabilities.LIVING)
          .ifPresent(ILiving::tick);
    }
  }

  @SubscribeEvent
  public void handlePlayerTick(TickEvent.PlayerTickEvent event) {
    switch (event.phase) {
      case END:
        event.player.getCapability(ModCapabilities.LIVING).ifPresent(ILiving::tick);
        break;
      default:
        break;
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingSetTarget(LivingSetAttackTargetEvent event) {
    if (event.getTarget() != null && event.getEntityLiving() instanceof MobEntity) {
      MobEntity mobEntity = (MobEntity) event.getEntityLiving();
      if (mobEntity.isPotionActive(ModEffects.FLASH_BLINDNESS.get())) {
        mobEntity.setAttackTarget(null);
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingDeath(LivingDeathEvent event) {
    if (event
        .getEntity()
        .getCapability(ModCapabilities.LIVING)
        .map(living -> living.onDeath(event.getSource()))
        .orElse(false)
        || (event.getSource().getTrueSource() != null && event
            .getSource()
            .getTrueSource()
            .getCapability(ModCapabilities.LIVING)
            .map(living -> living.onKill(event.getEntity()))
            .orElse(false))) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingDrops(LivingDropsEvent event) {
    event
        .getEntity()
        .getCapability(ModCapabilities.LIVING)
        .ifPresent(
            living -> event.setCanceled(living.onDeathDrops(event.getSource(), event.getDrops())));
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingAttack(LivingAttackEvent event) {
    event
        .getEntity()
        .getCapability(ModCapabilities.LIVING)
        .ifPresent(
            living -> event.setCanceled(living.onAttacked(event.getSource(), event.getAmount())));
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingDamage(LivingDamageEvent event) {
    event
        .getEntity()
        .getCapability(ModCapabilities.LIVING)
        .ifPresent(
            living -> event.setAmount(living.onDamaged(event.getSource(), event.getAmount())));
  }

  @SubscribeEvent
  public void handlePlayerClone(PlayerEvent.Clone event) {
    Player.get(event.getPlayer()).copyFrom(Player.get(event.getOriginal()), event.isWasDeath());
  }

  @SubscribeEvent
  public void handleAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
    if (!(event.getObject() instanceof PlayerEntity) && event.getObject() instanceof LivingEntity) {
      event.addCapability(new ResourceLocation(CraftingDead.ID, "living"),
          new SerializableCapabilityProvider<>(
              new DefaultLiving<>((LivingEntity) event.getObject()),
              () -> ModCapabilities.LIVING));
    } else if (event.getObject() instanceof ServerPlayerEntity) {
      event
          .addCapability(this.gameManager.getGameServer().getGameType().getRegistryName(),
              new SerializableCapabilityProvider<>(
                  this.gameManager.getGameServer()
                      .createPlayer((ServerPlayerEntity) event.getObject()),
                  () -> ModCapabilities.LIVING));
    }
  }

  @SubscribeEvent
  public void handleAttachCapabilitiesItemStack(AttachCapabilitiesEvent<ItemStack> event) {
    final Item item = event.getObject().getItem();
    int hydration = -1;
    if (item == Items.APPLE || item == Items.RABBIT_STEW) {
      hydration = 2;
    } else if (item == Items.CARROT || item == Items.BEETROOT || item == Items.HONEY_BOTTLE) {
      hydration = 1;
    } else if (item == Items.CHORUS_FRUIT || item == Items.SWEET_BERRIES) {
      hydration = 3;
    } else if (item == Items.ENCHANTED_GOLDEN_APPLE || item == Items.GOLDEN_APPLE
        || item == Items.MUSHROOM_STEW || item == Items.SUSPICIOUS_STEW
        || item == Items.BEETROOT_SOUP || item == Items.MELON_SLICE) {
      hydration = 5;
    } else if (item == Items.GOLDEN_CARROT) {
      hydration = 6;
    }
    if (hydration != -1) {
      event
          .addCapability(new ResourceLocation(CraftingDead.ID, "hydration"),
              new SimpleCapabilityProvider<>(new PresetHydration(hydration),
                  () -> ModCapabilities.HYDRATION));
    } else if (item == Items.POTION) {
      event
          .addCapability(new ResourceLocation(CraftingDead.ID, "hydration"),
              new SimpleCapabilityProvider<>(new DefaultHydration(),
                  () -> ModCapabilities.HYDRATION));
    }
  }

  @SubscribeEvent
  public void handleUseItem(LivingEntityUseItemEvent.Finish event) {
    event
        .getItem()
        .getCapability(ModCapabilities.HYDRATION)
        .map(hydration -> hydration.getHydration(event.getItem()))
        .ifPresent(hydration -> event
            .getEntityLiving()
            .addPotionEffect(new EffectInstance(ModEffects.HYDRATE.get(), 1, hydration)));
  }
}
