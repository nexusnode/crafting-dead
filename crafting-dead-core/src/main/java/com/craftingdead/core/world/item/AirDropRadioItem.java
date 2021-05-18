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

package com.craftingdead.core.world.item;

import com.craftingdead.core.world.entity.ModEntityTypes;
import com.craftingdead.core.world.entity.SupplyDropEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AirDropRadioItem extends Item {

  private final ResourceLocation lootTable;

  public AirDropRadioItem(AirDropRadioItem.Properties properties) {
    super(properties);
    this.lootTable = properties.lootTable;
  }

  @Override
  public ActionResultType useOn(ItemUseContext context) {
    World world = context.getLevel();
    BlockPos blockPos = context.getClickedPos();
    ItemStack itemStack = context.getItemInHand();
    SupplyDropEntity airDropEntity =
        new SupplyDropEntity(ModEntityTypes.SUPPLY_DROP.get(), world, this.lootTable,
            random.nextLong(),
            blockPos.getX(), blockPos.getY() + 25.0D, blockPos.getZ());
    world.addFreshEntity(airDropEntity);
    itemStack.shrink(1);
    return ActionResultType.SUCCESS;
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
