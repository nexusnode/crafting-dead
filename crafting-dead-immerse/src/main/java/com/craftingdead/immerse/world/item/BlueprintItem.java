package com.craftingdead.immerse.world.item;

import java.util.function.Supplier;
import com.craftingdead.core.world.action.item.ItemActionType;
import com.craftingdead.core.world.item.ActionItem;

public class BlueprintItem extends ActionItem {

  public BlueprintItem(Supplier<? extends ItemActionType<?>> itemActionType,
      Properties properties) {
    super(itemActionType, properties);
  }

}
