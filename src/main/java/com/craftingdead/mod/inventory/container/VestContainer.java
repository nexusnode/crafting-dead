package com.craftingdead.mod.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

public class VestContainer extends Container {

  /*
   * TODO 
   *    VestContainer
   *    BackpackContainer
   * 
   */
  protected VestContainer(ContainerType<?> p_i50105_1_, int p_i50105_2_) {
    super(p_i50105_1_, p_i50105_2_);
  }

  @Override
  public boolean canInteractWith(PlayerEntity p_75145_1_) {
    return false;
  }

}
