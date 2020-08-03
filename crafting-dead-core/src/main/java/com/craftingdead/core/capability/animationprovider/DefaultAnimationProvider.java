package com.craftingdead.core.capability.animationprovider;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeCallable;

public class DefaultAnimationProvider<T extends IAnimationController>
    implements IAnimationProvider<T> {

  @Nullable
  private final Supplier<DistExecutor.SafeCallable<T>> factory;

  private T animationController;

  public DefaultAnimationProvider() {
    this(null);
  }

  public DefaultAnimationProvider(Supplier<SafeCallable<T>> factory) {
    this.factory = factory;
  }

  @Override
  public T getAnimationController() {
    return this.animationController == null
        ? this.animationController = DistExecutor.safeCallWhenOn(Dist.CLIENT, this.factory)
        : this.animationController;
  }
}
