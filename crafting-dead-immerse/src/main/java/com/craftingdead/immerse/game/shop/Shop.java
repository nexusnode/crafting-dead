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

package com.craftingdead.immerse.game.shop;

import java.util.List;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.item.ModItems;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

public interface Shop {

  int getPlayerMoney(PlayerExtension<?> player);

  void buyItem(PlayerExtension<?> player, ItemStack itemStack);

  int getBuyTimeSeconds(PlayerExtension<?> player);

  default boolean canAfford(PlayerExtension<?> player, int amount) {
    return this.getPlayerMoney(player) >= amount;
  }

  default List<ShopCategory> getCategories(PlayerExtension<?> player) {
    ImmutableList.Builder<ShopCategory> builder = ImmutableList.builder();

    // TODO Temp for testing

    builder.add(new SimpleShopCategory(
        new StringTextComponent("Rifle"),
        new StringTextComponent("Assault rifle selections."),
        ImmutableList.of(
            new ShopItem(ModItems.M4A1.get()),
            new ShopItem(ModItems.AK47.get()),
            new ShopItem(ModItems.FNFAL.get()),
            new ShopItem(ModItems.ACR.get()))));

    builder.add(new SimpleShopCategory(
        new StringTextComponent("SMG"),
        new StringTextComponent("Sub-machine gun selections."),
        ImmutableList.of(
            new ShopItem(ModItems.MAC10.get()),
            new ShopItem(ModItems.P90.get()),
            new ShopItem(ModItems.VECTOR.get()))));

    builder.add(new SimpleShopCategory(
        new StringTextComponent("Heavy"),
        new StringTextComponent("Heavy-based gun selections."),
        ImmutableList.of(
            new ShopItem(ModItems.MOSSBERG.get()),
            new ShopItem(ModItems.M240B.get()),
            new ShopItem(ModItems.M1GARAND.get()))));

    builder.add(new SimpleShopCategory(
        new StringTextComponent("Sniper"),
        new StringTextComponent("Sniper rifle selections."),
        ImmutableList.of(
            new ShopItem(ModItems.M107.get()),
            new ShopItem(ModItems.AS50.get()),
            new ShopItem(ModItems.AWP.get()))));

    builder.add(new SimpleShopCategory(
        new StringTextComponent("Pistol"),
        new StringTextComponent("Side arm and pistol selections."),
        ImmutableList.of(
            new ShopItem(ModItems.M1911.get()),
            new ShopItem(ModItems.G18.get()),
            new ShopItem(ModItems.DESERT_EAGLE.get()),
            new ShopItem(ModItems.P250.get()),
            new ShopItem(ModItems.FN57.get()))));

    builder.add(new SimpleShopCategory(
        new StringTextComponent("Grenades"),
        new StringTextComponent("Utilities and grenades."),
        ImmutableList.of(
            new ShopItem(ModItems.FLASH_GRENADE.get()),
            new ShopItem(ModItems.DECOY_GRENADE.get()),
            new ShopItem(ModItems.SMOKE_GRENADE.get()),
            new ShopItem(ModItems.FIRE_GRENADE.get()),
            new ShopItem(ModItems.FRAG_GRENADE.get()))));

    return builder.build();
  }
}
