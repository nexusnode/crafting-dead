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

package com.craftingdead.survival.world.item;

import com.craftingdead.survival.world.entity.SupplyDrop;
import com.craftingdead.survival.world.entity.SurvivalEntityTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class SupplyDropRadioItem extends Item {

  private static final double SPAWN_HEIGHT_OFFSET = 25.0D;

  private final ResourceLocation lootTable;

  public SupplyDropRadioItem(SupplyDropRadioItem.Properties properties) {
    super(properties);
    this.lootTable = properties.lootTable;
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    var level = context.getLevel();
    var blockPos = context.getClickedPos();
    var itemStack = context.getItemInHand();
    var supplyDrop =
        new SupplyDrop(SurvivalEntityTypes.SUPPLY_DROP.get(), level, this.lootTable,
            level.getRandom().nextLong(),
            blockPos.getX(), blockPos.getY() + SPAWN_HEIGHT_OFFSET, blockPos.getZ());
    level.addFreshEntity(supplyDrop);
    itemStack.shrink(1);
    return InteractionResult.SUCCESS;
  }

  public static class Properties extends Item.Properties {

    private ResourceLocation lootTable;

    public ResourceLocation getLootTable() {
      return lootTable;
    }

    public Properties setLootTable(ResourceLocation lootTable) {
      this.lootTable = lootTable;
      return this;
    }
  }
}
