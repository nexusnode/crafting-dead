package com.craftingdead.core.capability.actionprovider;

import java.util.Optional;
import javax.annotation.Nullable;
import com.craftingdead.core.action.IAction;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.util.math.BlockPos;

public interface IActionProvider {

  Optional<IAction> getBlockAction(ILiving<?> performer, BlockPos blockPos);

  Optional<IAction> getEntityAction(ILiving<?> performer, @Nullable ILiving<?> target);
}
