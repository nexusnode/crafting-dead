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
