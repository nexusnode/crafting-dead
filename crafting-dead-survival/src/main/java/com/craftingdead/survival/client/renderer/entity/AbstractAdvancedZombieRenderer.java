/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.survival.client.renderer.entity;

import com.craftingdead.core.client.renderer.entity.layers.ClothingLayer;
import com.craftingdead.core.client.renderer.entity.layers.EquipmentLayer;
import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.client.model.AdvancedZombieModel;
import com.craftingdead.survival.world.entity.monster.AdvancedZombie;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractAdvancedZombieRenderer<T extends AdvancedZombie, M extends AdvancedZombieModel<T>>
    extends HumanoidMobRenderer<T, M> {

  public AbstractAdvancedZombieRenderer(EntityRendererProvider.Context context, M model,
      float scale) {
    super(context, model, scale);
    this.addLayer(new ClothingLayer<>(this));
    this.addLayer(EquipmentLayer.builder(this)
        .slot(ModEquipmentSlot.MELEE)
        .useCrouchOrientation(true)
        .build());
    this.addLayer(EquipmentLayer.builder(this)
        .slot(ModEquipmentSlot.VEST)
        .useCrouchOrientation(true)
        .build());
    this.addLayer(EquipmentLayer.builder(this)
        .slot(ModEquipmentSlot.HAT)
        .useHeadOrientation(true)
        .transformation(poseStack -> poseStack.scale(-1F, -1F, 1F))
        .build());
    this.addLayer(EquipmentLayer.builder(this)
        .slot(ModEquipmentSlot.GUN)
        .useCrouchOrientation(true)
        .build());
  }

  @Override
  public ResourceLocation getTextureLocation(AdvancedZombie entity) {
    return new ResourceLocation(CraftingDeadSurvival.ID, "textures/entity/zombie/zombie"
        + entity.getTextureNumber() + ".png");
  }
}
