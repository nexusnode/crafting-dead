/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.worldguard;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Set;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.craftingdead.core.event.GrenadeThrowEvent;
import com.craftingdead.core.event.GunEvent;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.event.WaterDecayEvent;
import com.craftingdead.survival.world.effect.SurvivalMobEffects;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.BukkitPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.Event;

public class CraftingDeadWorldGuard extends JavaPlugin {

  private static final Logger logger = LoggerFactory.getLogger(CraftingDeadWorldGuard.class);
  private static final MethodHandle getWorld;
  private static final MethodHandle getBukkitEntity;
  private static final MethodHandle getHandle;
  private static final MethodHandle getEffect;
  private static final MethodHandle hasEffect;
  private static final MethodHandle removeEffect;

  public static final StateFlag INFECTION = new StateFlag("infection", true);
  public static final StateFlag BROKEN_LEGS = new StateFlag("broken-legs", true);
  public static final StateFlag BLEEDING = new StateFlag("bleeding", true);
  public static final StateFlag THIRST = new StateFlag("thirst", true);
  public static final StateFlag SHOOTING = new StateFlag("shooting", true);
  public static final StateFlag GRENADE_THROWING = new StateFlag("grenade-throwing", true);
  public static final BooleanFlag CLEAR_EQUIPMENT_ON_EXIT =
      new BooleanFlag("clear-equipment-on-exit");

  static {
    try {
      var craftWorld = Class.forName("org.bukkit.craftbukkit.v1_18_R2.CraftWorld");
      var lookup = MethodHandles.publicLookup();
      getWorld = lookup.findVirtual(
          Level.class,
          "getWorld",
          MethodType.methodType(craftWorld));

      var craftEntity = Class.forName("org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity");
      getBukkitEntity = lookup.findVirtual(
          Entity.class,
          "getBukkitEntity",
          MethodType.methodType(craftEntity));
      getHandle = lookup.findVirtual(
          craftEntity,
          "getHandle",
          MethodType.methodType(Entity.class));

      hasEffect = lookup.findVirtual(LivingEntity.class, "m_21023_",
          MethodType.methodType(boolean.class,
              Class.forName("net.minecraft.world.effect.MobEffectList")));
      removeEffect = lookup.findVirtual(LivingEntity.class, "m_21195_",
          MethodType.methodType(boolean.class,
              Class.forName("net.minecraft.world.effect.MobEffectList")));

      getEffect = lookup.findVirtual(MobEffectInstance.class, "m_19544_",
          MethodType.methodType(Class.forName("net.minecraft.world.effect.MobEffectList")));

    } catch (Throwable t) {
      throw new ExceptionInInitializerError(t);
    }
  }

  @Override
  public void onLoad() {
    logger.info("Loading Crafting Dead WorldGuard...");
    registerFlag(INFECTION);
    registerFlag(BROKEN_LEGS);
    registerFlag(BLEEDING);
    registerFlag(THIRST);
    registerFlag(SHOOTING);
    registerFlag(GRENADE_THROWING);
    registerFlag(CLEAR_EQUIPMENT_ON_EXIT);

    var forgeBus = MinecraftForge.EVENT_BUS;
    forgeBus.addListener(this::handlePotionApplicable);
    forgeBus.addListener(this::handleWaterDecay);
    forgeBus.addListener(this::handleGunEntityHit);
    forgeBus.addListener(this::handleGunBlockHit);
    forgeBus.addListener(this::handleGrenadeThrow);

    logger.info("Crafting Dead WorldGuard loaded.");
  }

  @Override
  public void onEnable() {
    var sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
    sessionManager.registerHandler(
        HandlerAdapter.createFactory(
            this::handleEnter,
            this::handleExit),
        null);
  }

  private void handleEnter(PlayerExtension<?> extension, Set<ProtectedRegion> regions) {
    var stopBleeding = false;
    var stopBrokenLegs = false;

    for (var region : regions) {
      if (region.getFlag(BLEEDING) == StateFlag.State.DENY) {
        stopBleeding = true;
      }

      if (region.getFlag(BROKEN_LEGS) == StateFlag.State.DENY) {
        stopBrokenLegs = true;
      }
    }

    if (stopBleeding && hasEffect(extension.entity(), SurvivalMobEffects.BLEEDING.get())) {
      removeEffect(extension.entity(), SurvivalMobEffects.BLEEDING.get());
    }

    if (stopBrokenLegs && hasEffect(extension.entity(), SurvivalMobEffects.BROKEN_LEG.get())) {
      removeEffect(extension.entity(), SurvivalMobEffects.BROKEN_LEG.get());
    }
  }

  private void handleExit(PlayerExtension<?> extension, Set<ProtectedRegion> regions) {
    var clearEquipment = false;

    for (var region : regions) {
      if (region.getFlag(CLEAR_EQUIPMENT_ON_EXIT) == Boolean.TRUE) {
        clearEquipment = true;
      }
    }

    if (clearEquipment) {
      extension.clearEquipment();
    }
  }

  private void handlePotionApplicable(PotionEvent.PotionApplicableEvent event) {
    if (event.getEntity() instanceof Player player) {
      var localPlayer = toWorldGuardPlayer(player);
      var regions = getApplicableRegions(player);
      var effect = getEffect(event.getPotionEffect());


      StateFlag flag = null;
      if (effect == SurvivalMobEffects.INFECTION.get()) {
        flag = INFECTION;
      } else if (effect == SurvivalMobEffects.BROKEN_LEG.get()) {
        flag = BROKEN_LEGS;
      } else if (effect == SurvivalMobEffects.BLEEDING.get()) {
        flag = BLEEDING;
      }

      if (flag != null && !regions.testState(localPlayer, flag)) {
        event.setResult(Event.Result.DENY);
      }
    }
  }

  private void handleWaterDecay(WaterDecayEvent event) {
    var localPlayer = toWorldGuardPlayer(event.getPlayer());
    var regions = getApplicableRegions(event.getPlayer());
    event.setCanceled(!regions.testState(localPlayer, THIRST));
  }

  private void handleGunEntityHit(GunEvent.EntityHit event) {
    if (event.living() instanceof PlayerExtension<?> player) {
      var localPlayer = toWorldGuardPlayer(player.entity());
      var regions = getApplicableRegions(player.entity());
      event.setCanceled(!regions.testState(localPlayer, SHOOTING));
    }
  }

  private void handleGunBlockHit(GunEvent.BlockHit event) {
    if (event.living() instanceof PlayerExtension<?> player) {
      var localPlayer = toWorldGuardPlayer(player.entity());
      var regions = getApplicableRegions(player.entity());
      event.setCanceled(!regions.testState(localPlayer, SHOOTING));
    }
  }

  private void handleGrenadeThrow(GrenadeThrowEvent event) {
    var localPlayer = toWorldGuardPlayer(event.getPlayer());
    var regions = getApplicableRegions(event.getPlayer());
    event.setCanceled(!regions.testState(localPlayer, GRENADE_THROWING));
  }

  private static ApplicableRegionSet getApplicableRegions(Player player) {
    var container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    var regions = container.get(BukkitAdapter.adapt(adapt(player.getLevel())));
    var position = BlockVector3.at(player.getX(), player.getY(), player.getZ());
    return regions.getApplicableRegions(position);
  }

  private static LocalPlayer toWorldGuardPlayer(Player player) {
    return WorldGuardPlugin.inst().wrapPlayer((org.bukkit.entity.Player) adapt(player));
  }

  private static World adapt(Level level) {
    try {
      return (World) getWorld.invoke(level);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  private static org.bukkit.entity.Entity adapt(Entity entity) {
    try {
      return (org.bukkit.entity.Entity) getBukkitEntity.invoke(entity);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  private static void registerFlag(Flag<?> flag) {
    var registry = WorldGuard.getInstance().getFlagRegistry();
    try {
      registry.register(flag);
    } catch (FlagConflictException e) {
      throw new IllegalStateException(e);
    }
  }

  private static Entity toEntity(org.bukkit.entity.Entity entity) {
    try {
      return (Entity) getHandle.invoke(entity);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  private static Object getEffect(MobEffectInstance effectInstance) {
    try {
      return getEffect.invoke(effectInstance);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  private static boolean hasEffect(LivingEntity entity, Object effect) {
    try {
      return (boolean) hasEffect.invoke(entity, effect);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  private static boolean removeEffect(LivingEntity entity, Object effect) {
    try {
      return (boolean) removeEffect.invoke(entity, effect);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public static Player toEntity(LocalPlayer player) {
    return (Player) toEntity(((BukkitPlayer) player).getPlayer());
  }
}
