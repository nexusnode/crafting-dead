/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.survival.client;

import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.ClientConfig;
import com.craftingdead.core.client.renderer.entity.grenade.CylinderGrenadeRenderer;
import com.craftingdead.core.client.util.RenderUtil;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.ModDist;
import com.craftingdead.survival.client.model.SupplyDropModel;
import com.craftingdead.survival.client.model.geom.SurvivalModelLayers;
import com.craftingdead.survival.client.renderer.entity.AdvancedZombieRenderer;
import com.craftingdead.survival.client.renderer.entity.GiantZombieRenderer;
import com.craftingdead.survival.client.renderer.entity.SupplyDropRenderer;
import com.craftingdead.survival.particles.SurvivalParticleTypes;
import com.craftingdead.survival.world.effect.SurvivalMobEffects;
import com.craftingdead.survival.world.entity.SurvivalEntityTypes;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientDist implements ModDist {

  public static final ClientConfig clientConfig;
  public static final ForgeConfigSpec clientConfigSpec;

  static {
    final Pair<ClientConfig, ForgeConfigSpec> clientConfigPair =
        new ForgeConfigSpec.Builder().configure(ClientConfig::new);
    clientConfigSpec = clientConfigPair.getRight();
    clientConfig = clientConfigPair.getLeft();
  }

  private static final ResourceLocation BLOOD =
      new ResourceLocation(CraftingDeadSurvival.ID, "textures/gui/blood.png");
  private static final ResourceLocation BLOOD_2 =
      new ResourceLocation(CraftingDeadSurvival.ID, "textures/gui/blood_2.png");

  private final Minecraft minecraft;

  public ClientDist() {
    this.minecraft = Minecraft.getInstance();
    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::handleEntityRenderers);
    modEventBus.addListener(this::handleParticleFactoryRegisterEvent);
    modEventBus.addListener(this::handleEntityRenderersLayerDefinitions);

    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, clientConfigSpec);

    MinecraftForge.EVENT_BUS.register(this);
  }

  private void handleEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(SurvivalEntityTypes.PIPE_GRENADE.get(),
        CylinderGrenadeRenderer::new);
    event.registerEntityRenderer(SurvivalEntityTypes.SUPPLY_DROP.get(),
        SupplyDropRenderer::new);
    event.registerEntityRenderer(SurvivalEntityTypes.ADVANCED_ZOMBIE.get(),
        AdvancedZombieRenderer::new);
    event.registerEntityRenderer(SurvivalEntityTypes.FAST_ZOMBIE.get(),
        AdvancedZombieRenderer::new);
    event.registerEntityRenderer(SurvivalEntityTypes.TANK_ZOMBIE.get(),
        AdvancedZombieRenderer::new);
    event.registerEntityRenderer(SurvivalEntityTypes.WEAK_ZOMBIE.get(),
        AdvancedZombieRenderer::new);
    event.registerEntityRenderer(SurvivalEntityTypes.POLICE_ZOMBIE.get(),
        AdvancedZombieRenderer::new);
    event.registerEntityRenderer(SurvivalEntityTypes.DOCTOR_ZOMBIE.get(),
        AdvancedZombieRenderer::new);
    event.registerEntityRenderer(SurvivalEntityTypes.GIANT_ZOMBIE.get(),
        GiantZombieRenderer::new);
  }

  private void handleEntityRenderersLayerDefinitions(
      EntityRenderersEvent.RegisterLayerDefinitions event) {
    event.registerLayerDefinition(SurvivalModelLayers.SUPPLY_DROP,
        SupplyDropModel::createBodyLayer);
  }

  private void handleParticleFactoryRegisterEvent(ParticleFactoryRegisterEvent event) {
    final var particleEngine = this.minecraft.particleEngine;
    particleEngine.register(SurvivalParticleTypes.MILITARY_LOOT_GEN.get(),
        SpellParticle.Provider::new);
    particleEngine.register(SurvivalParticleTypes.MEDIC_LOOT_GEN.get(),
        SpellParticle.Provider::new);
    particleEngine.register(SurvivalParticleTypes.CIVILIAN_LOOT_GEN.get(),
        SpellParticle.Provider::new);
    particleEngine.register(SurvivalParticleTypes.CIVILIAN_RARE_LOOT_GEN.get(),
        SpellParticle.Provider::new);
    particleEngine.register(SurvivalParticleTypes.POLICE_LOOT_GEN.get(),
        SpellParticle.Provider::new);
  }

  @SubscribeEvent
  public void handleRenderGameOverlayPre(RenderGameOverlayEvent.Pre event) {
    var player = CraftingDead.getInstance().getClientDist().getCameraPlayer();
    if (player == null) {
      return;
    }

    switch (event.getType()) {
      case ALL:
        // Only draw in survival
        if (this.minecraft.gameMode.canHurtPlayer() && !player.isCombatModeEnabled()) {
          float healthPercentage =
              player.getEntity().getHealth() / player.getEntity().getMaxHealth();
          if (clientConfig.displayBlood.get() && healthPercentage < 1.0F
              && player.getEntity().hasEffect(SurvivalMobEffects.BLEEDING.get())) {
            renderBlood(event.getWindow().getGuiScaledWidth(),
                event.getWindow().getGuiScaledHeight(), healthPercentage);
          }
        }
        break;
      default:
        break;
    }
  }

  private static void renderBlood(int width, int height, float healthPercentage) {
    RenderSystem.enableBlend();
    RenderSystem.setShaderTexture(0, healthPercentage <= 0.25F ? BLOOD_2 : BLOOD);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1 - healthPercentage);
    RenderUtil.blit(0, 0, width, height);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.disableBlend();
  }
}
