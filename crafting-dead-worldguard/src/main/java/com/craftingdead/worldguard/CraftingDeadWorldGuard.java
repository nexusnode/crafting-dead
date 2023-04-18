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
import java.util.Optional;
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
  private static final MethodHandle getWorldHandle;
  private static final MethodHandle getBukkitEntityHandle;

  private StateFlag brokenLegs;
  private StateFlag bleeding;
  private StateFlag thirst;
  private StateFlag shooting;
  private StateFlag grenadeThrowing;

  static {
    try {
      var craftWorld = Class.forName("org.bukkit.craftbukkit.v1_18_R2.CraftWorld");
      var lookup = MethodHandles.publicLookup();
      getWorldHandle =
          lookup.findVirtual(Level.class, "getWorld", MethodType.methodType(craftWorld));

      var craftEntity = Class.forName("org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity");
      getBukkitEntityHandle =
          lookup.findVirtual(Entity.class, "getBukkitEntity", MethodType.methodType(craftEntity));
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  @Override
  public void onLoad() {
    logger.info("Loading Crafting Dead WorldGuard...");
    registerFlag("broken-legs", true)
        .ifPresent(flag -> {
          this.brokenLegs = flag;
          MinecraftForge.EVENT_BUS.addListener(this::handleBreakLeg);
        });
    registerFlag("bleeding", true)
        .ifPresent(flag -> {
          this.bleeding = flag;
          MinecraftForge.EVENT_BUS.addListener(this::handleBleed);
        });
    registerFlag("thirst", true)
        .ifPresent(flag -> {
          this.thirst = flag;
          MinecraftForge.EVENT_BUS.addListener(this::handleWaterDecay);
        });
    registerFlag("shooting", true)
        .ifPresent(flag -> {
          this.shooting = flag;
          MinecraftForge.EVENT_BUS.addListener(this::handleGunEntityHit);
          MinecraftForge.EVENT_BUS.addListener(this::handleGunBlockHit);
        });
    registerFlag("grenade-throwing", true)
        .ifPresent(flag -> {
          this.grenadeThrowing = flag;
          MinecraftForge.EVENT_BUS.addListener(this::handleGrenadeThrow);
        });
    logger.info("Crafting Dead WorldGuard loaded.");
  }

  private void handleBreakLeg(BreakLegEvent event) {
    var localPlayer = toWorldGuardPlayer(event.getPlayer());
    var regions = getApplicableRegions(event.getPlayer());
    event.setCanceled(!regions.testState(localPlayer, this.brokenLegs));
  }

  private void handleBleed(BleedEvent event) {
    var localPlayer = toWorldGuardPlayer(event.getPlayer());
    var regions = getApplicableRegions(event.getPlayer());
    event.setCanceled(!regions.testState(localPlayer, this.bleeding));
  }

  private void handleWaterDecay(WaterDecayEvent event) {
    var localPlayer = toWorldGuardPlayer(event.getPlayer());
    var regions = getApplicableRegions(event.getPlayer());
    event.setCanceled(!regions.testState(localPlayer, this.thirst));
  }

  private void handleGunEntityHit(GunEvent.EntityHit event) {
    if (event.living() instanceof PlayerExtension<?> player) {
      var localPlayer = toWorldGuardPlayer(player.entity());
      var regions = getApplicableRegions(player.entity());
      event.setCanceled(!regions.testState(localPlayer, this.shooting));
    }
  }

  private void handleGunBlockHit(GunEvent.BlockHit event) {
    if (event.living() instanceof PlayerExtension<?> player) {
      var localPlayer = toWorldGuardPlayer(player.entity());
      var regions = getApplicableRegions(player.entity());
      event.setCanceled(!regions.testState(localPlayer, this.shooting));
    }
  }

  private void handleGrenadeThrow(GrenadeThrowEvent event) {
    var localPlayer = toWorldGuardPlayer(event.getPlayer());
    var regions = getApplicableRegions(event.getPlayer());
    event.setCanceled(!regions.testState(localPlayer, this.grenadeThrowing));
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
      return (World) getWorldHandle.invoke(level);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  private static org.bukkit.entity.Entity adapt(Entity entity) {
    try {
      return (org.bukkit.entity.Entity) getBukkitEntityHandle.invoke(entity);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  private static Optional<StateFlag> registerFlag(String name, boolean def) {
    return registerFlag(new StateFlag(name, def), StateFlag.class);
  }

  private static <T extends Flag<?>> Optional<T> registerFlag(T flag, Class<T> type) {
    var registry = WorldGuard.getInstance().getFlagRegistry();
    try {
      registry.register(flag);
      return Optional.of(flag);
    } catch (FlagConflictException e) {
      var existing = registry.get(flag.getName());
      if (type.isInstance(existing)) {
        return Optional.of(type.cast(existing));
      }
    }
    return Optional.empty();
  }
}
