package com.craftingdead.mod.potion;

import javax.annotation.Nullable;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.player.IPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.InstantEffect;

public class HydrateEffect extends InstantEffect {

  protected HydrateEffect() {
    super(EffectType.BENEFICIAL, 0x0077BE);
  }

  @Override
  public void performEffect(LivingEntity livingEntity, int amplifier) {
    livingEntity
        .getCapability(ModCapabilities.LIVING)
        .filter(living -> living instanceof IPlayer)
        .map(living -> (IPlayer<?>) living)
        .ifPresent(player -> player.setWater(player.getWater() + amplifier + 1));
  }

  @Override
  public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource,
      LivingEntity entityLivingBaseIn, int amplifier, double health) {
    this.performEffect(entityLivingBaseIn, amplifier);
  }

  @Override
  public boolean shouldRender(EffectInstance effect) {
    return false;
  }

  @Override
  public boolean shouldRenderInvText(EffectInstance effect) {
    return false;
  }

  @Override
  public boolean shouldRenderHUD(EffectInstance effect) {
    return false;
  }
}
