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

package com.craftingdead.survival;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {

  public final ForgeConfigSpec.BooleanValue brokenLegsEnabled;
  public final ForgeConfigSpec.BooleanValue bleedingEnabled;
  public final ForgeConfigSpec.BooleanValue disableZombies;
  public final ForgeConfigSpec.BooleanValue infectionEnabled;
  public final ForgeConfigSpec.IntValue militaryLootRefreshDelayTicks;
  public final ForgeConfigSpec.IntValue medicalLootRefreshDelayTicks;
  public final ForgeConfigSpec.IntValue civilianLootRefreshDelayTicks;
  public final ForgeConfigSpec.IntValue rareCivilianLootRefreshDelayTicks;
  public final ForgeConfigSpec.IntValue policeLootRefreshDelayTicks;

  public ServerConfig(ForgeConfigSpec.Builder builder) {

    this.brokenLegsEnabled = builder
        .translation("options.craftingdeadsurvival.server.broken_legs_enabled")
        .define("brokenLegsEnabled", true);
    this.bleedingEnabled = builder
        .translation("options.craftingdeadsurvival.server.bleeding_enabled")
        .define("bleedingEnabled", true);
    this.disableZombies = builder
        .translation("options.craftingdeadsurvival.server.disable_zombies")
        .define("disableZombies", false);
    this.infectionEnabled = builder
        .translation("options.craftingdeadsurvival.server.infection_enabled")
        .define("infectionEnabled", true);

    this.militaryLootRefreshDelayTicks = builder
        .translation("options.craftingdeadsurvival.server.military_loot_refresh_delay_ticks")
        .defineInRange("militaryLootRefreshDelayTicks", 1000, 0, Integer.MAX_VALUE);
    this.medicalLootRefreshDelayTicks = builder
        .translation("options.craftingdeadsurvival.server.medical_loot_refresh_delay_ticks")
        .defineInRange("medicalLootRefreshDelayTicks", 1000, 0, Integer.MAX_VALUE);
    this.civilianLootRefreshDelayTicks = builder
        .translation("options.craftingdeadsurvival.server.civilian_loot_refresh_delay_ticks")
        .defineInRange("civilianLootRefreshDelayTicks", 1000, 0, Integer.MAX_VALUE);
    this.rareCivilianLootRefreshDelayTicks = builder
        .translation("options.craftingdeadsurvival.server.rare_civilian_loot_refresh_delay_ticks")
        .defineInRange("rareCivilianLootRefreshDelayTicks", 1000, 0, Integer.MAX_VALUE);
    this.policeLootRefreshDelayTicks = builder
        .translation("options.craftingdeadsurvival.server.police_loot_refresh_delay_ticks")
        .defineInRange("policeLootRefreshDelayTicks", 1000, 0, Integer.MAX_VALUE);
  }
}
