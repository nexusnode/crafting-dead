package com.craftingdead.mod.client.model;

import net.minecraft.client.renderer.model.IBakedModel;

/**
 * A baked model that carries an equipped version of itself.
 * The equipped model can be used when the model owner (e.g. an ItemStack) is equipped in the player's body.
 */
public interface IEquipableModel extends IBakedModel {

  /**
   * Gets the equipped version of the model to be rendered in a player's body.
   */
  public IBakedModel getEquippedModel();

}
