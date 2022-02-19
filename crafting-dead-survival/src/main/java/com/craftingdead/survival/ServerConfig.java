/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
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
  public final ForgeConfigSpec.BooleanValue lootCivilianLootEnabled;
  public final ForgeConfigSpec.BooleanValue lootCivilianRareLootEnabled;
  public final ForgeConfigSpec.BooleanValue lootMedicalLootEnabled;
  public final ForgeConfigSpec.BooleanValue lootPoliceLootEnabled;
  public final ForgeConfigSpec.BooleanValue lootMilitaryLootEnabled;
  public final ForgeConfigSpec.IntValue lootCivilianRespawnTick;
  public final ForgeConfigSpec.IntValue lootCivilianRareRespawnTick;
  public final ForgeConfigSpec.IntValue lootMedicalRespawnTick;
  public final ForgeConfigSpec.IntValue lootPoliceRespawnTick;
  public final ForgeConfigSpec.IntValue lootMilitaryRespawnTick;

  // ================================================================================
  // Zombies Values
  // ================================================================================

  public final ForgeConfigSpec.BooleanValue zombiesEnabled;
  public final ForgeConfigSpec.BooleanValue zombiesBabyZombies;
  public final ForgeConfigSpec.BooleanValue zombiesAdvancedZombie;
  public final ForgeConfigSpec.BooleanValue zombiesTankZombie;
  public final ForgeConfigSpec.BooleanValue zombiesFastZombie;
  public final ForgeConfigSpec.BooleanValue zombiesWeakZombie;
  public final ForgeConfigSpec.DoubleValue zombiesAdvancedZombieHealth;
  public final ForgeConfigSpec.DoubleValue zombiesTankZombieHealth;
  public final ForgeConfigSpec.DoubleValue zombiesFastZombieHealth;
  public final ForgeConfigSpec.DoubleValue zombiesWeakZombieHealth;
  public final ForgeConfigSpec.DoubleValue zombiesPoliceZombieHealth;
  public final ForgeConfigSpec.DoubleValue zombiesDoctorZombieHealth;
  public final ForgeConfigSpec.DoubleValue zombiesGiantZombieHealth;
  public final ForgeConfigSpec.DoubleValue zombiesAdvancedZombieDamage;
  public final ForgeConfigSpec.DoubleValue zombiesTankZombieDamage;
  public final ForgeConfigSpec.DoubleValue zombiesFastZombieDamage;
  public final ForgeConfigSpec.DoubleValue zombiesWeakZombieDamage;
  public final ForgeConfigSpec.DoubleValue zombiesPoliceZombieDamage;
  public final ForgeConfigSpec.DoubleValue zombiesDoctorZombieDamage;
  public final ForgeConfigSpec.DoubleValue zombiesGiantZombieDamage;
  public final ForgeConfigSpec.IntValue zombiesSpawnAdvancedZombieSpawnWeight;
  public final ForgeConfigSpec.IntValue zombiesSpawnTankZombieSpawnWeight;
  public final ForgeConfigSpec.IntValue zombiesSpawnFastZombieSpawnWeight;
  public final ForgeConfigSpec.IntValue zombiesSpawnWeakZombieSpawnWeight;
  public final ForgeConfigSpec.IntValue zombiesSpawnAdvancedZombieMinSpawn;
  public final ForgeConfigSpec.IntValue zombiesSpawnTankZombieMinSpawn;
  public final ForgeConfigSpec.IntValue zombiesSpawnFastZombieMinSpawn;
  public final ForgeConfigSpec.IntValue zombiesSpawnWeakZombieMinSpawn;
  public final ForgeConfigSpec.IntValue zombiesSpawnAdvancedZombieMaxSpawn;
  public final ForgeConfigSpec.IntValue zombiesSpawnTankZombieMaxSpawn;
  public final ForgeConfigSpec.IntValue zombiesSpawnFastZombieMaxSpawn;
  public final ForgeConfigSpec.IntValue zombiesSpawnWeakZombieMaxSpawn;
  public final ForgeConfigSpec.DoubleValue zombiesAttackKnockback;
  public final ForgeConfigSpec.BooleanValue zombiesDeathDrops;

  // ================================================================================
  // Abilities Values
  // ================================================================================

  public final ForgeConfigSpec.BooleanValue abilitiesBrokenLegs;
  public final ForgeConfigSpec.BooleanValue abilitiesBleedEffect;
  public final ForgeConfigSpec.BooleanValue abilitiesInfectionEffect;

  // ================================================================================
  // Explosives Values
  // ================================================================================

  public final ForgeConfigSpec.BooleanValue explosivesPipeBombEnabled;
  public final ForgeConfigSpec.BooleanValue explosivesPipeGrenadeEnabled;
  public final ForgeConfigSpec.EnumValue<Explosion.BlockInteraction> explosivesPipeBombMode;
  public final ForgeConfigSpec.EnumValue<Explosion.BlockInteraction> explosivesPipeGrenadeMode;
  public final ForgeConfigSpec.DoubleValue explosivesPipeBombRadius;
  public final ForgeConfigSpec.DoubleValue explosivesPipeGrenadeRadius;
  public final ForgeConfigSpec.DoubleValue explosivesPipeBombKnockbackMultiplier;
  public final ForgeConfigSpec.DoubleValue explosivesPipeGrenadeKnockbackMultiplier;
  public final ForgeConfigSpec.DoubleValue explosivesPipeBombDamageMultiplier;
  public final ForgeConfigSpec.DoubleValue explosivesPipeGrenadeDamageMultiplier;
  public final ForgeConfigSpec.IntValue explosivesPipeBombTicksBeforeActivation;
  public final ForgeConfigSpec.IntValue explosivesPipeGrenadeTicksBeforeActivation;

  public ServerConfig(ForgeConfigSpec.Builder builder) {
    // Loot configuration
    builder
        .comment("Tweak loot spawning and delays")
        .push("loot");
    {
      this.lootEnabled = builder
          .translation("options.craftingdeadsurvival.server.loot.enable")
          .comment("Defines if loot can be respawned (applies to all loots)")
          .define("enabled", true);
      this.lootCivilianLootEnabled = builder
          .translation("options.craftingdeadsurvival.server.loot.civilian_loot")
          .comment("Defines if Civilian Loot can be respawned")
          .define("civilianLootEnabled", true);
      this.lootCivilianRareLootEnabled = builder
          .translation("options.craftingdeadsurvival.server.loot.civilian_rare_loot")
          .comment("Defines if Civilian Rare Loot can be respawned")
          .define("civilianRareLootEnabled", true);
      this.lootMedicalLootEnabled = builder
          .translation("options.craftingdeadsurvival.server.loot.medical_loot")
          .comment("Defines if Medical Loot can be respawned")
          .define("medicalLootEnabled", true);
      this.lootPoliceLootEnabled = builder
          .translation("options.craftingdeadsurvival.server.loot.police_loot")
          .comment("Defines if Police Loot can be respawned")
          .define("policeLootEnabled", true);
      this.lootMilitaryLootEnabled = builder
          .translation("options.craftingdeadsurvival.server.loot.military_loot")
          .comment("Defines if Military Loot can be respawned")
          .define("militaryLootEnabled", true);
      this.lootCivilianRespawnTick = builder
          .translation("options.craftingdeadsurvival.server.loot.civilian_loot_respawn_tick")
          .defineInRange("civilianRespawnTick", 1000, 0, Integer.MAX_VALUE);
      this.lootCivilianRareRespawnTick = builder
          .translation("options.craftingdeadsurvival.server.loot.civilian_rare_loot_respawn_tick")
          .defineInRange("civilianRareRespawnTick", 1000, 0, Integer.MAX_VALUE);
      this.lootMedicalRespawnTick = builder
          .translation("options.craftingdeadsurvival.server.loot.medical_loot_respawn_tick")
          .defineInRange("medicalRespawnTick", 1000, 0, Integer.MAX_VALUE);
      this.lootPoliceRespawnTick = builder
          .translation("options.craftingdeadsurvival.server.loot.police_loot_respawn_tick")
          .defineInRange("policeRespawnTick", 1000, 0, Integer.MAX_VALUE);
      this.lootMilitaryRespawnTick = builder
          .translation("options.craftingdeadsurvival.server.loot.military_loot_respawn_tick")
          .defineInRange("militaryRespawnTick", 1000, 0, Integer.MAX_VALUE);
    }
    builder.pop();

    // Zombies configuration
    builder
        .comment("Change every aspect of all zombies",
            "WARNING: Most changes only affects newly spawned zombies. Previously spawned zombies will retain their old settings.")
        .push("zombies");
    {
      this.zombiesAdvancedZombieHealth = builder
          .translation("options.craftingdeadsurvival.server.zombies.advanced_zombie.health")
          .comment("Defines how much health the zombie has (2 health points = 1 heart)")
          .defineInRange("advancedZombieHealth", 20.0D, 1.0D, 1024.0D);
      this.zombiesTankZombieHealth = builder
          .translation("options.craftingdeadsurvival.server.zombies.tank_zombie.health")
          .comment("Defines how much health the zombie has (2 health points = 1 heart)")
          .defineInRange("tankZombieHealth", 100.0D, 1.0D, 1024.0D);
      this.zombiesFastZombieHealth = builder
          .translation("options.craftingdeadsurvival.server.zombies.fast_zombie.health")
          .comment("Defines how much health the zombie has (2 health points = 1 heart)")
          .defineInRange("fastZombieHealth", 10.0D, 1.0D, 1024.0D);
      this.zombiesWeakZombieHealth = builder
          .translation("options.craftingdeadsurvival.server.zombies.weak_zombie.health")
          .comment("Defines how much health the zombie has (2 health points = 1 heart)")
          .defineInRange("weakZombieHealth", 5.0D, 1.0D, 1024.0D);
      this.zombiesPoliceZombieHealth = builder
          .translation("options.craftingdeadsurvival.server.zombies.police_zombie.health")
          .comment("Defines how much health the zombie has (2 health points = 1 heart)")
          .defineInRange("policeZombieHealth", 20.0D, 1.0D, 1024.0D);
      this.zombiesDoctorZombieHealth = builder
          .translation("options.craftingdeadsurvival.server.zombies.doctor_zombie.health")
          .comment("Defines how much health the zombie has (2 health points = 1 heart)")
          .defineInRange("doctorZombieHealth", 20.0D, 1.0D, 1024.0D);
      this.zombiesGiantZombieHealth = builder
          .translation("options.craftingdeadsurvival.server.zombies.giant_zombie.health")
          .comment("Defines how much health the zombie has (2 health points = 1 heart)")
          .defineInRange("giantZombieHealth", 100.0D, 1.0D, 1024.0D);
      this.zombiesAdvancedZombieDamage = builder
          .translation("options.craftingdeadsurvival.server.zombies.advanced_zombie.damage")
          .comment("Defines how much damage the zombie deals (2 damage points points = 1 heart)")
          .defineInRange("advancedZombieDamage", 3.0D, 0.0D, 2048.0D);
      this.zombiesTankZombieDamage = builder
          .translation("options.craftingdeadsurvival.server.zombies.tank_zombie.damage")
          .comment("Defines how much damage the zombie deals (2 damage points points = 1 heart)")
          .defineInRange("tankZombieDamage", 15.0D, 0.0D, 2048.0D);
      this.zombiesFastZombieDamage = builder
          .translation("options.craftingdeadsurvival.server.zombies.fast_zombie.damage")
          .comment("Defines how much damage the zombie deals (2 damage points points = 1 heart)")
          .defineInRange("fastZombieDamage", 1.0D, 0.0D, 2048.0D);
      this.zombiesWeakZombieDamage = builder
          .translation("options.craftingdeadsurvival.server.zombies.weak_zombie.damage")
          .comment("Defines how much damage the zombie deals (2 damage points points = 1 heart)")
          .defineInRange("weakZombieDamage", 2.0D, 0.0D, 2048.0D);
      this.zombiesPoliceZombieDamage = builder
          .translation("options.craftingdeadsurvival.server.zombies.police_zombie.damage")
          .comment("Defines how much damage the zombie deals (2 damage points points = 1 heart)")
          .defineInRange("policeZombieDamage", 3.0D, 0.0D, 2048.0D);
      this.zombiesDoctorZombieDamage = builder
          .translation("options.craftingdeadsurvival.server.zombies.doctor_zombie.damage")
          .comment("Defines how much damage the zombie deals (2 damage points points = 1 heart)")
          .defineInRange("doctorZombieDamage", 3.0D, 0.0D, 2048.0D);
      this.zombiesGiantZombieDamage = builder
          .translation("options.craftingdeadsurvival.server.zombies.giant_zombie.damage")
          .comment("Defines how much damage the zombie deals (2 damage points points = 1 heart)")
          .defineInRange("giantZombieDamage", 50.0D, 0.0D, 2048.0D);

      builder
          .comment("Configure how zombies should spawn",
              "Minecraft's spawning is a weighted conditional system",
              "With a lower weight, rarer the zombie will be",
              "---------------------------------",
              "Minimum/Maximum spawn defines how much mobs will be spawned per group")
          .push("spawning");
      this.zombiesEnabled = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.enable")
          .define("enabled", true);
      this.zombiesBabyZombies = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.baby_zombies.enable")
          .define("babyZombies", true);
      this.zombiesAdvancedZombie = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.advanced_zombie.enable")
          .define("advancedZombie", true);
      this.zombiesTankZombie = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.tank_zombie.enable")
          .define("tankZombie", true);
      this.zombiesFastZombie = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.fast_zombie.enable")
          .define("fastZombie", true);
      this.zombiesWeakZombie = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.weak_zombie.enable")
          .define("weakZombie", true);
      this.zombiesSpawnAdvancedZombieSpawnWeight = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.advanced_zombie.weight")
          .defineInRange("advancedZombieSpawnWeight", 40, 1, Integer.MAX_VALUE);
      this.zombiesSpawnTankZombieSpawnWeight = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.tank_zombie.weight")
          .defineInRange("tankZombieSpawnWeight", 5, 1, Integer.MAX_VALUE);
      this.zombiesSpawnFastZombieSpawnWeight = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.fast_zombie.weight")
          .defineInRange("fastZombieSpawnWeight", 15, 1, Integer.MAX_VALUE);
      this.zombiesSpawnWeakZombieSpawnWeight = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.weak_zombie.weight")
          .defineInRange("weakZombieSpawnWeight", 30, 1, Integer.MAX_VALUE);
      zombiesSpawnAdvancedZombieMinSpawn = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.advanced_zombie.min_spawn")
          .defineInRange("advancedZombieMinSpawn", 2, 1, Integer.MAX_VALUE);
      this.zombiesSpawnTankZombieMinSpawn = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.tank_zombie.min_spawn")
          .defineInRange("tankZombieMinSpawn", 2, 1, Integer.MAX_VALUE);
      this.zombiesSpawnFastZombieMinSpawn = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.fast_zombie.min_spawn")
          .defineInRange("fastZombieMinSpawn", 2, 1, Integer.MAX_VALUE);
      this.zombiesSpawnWeakZombieMinSpawn = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.weak_zombie.min_spawn")
          .defineInRange("weakZombieMinSpawn", 2, 1, Integer.MAX_VALUE);
      this.zombiesSpawnAdvancedZombieMaxSpawn = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.advanced_zombie.max_spawn")
          .defineInRange("advancedZombieMaxSpawn", 8, 1, Integer.MAX_VALUE);
      this.zombiesSpawnTankZombieMaxSpawn = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.tank_zombie.max_spawn")
          .defineInRange("tankZombieMaxSpawn", 4, 1, Integer.MAX_VALUE);
      this.zombiesSpawnFastZombieMaxSpawn = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.fast_zombie.max_spawn")
          .defineInRange("fastZombieMaxSpawn", 4, 1, Integer.MAX_VALUE);
      this.zombiesSpawnWeakZombieMaxSpawn = builder
          .translation("options.craftingdeadsurvival.server.zombies.spawning.weak_zombie.max_spawn")
          .defineInRange("weakZombieMaxSpawn", 12, 1, Integer.MAX_VALUE);
      builder.pop();

      builder.push("misc");
      this.zombiesAttackKnockback = builder
          .translation("options.craftingdeadsurvival.server.zombies.misc.attack_knockback")
          .comment("Additional knockback given to all zombies")
          .defineInRange("attackKnockback", 0D, 0D, 5.0D);
      this.zombiesDeathDrops = builder
          .translation("options.craftingdeadsurvival.server.zombies.misc.death_drops")
          .comment("If disabled, all zombies will not drop any loot")
          .define("deathDrops", true);
      builder.pop();
    }
    builder.pop();

    // Abilities configuration
    builder
        .comment("Allows toggling some gameplay aspects")
        .push("abilities");
    {
      this.abilitiesBrokenLegs = builder
          .translation("options.craftingdeadsurvival.server.abilities.broken_leg")
          .comment("Defines if players can break their legs")
          .define("brokenLegs", true);
      this.abilitiesBleedEffect = builder
          .translation("options.craftingdeadsurvival.server.abilities.bleed_effect")
          .comment("Defines if players can bleed")
          .define("bleedEffect", true);
      this.abilitiesInfectionEffect = builder
          .translation("options.craftingdeadsurvival.server.abilities.infection_effect")
          .comment("Defines if players can be infected")
          .define("infectionEffect", true);
    }
    builder.pop();

    // Explosives configuration
    builder.push("explosives");
    {
      this.explosivesPipeBombEnabled = builder
          .translation("options.craftingdeadsurvival.server.explosives.pipe_bomb.enable")
          .comment("Enables the usage of Pipe Bomb",
              "It wont prevent the ability to get Pipe Bombs, only the ability to use it")
          .define("pipeBombEnabled", true);
      this.explosivesPipeGrenadeEnabled = builder
          .translation("options.craftingdeadsurvival.server.explosives.pipe_grenade.enable")
          .comment("Enables the usage of Pipe Grenade",
              "It wont prevent the ability to get Pipe Grenades, only the ability to use it")
          .define("pipeGrenadeEnabled", true);
      this.explosivesPipeBombMode = builder
          .translation("options.craftingdeadsurvival.server.explosives.pipe_bomb.mode")
          .comment("Defines how the explosion should interact with blocks",
              "NONE: No block interaction, blocks will remain unchanged",
              "BREAK: Blocks are broken, they will be dropped when exploded",
              "DESTROY: Blocks are destroyed, nothing will be dropped and only a crater will be left")
          .defineEnum("pipeBombExplosionMode", BlockInteraction.NONE);
      this.explosivesPipeGrenadeMode = builder
          .translation("options.craftingdeadsurvival.server.explosives.pipe_grenade.mode")
          .comment("Defines how the explosion should interact with blocks",
              "NONE: No block interaction, blocks will remain unchanged",
              "BREAK: Blocks are broken, they will be dropped when exploded",
              "DESTROY: Blocks are destroyed, nothing will be dropped and only a crater will be left")
          .defineEnum("pipeGrenadeExplosionMode", BlockInteraction.NONE);
      this.explosivesPipeBombRadius = builder
          .translation("options.craftingdeadsurvival.server.explosives.pipe_bomb.radius")
          .comment("The explosion radius (in blocks), it tells how big the explosion should be")
          .defineInRange("pipeBombRadius", 4D, 0.1D, 50D);
      this.explosivesPipeGrenadeRadius = builder
          .translation("options.craftingdeadsurvival.server.explosives.pipe_grenade.radius")
          .comment("The explosion radius (in blocks), it tells how big the explosion should be")
          .defineInRange("pipeGrenadeRadius", 4D, 0.1D, 50D);
      this.explosivesPipeBombKnockbackMultiplier = builder
          .translation("options.craftingdeadsurvival.server.explosives.pipe_bomb.knockback")
          .comment("Defines how strong the explosion knockback should be (Multiplier)")
          .defineInRange("pipeBombKnockbackMultiplier", 1D, 0D, 30D);
      this.explosivesPipeGrenadeKnockbackMultiplier = builder
          .translation("options.craftingdeadsurvival.server.explosives.pipe_grenade.knockback")
          .comment("Defines how strong the explosion knockback should be (Multiplier)")
          .defineInRange("pipeGrenadeKnockbackMultiplier", 1D, 0D, 30D);
      this.explosivesPipeBombDamageMultiplier = builder
          .translation("options.craftingdeadsurvival.server.explosives.pipe_bomb.damage")
          .comment("Multiplies the base damage given by the explosion (Multiplier)")
          .defineInRange("pipeBombDamageMultiplier", 1D, 0D, 30D);
      this.explosivesPipeGrenadeDamageMultiplier = builder
          .translation("options.craftingdeadsurvival.server.explosives.pipe_grenade.damage")
          .comment("Multiplies the base damage given by the explosion (Multiplier)")
          .defineInRange("pipeGrenadeDamageMultiplier", 1D, 0D, 30D);
      this.explosivesPipeBombTicksBeforeActivation = builder
          .translation("options.craftingdeadsurvival.server.explosives.pipe_bomb.activation_tick")
          .comment("How long before the bomb activates automatically (Ticks)")
          .defineInRange("pipeBombTicksBeforeActivation", 100, 0, 18000);
      this.explosivesPipeGrenadeTicksBeforeActivation = builder
          .translation("options.craftingdeadsurvival.server.explosives.pipe_grenade.activation_tick")
          .comment("How long before the grenade activates automatically (Ticks)")
          .defineInRange("pipeGrenadeTicksBeforeActivation", 100, 0, 18000);
    }
    builder.pop();
  }
}
