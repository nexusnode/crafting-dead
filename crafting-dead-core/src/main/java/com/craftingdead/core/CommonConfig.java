/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

  public final ForgeConfigSpec.ConfigValue<String> masterServerHost;

  public final ForgeConfigSpec.IntValue masterServerPort;

  public CommonConfig(ForgeConfigSpec.Builder builder) {
    builder.push("common");
    {
      this.masterServerHost = builder //
          .translation("options.craftingdead.common.master_server_host") //
          .define("masterServerHost", "localhost");
      this.masterServerPort = builder //
          .translation("options.craftingdead.common.master_server_port") //
          .defineInRange("masterServerPort", 25578, 0, 65535);
    }
    builder.pop();
  }
}
