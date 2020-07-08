package com.craftingdead.core.capability.actionprovider;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.core.action.ActionType;
import com.craftingdead.core.action.IAction;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.util.math.BlockPos;

public class DefaultActionProvider implements IActionProvider {

  @Nullable
  private final BiFunction<ILiving<?>, BlockPos, IAction> blockFactory;
  @Nullable
  private final BiFunction<ILiving<?>, ILiving<?>, IAction> entityFactory;

  public DefaultActionProvider() {
    this(null, null);
  }

  public DefaultActionProvider(Supplier<? extends ActionType<?>> actionType) {
    this(null, (performer, target) -> actionType.get().createAction(performer, target));
  }

  public DefaultActionProvider(@Nullable BiFunction<ILiving<?>, BlockPos, IAction> blockFactory,
      @Nullable BiFunction<ILiving<?>, ILiving<?>, IAction> entityFactory) {
    this.blockFactory = blockFactory;
    this.entityFactory = entityFactory;
  }

  @Override
  public Optional<IAction> getBlockAction(ILiving<?> performer, BlockPos blockPos) {
    return this.blockFactory == null ? Optional.empty()
        : Optional.of(this.blockFactory.apply(performer, blockPos));
  }

  @Override
  public Optional<IAction> getEntityAction(ILiving<?> performer, ILiving<?> target) {
    return this.entityFactory == null ? Optional.empty()
        : Optional.of(this.entityFactory.apply(performer, target));
  }
}
