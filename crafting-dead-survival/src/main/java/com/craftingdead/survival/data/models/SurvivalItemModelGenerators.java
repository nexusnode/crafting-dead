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

package com.craftingdead.survival.data.models;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import com.craftingdead.survival.world.item.SurvivalItems;
import com.google.gson.JsonElement;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class SurvivalItemModelGenerators {

  private final BiConsumer<ResourceLocation, Supplier<JsonElement>> output;

  public SurvivalItemModelGenerators(BiConsumer<ResourceLocation, Supplier<JsonElement>> output) {
    this.output = output;
  }

  private void generateFlatItem(Item item, ModelTemplate model) {
    model.create(ModelLocationUtils.getModelLocation(item),
        TextureMapping.layer0(item), this.output);
  }

  public void run() {
    this.generateFlatItem(SurvivalItems.EMPTY_WATER_BOTTLE.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.WATER_BOTTLE.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.EMPTY_WATER_CANTEEN.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.WATER_CANTEEN.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.POWER_BAR.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.CANDY_BAR.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.CEREAL.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.CANNED_SWEETCORN.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.OPEN_CANNED_SWEETCORN.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.CANNED_BEANS.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.OPEN_CANNED_BEANS.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.CANNED_TUNA.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.OPEN_CANNED_TUNA.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.CANNED_PEACHES.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.OPEN_CANNED_PEACHES.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.CANNED_PASTA.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.OPEN_CANNED_PASTA.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.CANNED_CORNED_BEEF.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.OPEN_CANNED_CORNED_BEEF.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.CANNED_CUSTARD.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.OPEN_CANNED_CUSTARD.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.CANNED_PICKLES.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.OPEN_CANNED_PICKLES.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.CANNED_DOG_FOOD.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.OPEN_CANNED_DOG_FOOD.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.CANNED_TOMATO_SOUP.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.OPEN_CANNED_TOMATO_SOUP.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.MRE.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.ORANGE.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.ROTTEN_ORANGE.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.PEAR.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.ROTTEN_PEAR.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.RICE_BAG.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.ROTTEN_APPLE.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.NOODLES.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.ROTTEN_MELON_SLICE.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.BLUEBERRY.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.ROTTEN_BLUEBERRY.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.RASPBERRY.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.ROTTEN_RASPBERRY.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.CHIPS.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.RANCH_CHIPS.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.CHEESY_CHIPS.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.SALTED_CHIPS.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.POPCORN.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.NUTTY_CEREAL.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.EMERALD_CEREAL.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.FLAKE_CEREAL.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.CAN_OPENER.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.SCREWDRIVER.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(SurvivalItems.MULTI_TOOL.get(), ModelTemplates.FLAT_ITEM);
  }
}
