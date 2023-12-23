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
