package com.craftingdead.mod;

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
