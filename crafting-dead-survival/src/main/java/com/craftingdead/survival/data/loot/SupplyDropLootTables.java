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
