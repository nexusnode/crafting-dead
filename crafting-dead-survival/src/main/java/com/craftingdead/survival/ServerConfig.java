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

package com.craftingdead.survival;

import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {

  // ================================================================================
  // Loot Values
  // ================================================================================

  public final ForgeConfigSpec.BooleanValue lootEnabled;
  public final ForgeConfigSpec.BooleanValue civilianLootEnabled;
  public final ForgeConfigSpec.BooleanValue rareCivilianLootEnabled;
  public final ForgeConfigSpec.BooleanValue medicalLootEnabled;
  public final ForgeConfigSpec.BooleanValue policeLootEnabled;
  public final ForgeConfigSpec.BooleanValue militaryLootEnabled;
  public final ForgeConfigSpec.IntValue civilianLootRefreshDelayTicks;
  public final ForgeConfigSpec.IntValue rareCivilianLootRefreshDelayTicks;
  public final ForgeConfigSpec.IntValue medicalLootRefreshDelayTicks;
  public final ForgeConfigSpec.IntValue policeLootRefreshDelayTicks;
  public final ForgeConfigSpec.IntValue militaryLootRefreshDelayTicks;

  // ================================================================================
  // Zombies Values
  // ================================================================================

  public final ForgeConfigSpec.BooleanValue zombiesEnabled;
  public final ForgeConfigSpec.BooleanValue advancedZombiesEnabled;
  public final ForgeConfigSpec.BooleanValue tankZombiesEnabled;
  public final ForgeConfigSpec.BooleanValue fastZombiesEnabled;
  public final ForgeConfigSpec.BooleanValue weakZombiesEnabled;
  public final ForgeConfigSpec.DoubleValue advancedZombieMaxHealth;
  public final ForgeConfigSpec.DoubleValue tankZombieMaxHealth;
  public final ForgeConfigSpec.DoubleValue fastZombieMaxHealth;
  public final ForgeConfigSpec.DoubleValue weakZombieMaxHealth;
  public final ForgeConfigSpec.DoubleValue policeZombieMaxHealth;
  public final ForgeConfigSpec.DoubleValue doctorZombieMaxHealth;
  public final ForgeConfigSpec.DoubleValue giantZombieMaxHealth;
  public final ForgeConfigSpec.DoubleValue advancedZombieAttackDamage;
  public final ForgeConfigSpec.DoubleValue tankZombieAttackDamage;
  public final ForgeConfigSpec.DoubleValue fastZombieAttackDamage;
  public final ForgeConfigSpec.DoubleValue weakZombieAttackDamage;
  public final ForgeConfigSpec.DoubleValue policeZombieAttackDamage;
  public final ForgeConfigSpec.DoubleValue doctorZombieAttackDamage;
  public final ForgeConfigSpec.DoubleValue giantZombieAttackDamage;
  public final ForgeConfigSpec.IntValue advancedZombieSpawnWeight;
  public final ForgeConfigSpec.IntValue tankZombieSpawnWeight;
  public final ForgeConfigSpec.IntValue fastZombieSpawnWeight;
  public final ForgeConfigSpec.IntValue weakZombieSpawnWeight;
  public final ForgeConfigSpec.IntValue advancedZombieMinSpawn;
  public final ForgeConfigSpec.IntValue tankZombieMinSpawn;
  public final ForgeConfigSpec.IntValue fastZombieMinSpawn;
  public final ForgeConfigSpec.IntValue weakZombieMinSpawn;
  public final ForgeConfigSpec.IntValue advancedZombieMaxSpawn;
  public final ForgeConfigSpec.IntValue tankZombieMaxSpawn;
  public final ForgeConfigSpec.IntValue fastZombieMaxSpawn;
  public final ForgeConfigSpec.IntValue weakZombieMaxSpawn;
  public final ForgeConfigSpec.DoubleValue zombieHatSpawnChance;
  public final ForgeConfigSpec.DoubleValue zombieHandSpawnChance;
  public final ForgeConfigSpec.DoubleValue zombieClothingSpawnChance;
  public final ForgeConfigSpec.DoubleValue zombieHatDropChance;
  public final ForgeConfigSpec.DoubleValue zombieHandDropChance;
  public final ForgeConfigSpec.DoubleValue zombieClothingDropChance;
  public final ForgeConfigSpec.DoubleValue zombieAttackKnockback;
  public final ForgeConfigSpec.DoubleValue fastZombieSpeed;

  // ================================================================================
  // Abilities Values
  // ================================================================================

  public final ForgeConfigSpec.BooleanValue brokenLegsEnabled;
  public final ForgeConfigSpec.DoubleValue brokenLegChance;
  public final ForgeConfigSpec.BooleanValue bleedingEnabled;
  public final ForgeConfigSpec.BooleanValue infectionEnabled;

  // ================================================================================
  // Explosives Values
  // ================================================================================

  public final ForgeConfigSpec.BooleanValue pipeBombEnabled;
  public final ForgeConfigSpec.EnumValue<Explosion.BlockInteraction> pipeBombBlockInteraction;
  public final ForgeConfigSpec.DoubleValue pipeBombRadius;
  public final ForgeConfigSpec.DoubleValue pipeBombKnockbackMultiplier;
  public final ForgeConfigSpec.DoubleValue pipeBombDamageMultiplier;
  public final ForgeConfigSpec.IntValue pipeBombTicksBeforeActivation;

  public ServerConfig(ForgeConfigSpec.Builder builder) {
    // Loot configuration
    builder
        .comment("Tweak loot spawning and delays")
        .push("loot");
    {
      this.lootEnabled = builder
          .translation("options.craftingdeadsurvival.server.loot.enable")
          .comment("Defines if loot can be respawned (applies to all loots)")
          .define("lootEnabled", true);
      this.civilianLootEnabled = builder
          .translation("options.craftingdeadsurvival.server.loot.civilian_loot")
          .comment("Defines if Civilian Loot can be respawned")
          .define("civilianLootEnabled", true);
      this.rareCivilianLootEnabled = builder
          .translation("options.craftingdeadsurvival.server.loot.civilian_rare_loot")
          .comment("Defines if Civilian Rare Loot can be respawned")
          .define("rareCivilianLootEnabled", true);
      this.medicalLootEnabled = builder
          .translation("options.craftingdeadsurvival.server.loot.medical_loot")
          .comment("Defines if Medical Loot can be respawned")
          .define("medicalLootEnabled", true);
      this.policeLootEnabled = builder
          .translation("options.craftingdeadsurvival.server.loot.police_loot")
          .comment("Defines if Police Loot can be respawned")
          .define("policeLootEnabled", true);
      this.militaryLootEnabled = builder
          .translation("options.craftingdeadsurvival.server.loot.military_loot")
          .comment("Defines if Military Loot can be respawned")
          .define("militaryLootEnabled", true);
      this.civilianLootRefreshDelayTicks = builder
          .translation("options.craftingdeadsurvival.server.loot.civilian_loot_respawn_tick")
          .defineInRange("civilianLootRefreshDelayTicks", 1000, 0, Integer.MAX_VALUE);
      this.rareCivilianLootRefreshDelayTicks = builder
          .translation("options.craftingdeadsurvival.server.loot.civilian_rare_loot_respawn_tick")
          .defineInRange("rareCivilianLootRefreshDelayTicks", 1000, 0, Integer.MAX_VALUE);
      this.medicalLootRefreshDelayTicks = builder
          .translation("options.craftingdeadsurvival.server.loot.medical_loot_respawn_tick")
          .defineInRange("medicalLootRefreshDelayTicks", 1000, 0, Integer.MAX_VALUE);
      this.policeLootRefreshDelayTicks = builder
          .translation("options.craftingdeadsurvival.server.loot.police_loot_respawn_tick")
          .defineInRange("policeLootRefreshDelayTicks", 1000, 0, Integer.MAX_VALUE);
      this.militaryLootRefreshDelayTicks = builder
          .translation("options.craftingdeadsurvival.server.loot.military_loot_respawn_tick")
          .defineInRange("militaryLootRefreshDelayTicks", 1000, 0, Integer.MAX_VALUE);
    }
    builder.pop();

    // Zombies configuration
    builder
        .comment("Change every aspect of all zombies",
            "WARNING: Most changes only affects newly spawned zombies. Previously spawned zombies will retain their old settings.")
        .push("zombies");
    {
      this.advancedZombieMaxHealth = builder
          .translation("options.craftingdeadsurvival.server.zombies.advanced_zombie.health")
          .comment("Defines how much health the zombie has (2 health points = 1 heart)")
          .defineInRange("advancedZombieMaxHealth", 20.0D, 1.0D, 1024.0D);
      this.tankZombieMaxHealth = builder
          .translation("options.craftingdeadsurvival.server.zombies.tank_zombie.health")
          .comment("Defines how much health the zombie has (2 health points = 1 heart)")
          .defineInRange("tankZombieMaxHealth", 100.0D, 1.0D, 1024.0D);
      this.fastZombieMaxHealth = builder
          .translation("options.craftingdeadsurvival.server.zombies.fast_zombie.health")
          .comment("Defines how much health the zombie has (2 health points = 1 heart)")
          .defineInRange("fastZombieMaxHealth", 10.0D, 1.0D, 1024.0D);
      this.weakZombieMaxHealth = builder
          .translation("options.craftingdeadsurvival.server.zombies.weak_zombie.health")
          .comment("Defines how much health the zombie has (2 health points = 1 heart)")
          .defineInRange("weakZombieMaxHealth", 5.0D, 1.0D, 1024.0D);
      this.policeZombieMaxHealth = builder
          .translation("options.craftingdeadsurvival.server.zombies.police_zombie.health")
          .comment("Defines how much health the zombie has (2 health points = 1 heart)")
          .defineInRange("policeZombieMaxHealth", 20.0D, 1.0D, 1024.0D);
      this.doctorZombieMaxHealth = builder
          .translation("options.craftingdeadsurvival.server.zombies.doctor_zombie.health")
          .comment("Defines how much health the zombie has (2 health points = 1 heart)")
          .defineInRange("doctorZombieMaxHealth", 20.0D, 1.0D, 1024.0D);
      this.giantZombieMaxHealth = builder
          .translation("options.craftingdeadsurvival.server.zombies.giant_zombie.health")
          .comment("Defines how much health the zombie has (2 health points = 1 heart)")
          .defineInRange("giantZombieMaxHealth", 100.0D, 1.0D, 1024.0D);
      this.advancedZombieAttackDamage = builder
          .translation("options.craftingdeadsurvival.server.zombies.advanced_zombie.damage")
          .comment("Defines how much damage the zombie deals (2 damage points points = 1 heart)")
          .defineInRange("advancedZombieAttackDamage", 3.0D, 0.0D, 2048.0D);
      this.tankZombieAttackDamage = builder
          .translation("options.craftingdeadsurvival.server.zombies.tank_zombie.damage")
          .comment("Defines how much damage the zombie deals (2 damage points points = 1 heart)")
          .defineInRange("tankZombieAttackDamage", 15.0D, 0.0D, 2048.0D);
      this.fastZombieAttackDamage = builder
          .translation("options.craftingdeadsurvival.server.zombies.fast_zombie.damage")
          .comment("Defines how much damage the zombie deals (2 damage points points = 1 heart)")
          .defineInRange("fastZombieAttackDamage", 1.0D, 0.0D, 2048.0D);
      this.weakZombieAttackDamage = builder
          .translation("options.craftingdeadsurvival.server.zombies.weak_zombie.damage")
          .comment("Defines how much damage the zombie deals (2 damage points points = 1 heart)")
          .defineInRange("weakZombieAttackDamage", 2.0D, 0.0D, 2048.0D);
      this.policeZombieAttackDamage = builder
          .translation("options.craftingdeadsurvival.server.zombies.police_zombie.damage")
          .comment("Defines how much damage the zombie deals (2 damage points points = 1 heart)")
          .defineInRange("policeZombieAttackDamage", 3.0D, 0.0D, 2048.0D);
      this.doctorZombieAttackDamage = builder
          .translation("options.craftingdeadsurvival.server.zombies.doctor_zombie.damage")
          .comment("Defines how much damage the zombie deals (2 damage points points = 1 heart)")
          .defineInRange("doctorZombieAttackDamage", 3.0D, 0.0D, 2048.0D);
      this.giantZombieAttackDamage = builder
          .translation("options.craftingdeadsurvival.server.zombies.giant_zombie.damage")
          .comment("Defines how much damage the zombie deals (2 damage points points = 1 heart)")
          .defineInRange("giantZombieAttackDamage", 50.0D, 0.0D, 2048.0D);
      this.fastZombieSpeed = builder
              .translation("options.craftingdeadsurvival.server.zombies.fast_zombie.speed")
              .comment("Defines how fast the zombie moves")
              .defineInRange("fastZombieSpeed", 0.33D, 0.0D, 2048.0D);

      builder
          .comment("Configure how zombies should spawn",
              "Minecraft's spawning is a weighted conditional system",
              "With a lower weight, rarer the zombie will be",
              "---------------------------------",
              "Minimum/Maximum spawn defines how much mobs will be spawned per group")
          .push("spawning");
      this.zombiesEnabled = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.enable")
          .define("zombiesEnabled", true);
      this.advancedZombiesEnabled = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.advanced_zombie.enable")
          .define("advancedZombiesEnabled", true);
      this.tankZombiesEnabled = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.tank_zombie.enable")
          .define("tankZombiesEnabled", true);
      this.fastZombiesEnabled = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.fast_zombie.enable")
          .define("fastZombiesEnabled", true);
      this.weakZombiesEnabled = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.weak_zombie.enable")
          .define("weakZombiesEnabled", true);
      this.advancedZombieSpawnWeight = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.advanced_zombie.weight")
          .defineInRange("advancedZombieSpawnWeight", 40, 1, Integer.MAX_VALUE);
      this.tankZombieSpawnWeight = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.tank_zombie.weight")
          .defineInRange("tankZombieSpawnWeight", 5, 1, Integer.MAX_VALUE);
      this.fastZombieSpawnWeight = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.fast_zombie.weight")
          .defineInRange("fastZombieSpawnWeight", 15, 1, Integer.MAX_VALUE);
      this.weakZombieSpawnWeight = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.weak_zombie.weight")
          .defineInRange("weakZombieSpawnWeight", 30, 1, Integer.MAX_VALUE);
      this.advancedZombieMinSpawn = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.advanced_zombie.min_spawn")
          .defineInRange("advancedZombieMinSpawn", 2, 1, Integer.MAX_VALUE);
      this.tankZombieMinSpawn = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.tank_zombie.min_spawn")
          .defineInRange("tankZombieMinSpawn", 2, 1, Integer.MAX_VALUE);
      this.fastZombieMinSpawn = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.fast_zombie.min_spawn")
          .defineInRange("fastZombieMinSpawn", 2, 1, Integer.MAX_VALUE);
      this.weakZombieMinSpawn = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.weak_zombie.min_spawn")
          .defineInRange("weakZombieMinSpawn", 2, 1, Integer.MAX_VALUE);
      this.advancedZombieMaxSpawn = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.advanced_zombie.max_spawn")
          .defineInRange("advancedZombieMaxSpawn", 8, 1, Integer.MAX_VALUE);
      this.tankZombieMaxSpawn = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.tank_zombie.max_spawn")
          .defineInRange("tankZombieMaxSpawn", 4, 1, Integer.MAX_VALUE);
      this.fastZombieMaxSpawn = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.fast_zombie.max_spawn")
          .defineInRange("fastZombieMaxSpawn", 4, 1, Integer.MAX_VALUE);
      this.weakZombieMaxSpawn = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.weak_zombie.max_spawn")
          .defineInRange("weakZombieMaxSpawn", 12, 1, Integer.MAX_VALUE);
      builder.pop();

      builder.push("misc");
      this.zombieHatSpawnChance = builder
          .translation("options.craftingdeadsurvival.server.zombies.misc.zombie_hat_spawn_chance")
          .comment("Spawn chance percentage (1.0 = 100% chance)")
          .defineInRange("zombieHatSpawnChance", 0.05D, 0D, 1.0D);
      this.zombieHandSpawnChance = builder
          .translation("options.craftingdeadsurvival.server.zombies.misc.zombie_hand_spawn_chance")
          .comment("Spawn chance percentage (1.0 = 100% chance)")
          .defineInRange("zombieHandSpawnChance", 0.15D, 0D, 1.0D);
      this.zombieClothingSpawnChance = builder
          .translation("options.craftingdeadsurvival.server.zombies.misc.zombie_clothing_spawn_chance")
          .comment("Spawn chance percentage (1.0 = 100% chance)")
          .defineInRange("zombieClothingSpawnChance", 0.25D, 0D, 1.0D);
      this.zombieHatDropChance = builder
          .translation("options.craftingdeadsurvival.server.zombies.misc.zombie_hat_drop_chance")
          .comment("Drop chance percentage (drop chance based on vanilla formula, use 2.0 for guarantee drop)")
          .defineInRange("zombieHatDropChance", 2.0D, 0D, 2.0D);
      this.zombieHandDropChance = builder
          .translation("options.craftingdeadsurvival.server.zombies.misc.zombie_hand_drop_chance")
          .comment("Drop chance percentage (drop chance based on vanilla formula, use 2.0 for guarantee drop)")
          .defineInRange("zombieHandDropChance", 0.085D, 0D, 2.0D);
      this.zombieClothingDropChance = builder
          .translation("options.craftingdeadsurvival.server.zombies.misc.zombie_clothing_drop_chance")
          .comment("Drop chance percentage (drop chance based on vanilla formula, use 2.0 for guarantee drop)")
          .defineInRange("zombieClothingDropChance", 2.00D, 0D, 2.0D);
      this.zombieAttackKnockback = builder
          .translation("options.craftingdeadsurvival.server.zombies.misc.attack_knockback")
          .comment("Additional knockback given to all zombies")
          .defineInRange("zombieAttackKnockback", 0D, 0D, 5.0D);
      builder.pop();
    }
    builder.pop();

    // Abilities configuration
    builder
        .comment("Allows toggling some gameplay aspects")
        .push("abilities");
    {
      this.brokenLegsEnabled = builder
          .translation("options.craftingdeadsurvival.server.abilities.broken_leg")
          .comment("Defines if players can break their legs")
          .define("brokenLegsEnabled", true);
      this.brokenLegChance = builder
          .translation("options.craftingdeadsurvival.server.abilities.broken_leg.chance")
          .comment("Defines the chance of the player breaking his leg")
          .defineInRange("brokenLegChance", 0.25F, 0.01F, 0.50F);
      this.bleedingEnabled = builder
          .translation("options.craftingdeadsurvival.server.abilities.bleed_effect")
          .comment("Defines if players can bleed")
          .define("bleedingEnabled", true);
      this.infectionEnabled = builder
          .translation("options.craftingdeadsurvival.server.abilities.infection_effect")
          .comment("Defines if players can be infected")
          .define("infectionEnabled", true);
    }
    builder.pop();

    // Explosives configuration
    builder.push("explosives");
    {
      this.pipeBombEnabled = builder
          .translation("options.craftingdeadsurvival.server.explosives.pipe_bomb.enable")
          .comment("Enables the usage of Pipe Bomb",
              "It wont prevent the ability to get Pipe Bombs, only the ability to use it")
          .define("pipeBombEnabled", true);
      this.pipeBombBlockInteraction = builder
          .translation("options.craftingdeadsurvival.server.explosives.pipe_grenade.mode")
          .comment("Defines how the explosion should interact with blocks",
              "NONE: No block interaction, blocks will remain unchanged",
              "BREAK: Blocks are broken, they will be dropped when exploded",
              "DESTROY: Blocks are destroyed, nothing will be dropped and only a crater will be left")
          .defineEnum("pipeBombBlockInteraction", BlockInteraction.NONE);
      this.pipeBombRadius = builder
          .translation("options.craftingdeadsurvival.server.explosives.pipe_bomb.radius")
          .comment("The explosion radius (in blocks), it tells how big the explosion should be")
          .defineInRange("pipeBombRadius", 4D, 0.1D, 50D);
      this.pipeBombKnockbackMultiplier = builder
          .translation("options.craftingdeadsurvival.server.explosives.pipe_bomb.knockback")
          .comment("Defines how strong the explosion knockback should be (Multiplier)")
          .defineInRange("pipeBombKnockbackMultiplier", 1D, 0D, 30D);
      this.pipeBombDamageMultiplier = builder
          .translation("options.craftingdeadsurvival.server.explosives.pipe_bomb.damage")
          .comment("Multiplies the base damage given by the explosion (Multiplier)")
          .defineInRange("pipeBombDamageMultiplier", 1D, 0D, 30D);
      this.pipeBombTicksBeforeActivation = builder
          .translation("options.craftingdeadsurvival.server.explosives.pipe_bomb.activation_tick")
          .comment("How long before the bomb activates automatically (Ticks)")
          .defineInRange("pipeBombTicksBeforeActivation", 100, 0, 18000);
    }
    builder.pop();
  }
}
