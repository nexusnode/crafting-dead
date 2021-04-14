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
import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.living.IPlayer;
import com.craftingdead.core.util.Text;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;

public interface IShop {

  int getPlayerMoney(IPlayer<?> player);

  void buyItem(IPlayer<?> player, ItemStack itemStack);

  int getBuyTimeSeconds(IPlayer<?> player);

  default boolean canAfford(IPlayer<?> player, int amount) {
    return this.getPlayerMoney(player) >= amount;
  }

  default List<IShopCategory> getCategories(IPlayer<?> player) {
    ImmutableList.Builder<IShopCategory> builder = ImmutableList.builder();

    // @formatter:off
    builder.add(new SimpleShopCategory(
        Text.of("Rifle"),
        Text.of("Assault rifle selections."),
        ImmutableList.of(
            new ShopItem(ModItems.M4A1.get()),
            new ShopItem(ModItems.AK47.get()),
            new ShopItem(ModItems.FNFAL.get()),
            new ShopItem(ModItems.ACR.get()))));

    builder.add(new SimpleShopCategory(
        Text.of("SMG"),
        Text.of("Sub-machine gun selections."),
        ImmutableList.of(
            new ShopItem(ModItems.MAC10.get()),
            new ShopItem(ModItems.P90.get()),
            new ShopItem(ModItems.VECTOR.get()))));

    builder.add(new SimpleShopCategory(
        Text.of("Heavy"),
        Text.of("Heavy-based gun selections."),
        ImmutableList.of(
            new ShopItem(ModItems.MOSSBERG.get()),
            new ShopItem(ModItems.M240B.get()),
            new ShopItem(ModItems.M1GARAND.get()))));

    builder.add(new SimpleShopCategory(
        Text.of("Sniper"),
        Text.of("Sniper rifle selections."),
        ImmutableList.of(
            new ShopItem(ModItems.M107.get()),
            new ShopItem(ModItems.AS50.get()),
            new ShopItem(ModItems.AWP.get()))));

    builder.add(new SimpleShopCategory(
        Text.of("Pistol"),
        Text.of("Side arm and pistol selections."),
        ImmutableList.of(
            new ShopItem(ModItems.M1911.get()),
            new ShopItem(ModItems.G18.get()),
            new ShopItem(ModItems.DESERT_EAGLE.get()),
            new ShopItem(ModItems.P250.get()),
            new ShopItem(ModItems.FN57.get()))));

    builder.add(new SimpleShopCategory(
        Text.of("Grenades"),
        Text.of("Utilities and grenades."),
        ImmutableList.of(
            new ShopItem(ModItems.FLASH_GRENADE.get()),
            new ShopItem(ModItems.DECOY_GRENADE.get()),
            new ShopItem(ModItems.SMOKE_GRENADE.get()),
            new ShopItem(ModItems.FIRE_GRENADE.get()),
            new ShopItem(ModItems.FRAG_GRENADE.get()))));
    // @formatter:on

    return builder.build();
  }
}
