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

package com.craftingdead.survival.world.item;

import com.craftingdead.survival.world.entity.SupplyDrop;
import com.craftingdead.survival.world.entity.SurvivalEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class SupplyDropRadioItem extends Item {

  private static final double SPAWN_HEIGHT_OFFSET = 25.0D;

  private final ResourceLocation lootTable;

  public SupplyDropRadioItem(SupplyDropRadioItem.Properties properties) {
    super(properties);
    this.lootTable = properties.lootTable;
  }

  private void spawnSupplyDrop(Level level, BlockPos pos, ItemStack itemStack) {
    var supplyDrop =
        new SupplyDrop(SurvivalEntityTypes.SUPPLY_DROP.get(), level, this.lootTable,
            level.getRandom().nextLong(),
            pos.getX(), pos.getY() + SPAWN_HEIGHT_OFFSET, pos.getZ());
    level.addFreshEntity(supplyDrop);
    itemStack.shrink(1);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    var level = context.getLevel();
    var blockPos = context.getClickedPos();
    var itemStack = context.getItemInHand();
    if (!level.isClientSide()) {
      this.spawnSupplyDrop(level, blockPos, itemStack);
    }
    return InteractionResult.sidedSuccess(level.isClientSide());
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    var itemStack = player.getItemInHand(hand);
    if (!level.isClientSide()) {
      this.spawnSupplyDrop(level, player.blockPosition(), itemStack);
    }
    return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
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
