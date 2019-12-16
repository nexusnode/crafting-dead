package com.craftingdead.mod.capability;

import java.util.concurrent.Callable;
import com.craftingdead.mod.capability.action.DefaultAction;
import com.craftingdead.mod.capability.action.IAction;
import com.craftingdead.mod.capability.aimable.IAimable;
import com.craftingdead.mod.capability.player.DefaultPlayer;
import com.craftingdead.mod.capability.player.IPlayer;
import com.craftingdead.mod.capability.triggerable.DefaultTriggerable;
import com.craftingdead.mod.capability.triggerable.ITriggerable;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ModCapabilities {

  @CapabilityInject(IPlayer.class)
  public static final Capability<IPlayer<?>> PLAYER = null;

  @CapabilityInject(ITriggerable.class)
  public static final Capability<ITriggerable> TRIGGERABLE = null;

  @CapabilityInject(IAimable.class)
  public static final Capability<IAimable> AIMABLE = null;

  @CapabilityInject(IAction.class)
  public static final Capability<IAction> ACTION = null;

  public static void registerCapabilities() {
    CapabilityManager.INSTANCE.register(IPlayer.class, new EmptyStorage<>(), DefaultPlayer::new);
    CapabilityManager.INSTANCE
        .register(ITriggerable.class, new EmptyStorage<>(), DefaultTriggerable::new);
    CapabilityManager.INSTANCE
        .register(IAimable.class, new EmptyStorage<>(), (Callable<IAimable>) () -> () -> 1.0F);
    CapabilityManager.INSTANCE.register(IAction.class, new EmptyStorage<>(), DefaultAction::new);
  }

  private static class EmptyStorage<C> implements Capability.IStorage<C> {

    @Override
    public INBT writeNBT(Capability<C> capability, C instance, Direction side) {
      return null;
    }

    @Override
    public void readNBT(Capability<C> capability, C instance, Direction side, INBT nbt) {
      ;
    }
  }
}
