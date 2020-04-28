package com.craftingdead.mod.capability;

import com.craftingdead.mod.capability.action.DefaultAction;
import com.craftingdead.mod.capability.action.IAction;
import com.craftingdead.mod.capability.animation.DefaultAnimationController;
import com.craftingdead.mod.capability.animation.IAnimationController;
import com.craftingdead.mod.capability.gun.DefaultGunController;
import com.craftingdead.mod.capability.gun.IGunController;
import com.craftingdead.mod.capability.paint.DefaultPaintColor;
import com.craftingdead.mod.capability.paint.IPaintColor;
import com.craftingdead.mod.capability.player.DefaultPlayer;
import com.craftingdead.mod.capability.player.IPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ModCapabilities {

  @CapabilityInject(IPlayer.class)
  public static final Capability<IPlayer<? extends PlayerEntity>> PLAYER = null;

  @CapabilityInject(IAnimationController.class)
  public static final Capability<IAnimationController> ANIMATION_CONTROLLER = null;

  @CapabilityInject(IGunController.class)
  public static final Capability<IGunController> GUN_CONTROLLER = null;

  @CapabilityInject(IAction.class)
  public static final Capability<IAction> ACTION = null;

  @CapabilityInject(IPaintColor.class)
  public static final Capability<IPaintColor> PAINT_COLOR = null;

  public static void registerCapabilities() {
    CapabilityManager.INSTANCE.register(IPlayer.class, new EmptyStorage<>(), DefaultPlayer::new);
    CapabilityManager.INSTANCE
        .register(IAnimationController.class, new EmptyStorage<>(),
            DefaultAnimationController::new);
    CapabilityManager.INSTANCE
        .register(IGunController.class, new EmptyStorage<>(), DefaultGunController::new);
    CapabilityManager.INSTANCE.register(IAction.class, new EmptyStorage<>(), DefaultAction::new);
    CapabilityManager.INSTANCE.register(IPaintColor.class, new EmptyStorage<>(), DefaultPaintColor::new);
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
