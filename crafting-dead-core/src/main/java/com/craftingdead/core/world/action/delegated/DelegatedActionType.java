package com.craftingdead.core.world.action.delegated;

import java.util.Optional;
import com.craftingdead.core.world.action.Action;

public interface DelegatedActionType {

  Optional<? extends DelegatedAction> create(Action action);
}
