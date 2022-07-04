package com.craftingdead.immerse.world;

import net.minecraft.world.damagesource.DamageSource;

public class ImmerseDamageSource {

  public static final DamageSource DEHYDRATION =
      new DamageSource("dehydration").bypassArmor();
}
