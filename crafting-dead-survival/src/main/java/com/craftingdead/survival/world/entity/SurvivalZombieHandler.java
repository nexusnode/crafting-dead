/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.survival.world.entity;

import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.LivingHandler;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.world.entity.monster.AdvancedZombie;
import java.util.Collection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;

public class SurvivalZombieHandler implements LivingHandler {

  public static final ResourceLocation ID =
      new ResourceLocation(CraftingDeadSurvival.ID, "zombie_handler");

  private final LivingExtension<? extends AdvancedZombie, LivingHandler> zombie;

  public SurvivalZombieHandler(LivingExtension<? extends AdvancedZombie, LivingHandler> zombie) {
    this.zombie = zombie;
  }

  @Override
  public boolean handleDeathLoot(DamageSource cause, Collection<ItemEntity> loot) {
    return !CraftingDeadSurvival.serverConfig.zombiesDeathDrops.get();
  }

  @Override
  public void encode(FriendlyByteBuf out, boolean writeAll) { }

  @Override
  public void decode(FriendlyByteBuf in) { }

  @Override
  public boolean requiresSync() {
    return false;
  }
}
