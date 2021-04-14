package com.craftingdead.core;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {

  public final ForgeConfigSpec.BooleanValue hydrationEnabled;
  public final ForgeConfigSpec.BooleanValue brokenLegsEnabled;
  public final ForgeConfigSpec.BooleanValue bleedingEnabled;

  public ServerConfig(ForgeConfigSpec.Builder builder) {
    builder.push("server");
    {
      this.hydrationEnabled = builder
          .translation("options.craftingdead.server.hydration_enabled")
          .define("hydrationEnabled", true);
      this.brokenLegsEnabled = builder
          .translation("options.craftingdead.server.broken_legs_enabled")
          .define("brokenLegsEnabled", true);
      this.bleedingEnabled = builder
          .translation("options.craftingdead.server.bleeding_enabled")
          .define("bleedingEnabled", true);
    }
  }
}
