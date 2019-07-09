package com.craftingdead.mod.capability;

import java.util.concurrent.Callable;
import com.craftingdead.mod.capability.crosshair.Aimable;
import com.craftingdead.mod.capability.player.DefaultPlayer;
import com.craftingdead.mod.capability.player.Player;
import com.craftingdead.mod.capability.triggerable.DefaultTriggerable;
import com.craftingdead.mod.capability.triggerable.Triggerable;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ModCapabilities {

  @CapabilityInject(Player.class)
  public static final Capability<Player<?>> PLAYER = null;

  @CapabilityInject(Triggerable.class)
  public static final Capability<Triggerable> TRIGGERABLE = null;

  @CapabilityInject(Aimable.class)
  public static final Capability<Aimable> AIMABLE = null;

  public static void registerCapabilities() {
    CapabilityManager.INSTANCE.register(Player.class, new EmptyStorage<>(), DefaultPlayer::new);
    CapabilityManager.INSTANCE.register(Triggerable.class, new EmptyStorage<>(),
        DefaultTriggerable::new);
    CapabilityManager.INSTANCE.register(Aimable.class, new EmptyStorage<>(),
        (Callable<Aimable>) () -> () -> 1.0F);
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
