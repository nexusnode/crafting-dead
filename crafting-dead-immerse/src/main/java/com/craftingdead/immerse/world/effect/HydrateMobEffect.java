package com.craftingdead.immerse.world.effect;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.survival.SurvivalPlayerHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.EffectRenderer;

public class HydrateMobEffect extends InstantenousMobEffect {

  protected HydrateMobEffect() {
    super(MobEffectCategory.BENEFICIAL, 0x0077BE);
  }

  @Override
  public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
    if (livingEntity instanceof Player player) {
      var survivalHandler = PlayerExtension.getOrThrow(player)
          .getHandlerOrThrow(SurvivalPlayerHandler.TYPE);
      survivalHandler.setWater(survivalHandler.getWater() + amplifier + 1);
    }
  }

  @Override
  public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource,
      LivingEntity entityLivingBaseIn, int amplifier, double health) {
    this.applyEffectTick(entityLivingBaseIn, amplifier);
  }

  @Override
  public void initializeClient(Consumer<EffectRenderer> consumer) {
    consumer.accept(new EffectRenderer() {

      @Override
      public boolean shouldRender(MobEffectInstance effect) {
        return false;
      }

      @Override
      public boolean shouldRenderInvText(MobEffectInstance effect) {
        return false;
      }

      @Override
      public boolean shouldRenderHUD(MobEffectInstance effect) {
        return false;
      }

      @Override
      public void renderInventoryEffect(MobEffectInstance effectInstance,
          EffectRenderingInventoryScreen<?> gui, PoseStack poseStack, int x, int y, float z) {}

      @Override
      public void renderHUDEffect(MobEffectInstance effectInstance, GuiComponent gui,
          PoseStack poseStack, int x, int y, float z, float alpha) {}

    });
  }
}
