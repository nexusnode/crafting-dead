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

import com.craftingdead.survival.world.entity.SurvivalEntityTypes;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

public class SurvivalEntityLoot extends EntityLoot {

  @Override
  protected void addTables() {
    var zombieLoot = LootTable.lootTable()
        .withPool(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1.0F))
            .add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                .apply(LootingEnchantFunction
                    .lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
        .withPool(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1.0F))
            .add(LootItem.lootTableItem(Items.IRON_INGOT))
            .add(LootItem.lootTableItem(Items.CARROT))
            .add(LootItem.lootTableItem(Items.POTATO)
                .apply(SmeltItemFunction.smelted()
                    .when(LootItemEntityPropertyCondition
                        .hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))))
            .when(LootItemKilledByPlayerCondition.killedByPlayer())
            .when(LootItemRandomChanceWithLootingCondition
                .randomChanceAndLootingBoost(0.025F, 0.01F)));
    this.add(SurvivalEntityTypes.FAST_ZOMBIE.get(), zombieLoot);
    this.add(SurvivalEntityTypes.TANK_ZOMBIE.get(), zombieLoot);
    this.add(SurvivalEntityTypes.WEAK_ZOMBIE.get(), zombieLoot);
    this.add(SurvivalEntityTypes.POLICE_ZOMBIE.get(), zombieLoot);
    this.add(SurvivalEntityTypes.DOCTOR_ZOMBIE.get(), zombieLoot);
    this.add(SurvivalEntityTypes.GIANT_ZOMBIE.get(), zombieLoot);
  }

  @Override
  protected Iterable<EntityType<?>> getKnownEntities() {
    return SurvivalEntityTypes.deferredRegister.getEntries()
        .stream()
        .<EntityType<?>>map(RegistryObject::get)
        .toList();
  }
}
