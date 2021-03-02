package com.craftingdead.core.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.items.IItemHandler;

public class CollectMagazineItemHandlers extends Event {

  private final ILiving<?, ?> living;

  private final List<IItemHandler> itemHandlers = new ArrayList<>();

  public CollectMagazineItemHandlers(ILiving<?, ?> living) {
    this.living = living;
  }

  public ILiving<?, ?> getLiving() {
    return living;
  }

  public void addItemHandler(IItemHandler itemHandler) {
    this.itemHandlers.add(itemHandler);
  }

  public Collection<IItemHandler> getItemHandlers() {
    return Collections.unmodifiableCollection(this.itemHandlers);
  }
}
