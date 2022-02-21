package com.craftingdead.immerse.world.action;

import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.world.item.context.BlockPlaceContext;

public class BuildBaseCenterAction extends BuildBlockAction {

  protected BuildBaseCenterAction(LivingExtension<?, ?> performer, BlockPlaceContext context,
      BuildBlockActionType type) {
    super(performer, context, type);
  }

}
