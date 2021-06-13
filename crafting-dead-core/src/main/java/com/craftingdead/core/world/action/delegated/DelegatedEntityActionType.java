package com.craftingdead.core.world.action.delegated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.world.action.Action;
import com.craftingdead.core.world.action.TargetSelector;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.potion.EffectInstance;

public final class DelegatedEntityActionType extends AbstractDelegatedActionType {

  private final TargetSelector targetSelector;
  private final List<Pair<Supplier<EffectInstance>, Float>> effects;
  @Nullable
  private final Pair<Consumer<LivingExtension<?, ?>>, Float> customAction;
  private final BooleanSupplier condition;

  private DelegatedEntityActionType(Builder builder) {
    super(builder);
    this.targetSelector = builder.targetSelector;
    this.effects = builder.effects;
    this.customAction = builder.customAction;
    this.condition = builder.condition;
  }

  public TargetSelector getTargetSelector() {
    return this.targetSelector;
  }

  public List<Pair<Supplier<EffectInstance>, Float>> getEffects() {
    return this.effects;
  }

  public Pair<Consumer<LivingExtension<?, ?>>, Float> getCustomAction() {
    return this.customAction;
  }

  public BooleanSupplier getCondition() {
    return this.condition;
  }

  @Override
  public Optional<? extends DelegatedAction> create(Action action) {
    return this.targetSelector.select(action.getPerformer(), action.getTarget().orElse(null))
        .map(target -> new DelegatedEntityAction(this, target));
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder extends AbstractDelegatedActionType.Builder<Builder> {

    private TargetSelector targetSelector = TargetSelector.SELF_ONLY;
    private final List<Pair<Supplier<EffectInstance>, Float>> effects = new ArrayList<>();
    @Nullable
    private Pair<Consumer<LivingExtension<?, ?>>, Float> customAction;
    private BooleanSupplier condition = () -> true;

    private Builder() {
      super(DelegatedEntityActionType::new);
    }

    public Builder setTargetSelector(TargetSelector targetSelector) {
      this.targetSelector = targetSelector;
      return this;
    }

    public Builder addEffect(Supplier<EffectInstance> effect, Float chance) {
      this.effects.add(Pair.of(effect, chance));
      return this;
    }

    public Builder setCustomAction(Consumer<LivingExtension<?, ?>> customAction, Float chance) {
      this.customAction = Pair.of(customAction, chance);
      return this;
    }

    public Builder setCondition(BooleanSupplier condition) {
      this.condition = condition;
      return this;
    }
  }
}
