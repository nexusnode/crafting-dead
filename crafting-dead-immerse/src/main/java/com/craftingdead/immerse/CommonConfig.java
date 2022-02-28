package com.craftingdead.immerse;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

  public final ForgeConfigSpec.BooleanValue sentryEnabled;
  public final ForgeConfigSpec.ConfigValue<String> sentryDsn;

  public CommonConfig(ForgeConfigSpec.Builder builder) {
    this.sentryEnabled = builder.define("sentryEnabled", () -> true);
    this.sentryDsn = builder.comment("Internal").define("sentryDsn",
        "https://31d8ac34b0c24ddf98223098d42fd526@o1128514.ingest.sentry.io/6174174");
  }
}
