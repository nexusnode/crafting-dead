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
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.craftingdead.core.event.GrenadeThrowEvent;
import com.craftingdead.core.event.GunEvent;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.event.WaterDecayEvent;
import com.craftingdead.survival.event.BleedEvent;
import com.craftingdead.survival.event.BreakLegEvent;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.BukkitPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

public class CraftingDeadWorldGuard extends JavaPlugin {

  private static final Logger logger = LoggerFactory.getLogger(CraftingDeadWorldGuard.class);
  private static final MethodHandle getWorld;
  private static final MethodHandle getBukkitEntity;
  private static final MethodHandle getHandle;

  public static final StateFlag BROKEN_LEGS = new StateFlag("broken-legs", true);
  public static final StateFlag BLEEDING = new StateFlag("bleeding", true);
  public static final StateFlag THIRST = new StateFlag("thirst", true);
  public static final StateFlag SHOOTING = new StateFlag("shooting", true);
  public static final StateFlag GRENADE_THROWING = new StateFlag("grenade-throwing", true);
  public static final StateFlag CLEAR_EQUIPMENT_ON_EXIT =
      new StateFlag("clear-equipment-on-exit", false);

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
    } catch (Throwable t) {
      throw new ExceptionInInitializerError(t);
    }
  }

  @Override
  public void onLoad() {
    logger.info("Loading Crafting Dead WorldGuard...");
    registerFlag(BROKEN_LEGS);
    registerFlag(BLEEDING);
    registerFlag(THIRST);
    registerFlag(SHOOTING);
    registerFlag(GRENADE_THROWING);
    registerFlag(CLEAR_EQUIPMENT_ON_EXIT);

    var sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
    sessionManager.registerHandler(ClearEquipmentOnExitFlag.FACTORY, null);

    var forgeBus = MinecraftForge.EVENT_BUS;
    forgeBus.addListener(this::handleBreakLeg);
    forgeBus.addListener(this::handleBleed);
    forgeBus.addListener(this::handleWaterDecay);
    forgeBus.addListener(this::handleGunEntityHit);
    forgeBus.addListener(this::handleGunBlockHit);
    forgeBus.addListener(this::handleGrenadeThrow);

    logger.info("Crafting Dead WorldGuard loaded.");
  }

  private void handleBreakLeg(BreakLegEvent event) {
    var localPlayer = toWorldGuardPlayer(event.getPlayer());
    var regions = getApplicableRegions(event.getPlayer());
    event.setCanceled(!regions.testState(localPlayer, BROKEN_LEGS));
  }

  private void handleBleed(BleedEvent event) {
    var localPlayer = toWorldGuardPlayer(event.getPlayer());
    var regions = getApplicableRegions(event.getPlayer());
    event.setCanceled(!regions.testState(localPlayer, BLEEDING));
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
      return (World) getWorld.invokeExact(level);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  private static org.bukkit.entity.Entity adapt(Entity entity) {
    try {
      return (org.bukkit.entity.Entity) getBukkitEntity.invokeExact(entity);
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
      return (Entity) getHandle.invokeExact(entity);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public static Player toEntity(LocalPlayer player) {
    return (Player) toEntity(((BukkitPlayer) player).getPlayer());
  }
}
