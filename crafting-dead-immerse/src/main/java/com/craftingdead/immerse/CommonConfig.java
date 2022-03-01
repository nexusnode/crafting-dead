/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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
