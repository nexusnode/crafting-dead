package com.craftingdead.core.world.action.delegated;

import java.util.Optional;
import java.util.function.Predicate;
import com.craftingdead.core.world.action.Action;
import net.minecraft.block.BlockState;

public final class DelegatedBlockActionType extends AbstractDelegatedActionType {

  private final Predicate<BlockState> predicate;

  private DelegatedBlockActionType(Builder builder) {
    super(builder);
    this.predicate = builder.predicate;
  }

  @Override
  public Optional<? extends DelegatedAction> create(Action action) {
    return Optional.of(new DelegatedBlockAction(this));
  }

  public Predicate<BlockState> getPredicate() {
    return this.predicate;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder extends AbstractDelegatedActionType.Builder<Builder> {

    private Predicate<BlockState> predicate;

    private Builder() {
      super(DelegatedBlockActionType::new);
    }

    public Builder setPredicate(Predicate<BlockState> predicate) {
      this.predicate = predicate;
      return this;
    }
  }
}
