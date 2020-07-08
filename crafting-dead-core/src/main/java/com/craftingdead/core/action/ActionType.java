package com.craftingdead.core.action;

import javax.annotation.Nullable;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ActionType<T extends IAction> extends ForgeRegistryEntry<ActionType<?>> {

  private final IFactory<T> factory;
  private final boolean triggeredByClient;

  public ActionType(IFactory<T> factory, boolean triggeredByClient) {
    this.factory = factory;
    this.triggeredByClient = triggeredByClient;
  }

  public T createAction(ILiving<?> performer, @Nullable ILiving<?> target) {
    return this.factory.create(this, performer, target);
  }

  public boolean isTriggeredByClient() {
    return this.triggeredByClient;
  }

  public interface IFactory<T extends IAction> {
    T create(ActionType<T> actionType, ILiving<?> performer, @Nullable ILiving<?> target);
  }
}
