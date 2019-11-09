package com.craftingdead.mod.potion;

import com.craftingdead.mod.capability.ModCapabilities;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class HydrateEffect extends Effect {

  protected HydrateEffect() {
    super(EffectType.BENEFICIAL, 0x0077BE);
  }

  @Override
  public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource,
      LivingEntity entityLivingBaseIn, int amplifier, double health) {
    entityLivingBaseIn.getCapability(ModCapabilities.PLAYER).ifPresent((player) -> {
      player.setWater(player.getWater() + amplifier);
    });
  }

  @Override
  public boolean isInstant() {
    return true;
  }
}
