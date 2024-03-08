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

package com.craftingdead.core;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
  
  public static final CommonConfig instance;
  public static final ForgeConfigSpec configSpec;

  static {
    var pair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
    configSpec = pair.getRight();
    instance = pair.getLeft();
  }

  public final ForgeConfigSpec.DoubleValue weakVestArmor;
  public final ForgeConfigSpec.DoubleValue weakVestArmorToughness;

  public final ForgeConfigSpec.DoubleValue strongVestArmor;
  public final ForgeConfigSpec.DoubleValue strongVestArmorToughness;

  private CommonConfig(ForgeConfigSpec.Builder builder) {
    this.weakVestArmor =
        builder.defineInRange("weakVestArmor", 10.0F, 0.0F, Float.MAX_VALUE);
    this.weakVestArmorToughness =
        builder.defineInRange("weakVestArmorToughness", 1.5F, 0.0F, Float.MAX_VALUE);

    this.strongVestArmor =
        builder.defineInRange("strongVestArmor", 20.0F, 0.0F, Float.MAX_VALUE);
    this.strongVestArmorToughness =
        builder.defineInRange("strongVestArmorToughness", 3.0F, 0.0F, Float.MAX_VALUE);
  }
}
