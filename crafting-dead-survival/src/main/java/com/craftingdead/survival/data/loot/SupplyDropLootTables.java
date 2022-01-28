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

package com.craftingdead.survival.data.loot;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import com.craftingdead.core.tags.ModItemTags;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.survival.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class SupplyDropLootTables
    implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {

  @Override
  public void accept(BiConsumer<ResourceLocation, LootTable.Builder> t) {
    t.accept(BuiltInLootTables.MEDICAL_SUPPLY_DROP, new LootTable.Builder()
        .withPool(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(10))
            .add(LootItem.lootTableItem(ModItems.BANDAGE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(4, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.FIRST_AID_KIT.get())
                .apply(SetItemCountFunction
                    .setCount(BinomialDistributionGenerator.binomial(6, 0.12F))))
            .add(LootItem.lootTableItem(ModItems.ADRENALINE_SYRINGE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(4, 0.5F))))
            .add(TagEntry.expandTag(ModItemTags.SYRINGES)
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(4, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.ARMY_MEDIC_CLOTHING.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.M9_MAGAZINE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.M1911_MAGAZINE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.G18_MAGAZINE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.SMOKE_GRENADE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.ADRENALINE_SYRINGE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.M1911.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(4, 0.4F))))
            .add(LootItem.lootTableItem(ModItems.G18.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.3F))))
            .add(LootItem.lootTableItem(ModItems.M9.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.3F))))
            .add(LootItem.lootTableItem(ModItems.AK47_30_ROUND_MAGAZINE.get())
                .apply(SetItemCountFunction
                    .setCount(BinomialDistributionGenerator.binomial(2, 0.45F))))));

    t.accept(BuiltInLootTables.MILITARY_SUPPLY_DROP, new LootTable.Builder()
        .withPool(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(10))
            .add(LootItem.lootTableItem(ModItems.BANDAGE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(4, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.BOLT_CUTTERS.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.7F))))
            .add(LootItem.lootTableItem(ModItems.C4.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(6, 0.2F))))
            .add(LootItem.lootTableItem(ModItems.REMOTE_DETONATOR.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, 0.9F))))
            .add(LootItem.lootTableItem(ModItems.STANAG_30_ROUND_MAGAZINE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(5, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.M107_MAGAZINE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(6, 0.4F))))
            .add(LootItem.lootTableItem(ModItems.M9_MAGAZINE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(5, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.G18_MAGAZINE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(5, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.M240B_MAGAZINE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.4F))))
            .add(LootItem.lootTableItem(ModItems.RPK_DRUM_MAGAZINE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.4F))))
            .add(LootItem.lootTableItem(ModItems.MINIGUN_MAGAZINE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.6F))))
            .add(LootItem.lootTableItem(ModItems.MPT55_MAGAZINE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.FRAG_GRENADE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.FIRE_GRENADE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.FLASH_GRENADE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.ACOG_SIGHT.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(4, 0.4F))))
            .add(LootItem.lootTableItem(ModItems.SUPPRESSOR.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.3F))))
            .add(LootItem.lootTableItem(ModItems.HP_SCOPE.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.2F))))
            .add(LootItem.lootTableItem(ModItems.ARMY_CLOTHING.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.4F))))
            .add(LootItem.lootTableItem(ModItems.TAC_GHILLIE_CLOTHING.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(4, 0.3F))))
            .add(LootItem.lootTableItem(ModItems.SPACE_SUIT_CLOTHING.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.2F))))
            .add(LootItem.lootTableItem(ModItems.JUGGERNAUT_CLOTHING.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.8F))))
            .add(LootItem.lootTableItem(ModItems.COMBAT_BDU_CLOTHING.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.8F))))
            .add(LootItem.lootTableItem(ModItems.CLONE_CLOTHING.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, 0.6F))))
            .add(LootItem.lootTableItem(ModItems.CONTRACTOR_CLOTHING.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.AK47.get())
                .apply(SetItemCountFunction
                    .setCount(BinomialDistributionGenerator.binomial(3, 0.45F))))
            .add(LootItem.lootTableItem(ModItems.M4A1.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.M107.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.2F))))
            .add(LootItem.lootTableItem(ModItems.M1911.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(6, 0.4F))))
            .add(LootItem.lootTableItem(ModItems.G18.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(5, 0.3F))))
            .add(LootItem.lootTableItem(ModItems.M9.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(5, 0.3F))))
            .add(LootItem.lootTableItem(ModItems.P250.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(5, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.AS50.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, 0.7F))))
            .add(LootItem.lootTableItem(ModItems.M240B.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, 0.9F))))
            .add(LootItem.lootTableItem(ModItems.MINIGUN.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, 0.2F))))
            .add(LootItem.lootTableItem(ModItems.MP5A5.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.MPT55.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.3F))))
            .add(LootItem.lootTableItem(ModItems.FURY_PAINT.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.5F))))
            .add(LootItem.lootTableItem(ModItems.SCORCHED_PAINT.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.4F))))
            .add(LootItem.lootTableItem(ModItems.INFERNO_PAINT.get())
                .apply(
                    SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.4F))))
            .add(LootItem.lootTableItem(ModItems.AK47_30_ROUND_MAGAZINE.get())
                .apply(SetItemCountFunction
                    .setCount(BinomialDistributionGenerator.binomial(2, 0.45F))))
            .add(LootItem.lootTableItem(ModItems.MOSSBERG.get())
                .apply(SetItemCountFunction
                    .setCount(BinomialDistributionGenerator.binomial(1, 0.45F))))));
  }
}
