/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core.client.renderer.entity;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.renderer.entity.layer.ClothingLayer;
import com.craftingdead.core.client.renderer.entity.layer.EquipmentLayer;
import com.craftingdead.core.client.renderer.entity.model.AdvancedZombieModel;
import com.craftingdead.core.entity.monster.AdvancedZombieEntity;
import com.craftingdead.core.inventory.InventorySlotType;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractAdvancedZombieRenderer<T extends AdvancedZombieEntity, M extends AdvancedZombieModel<T>>
    extends BipedRenderer<T, M> {

  public AbstractAdvancedZombieRenderer(EntityRendererManager renderManager, M model, float scale) {
    super(renderManager, model, scale);
    this.addLayer(new ClothingLayer<>(this));
    this
        .addLayer(new EquipmentLayer.Builder<T, M>()
            .withRenderer(this)
            .withSlot(InventorySlotType.MELEE)
            .withCrouchingOrientation(true)
            .build());
    this
        .addLayer(new EquipmentLayer.Builder<T, M>()
            .withRenderer(this)
            .withSlot(InventorySlotType.VEST)
            .withCrouchingOrientation(true)
            .build());
    this
        .addLayer(new EquipmentLayer.Builder<T, M>()
            .withRenderer(this)
            .withSlot(InventorySlotType.HAT)
            .withHeadOrientation(true)
            // Inverts X and Y rotation. This is from Mojang, based on HeadLayer.class.
            // TODO Find a reason to not remove this line. Also, if you remove it, you will
            // need to change the json file of every helmet since the scale affects positions.
            .withArbitraryTransformation(matrix -> matrix.scale(-1F, -1F, 1F))
            .build());
    this
        .addLayer(new EquipmentLayer.Builder<T, M>()
            .withRenderer(this)
            .withSlot(InventorySlotType.GUN)
            .withCrouchingOrientation(true)
            .build());
  }

  @Override
  public ResourceLocation getEntityTexture(AdvancedZombieEntity entity) {
    ResourceLocation texture = new ResourceLocation(CraftingDead.ID, "textures/entity/zombie/zombie"
        + ((AdvancedZombieEntity) entity).getTextureNumber() + ".png");
    return texture;
  }
}
