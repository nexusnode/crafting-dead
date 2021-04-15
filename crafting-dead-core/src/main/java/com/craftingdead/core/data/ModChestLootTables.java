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

package com.craftingdead.core.data;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.tag.ModItemTags;
import com.craftingdead.core.world.storage.loot.ModLootTables;
import net.minecraft.loot.BinomialRange;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.TagLootEntry;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;

public class ModChestLootTables
    implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {

  @Override
  public void accept(BiConsumer<ResourceLocation, LootTable.Builder> t) {
    t.accept(ModLootTables.MEDICAL_SUPPLY_DROP, new LootTable.Builder()
        .withPool(LootPool.lootPool()
            .setRolls(ConstantRange.exactly(10))
            .add(ItemLootEntry.lootTableItem(ModItems.BANDAGE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(4, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.CLEAN_RAG.get())
                .apply(SetCount.setCount(BinomialRange.binomial(4, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.FIRST_AID_KIT.get())
                .apply(SetCount.setCount(BinomialRange.binomial(6, 0.12F))))
            .add(ItemLootEntry.lootTableItem(ModItems.ADRENALINE_SYRINGE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(4, 0.5F))))
            .add(TagLootEntry.expandTag(ModItemTags.SYRINGES)
                .apply(SetCount.setCount(BinomialRange.binomial(4, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.SPLINT.get())
                .apply(SetCount.setCount(BinomialRange.binomial(4, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.ARMY_MEDIC_CLOTHING.get())
                .apply(SetCount.setCount(BinomialRange.binomial(2, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.M9_MAGAZINE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(3, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.M1911_MAGAZINE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(3, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.G18_MAGAZINE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(3, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.PIPE_GRENADE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(3, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.SMOKE_GRENADE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(3, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.ADRENALINE_SYRINGE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(2, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.M1911.get())
                .apply(SetCount.setCount(BinomialRange.binomial(4, 0.4F))))
            .add(ItemLootEntry.lootTableItem(ModItems.G18.get())
                .apply(SetCount.setCount(BinomialRange.binomial(2, 0.3F))))
            .add(ItemLootEntry.lootTableItem(ModItems.M9.get())
                .apply(SetCount.setCount(BinomialRange.binomial(2, 0.3F))))
            .add(ItemLootEntry.lootTableItem(ModItems.AK47_30_ROUND_MAGAZINE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(2, 0.45F))))));

    t.accept(ModLootTables.MILITARY_SUPPLY_DROP, new LootTable.Builder()
        .withPool(LootPool.lootPool()
            .setRolls(ConstantRange.exactly(10))
            .add(ItemLootEntry.lootTableItem(ModItems.BANDAGE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(4, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.BOLT_CUTTERS.get())
                .apply(SetCount.setCount(BinomialRange.binomial(2, 0.7F))))
            .add(ItemLootEntry.lootTableItem(ModItems.C4.get())
                .apply(SetCount.setCount(BinomialRange.binomial(6, 0.2F))))
            .add(ItemLootEntry.lootTableItem(ModItems.REMOTE_DETONATOR.get())
                .apply(SetCount.setCount(BinomialRange.binomial(1, 0.9F))))
            .add(ItemLootEntry.lootTableItem(ModItems.STANAG_30_ROUND_MAGAZINE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(5, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.M107_MAGAZINE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(6, 0.4F))))
            .add(ItemLootEntry.lootTableItem(ModItems.M9_MAGAZINE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(5, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.G18_MAGAZINE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(5, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.M240B_MAGAZINE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(2, 0.4F))))
            .add(ItemLootEntry.lootTableItem(ModItems.RPK_DRUM_MAGAZINE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(2, 0.4F))))
            .add(ItemLootEntry.lootTableItem(ModItems.MINIGUN_MAGAZINE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(2, 0.6F))))
            .add(ItemLootEntry.lootTableItem(ModItems.MPT55_MAGAZINE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(3, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.FRAG_GRENADE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(3, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.FIRE_GRENADE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(3, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.FLASH_GRENADE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(2, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.ACOG_SIGHT.get())
                .apply(SetCount.setCount(BinomialRange.binomial(4, 0.4F))))
            .add(ItemLootEntry.lootTableItem(ModItems.SUPPRESSOR.get())
                .apply(SetCount.setCount(BinomialRange.binomial(3, 0.3F))))
            .add(ItemLootEntry.lootTableItem(ModItems.HP_SCOPE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(3, 0.2F))))
            .add(ItemLootEntry.lootTableItem(ModItems.ARMY_CLOTHING.get())
                .apply(SetCount.setCount(BinomialRange.binomial(3, 0.4F))))
            .add(ItemLootEntry.lootTableItem(ModItems.TAC_GHILLIE_CLOTHING.get())
                .apply(SetCount.setCount(BinomialRange.binomial(4, 0.3F))))
            .add(ItemLootEntry.lootTableItem(ModItems.SPACE_SUIT_CLOTHING.get())
                .apply(SetCount.setCount(BinomialRange.binomial(3, 0.2F))))
            .add(ItemLootEntry.lootTableItem(ModItems.JUGGERNAUT_CLOTHING.get())
                .apply(SetCount.setCount(BinomialRange.binomial(2, 0.8F))))
            .add(ItemLootEntry.lootTableItem(ModItems.COMBAT_BDU_CLOTHING.get())
                .apply(SetCount.setCount(BinomialRange.binomial(2, 0.8F))))
            .add(ItemLootEntry.lootTableItem(ModItems.CLONE_CLOTHING.get())
                .apply(SetCount.setCount(BinomialRange.binomial(1, 0.6F))))
            .add(ItemLootEntry.lootTableItem(ModItems.CONTRACTOR_CLOTHING.get())
                .apply(SetCount.setCount(BinomialRange.binomial(2, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.AK47.get())
                .apply(SetCount.setCount(BinomialRange.binomial(3, 0.45F))))
            .add(ItemLootEntry.lootTableItem(ModItems.M4A1.get())
                .apply(SetCount.setCount(BinomialRange.binomial(3, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.M107.get())
                .apply(SetCount.setCount(BinomialRange.binomial(3, 0.2F))))
            .add(ItemLootEntry.lootTableItem(ModItems.M1911.get())
                .apply(SetCount.setCount(BinomialRange.binomial(6, 0.4F))))
            .add(ItemLootEntry.lootTableItem(ModItems.G18.get())
                .apply(SetCount.setCount(BinomialRange.binomial(5, 0.3F))))
            .add(ItemLootEntry.lootTableItem(ModItems.M9.get())
                .apply(SetCount.setCount(BinomialRange.binomial(5, 0.3F))))
            .add(ItemLootEntry.lootTableItem(ModItems.P250.get())
                .apply(SetCount.setCount(BinomialRange.binomial(5, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.AS50.get())
                .apply(SetCount.setCount(BinomialRange.binomial(1, 0.7F))))
            .add(ItemLootEntry.lootTableItem(ModItems.M240B.get())
                .apply(SetCount.setCount(BinomialRange.binomial(1, 0.9F))))
            .add(ItemLootEntry.lootTableItem(ModItems.MINIGUN.get())
                .apply(SetCount.setCount(BinomialRange.binomial(1, 0.2F))))
            .add(ItemLootEntry.lootTableItem(ModItems.MP5A5.get())
                .apply(SetCount.setCount(BinomialRange.binomial(3, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.MPT55.get())
                .apply(SetCount.setCount(BinomialRange.binomial(2, 0.3F))))
            .add(ItemLootEntry.lootTableItem(ModItems.FURY_PAINT.get())
                .apply(SetCount.setCount(BinomialRange.binomial(2, 0.5F))))
            .add(ItemLootEntry.lootTableItem(ModItems.SCORCHED_PAINT.get())
                .apply(SetCount.setCount(BinomialRange.binomial(2, 0.4F))))
            .add(ItemLootEntry.lootTableItem(ModItems.INFERNO_PAINT.get())
                .apply(SetCount.setCount(BinomialRange.binomial(2, 0.4F))))
            .add(ItemLootEntry.lootTableItem(ModItems.AK47_30_ROUND_MAGAZINE.get())
                .apply(SetCount.setCount(BinomialRange.binomial(2, 0.45F))))
            .add(ItemLootEntry.lootTableItem(ModItems.MOSSBERG.get())
                .apply(SetCount.setCount(BinomialRange.binomial(1, 0.45F))))));
  }
}
