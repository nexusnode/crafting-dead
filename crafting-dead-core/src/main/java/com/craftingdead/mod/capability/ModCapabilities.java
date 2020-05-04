package com.craftingdead.mod.capability;

import com.craftingdead.mod.capability.action.DefaultAction;
import com.craftingdead.mod.capability.action.IAction;
import com.craftingdead.mod.capability.animation.DefaultAnimationController;
import com.craftingdead.mod.capability.animation.IAnimationController;
import com.craftingdead.mod.capability.gun.DefaultGun;
import com.craftingdead.mod.capability.gun.IGun;
import com.craftingdead.mod.capability.magazine.DefaultMagazine;
import com.craftingdead.mod.capability.magazine.IMagazine;
import com.craftingdead.mod.capability.paint.DefaultPaint;
import com.craftingdead.mod.capability.paint.IPaint;
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

  @CapabilityInject(IGun.class)
  public static final Capability<IGun> GUN = null;

  @CapabilityInject(IAction.class)
  public static final Capability<IAction> ACTION = null;

  @CapabilityInject(IPaint.class)
  public static final Capability<IPaint> PAINT = null;

  @CapabilityInject(IMagazine.class)
  public static final Capability<IMagazine> MAGAZINE = null;

  public static void registerCapabilities() {
    CapabilityManager.INSTANCE.register(IPlayer.class, new EmptyStorage<>(), DefaultPlayer::new);
    CapabilityManager.INSTANCE
        .register(IAnimationController.class, new EmptyStorage<>(),
            DefaultAnimationController::new);
    CapabilityManager.INSTANCE.register(IGun.class, new EmptyStorage<>(), DefaultGun::new);
    CapabilityManager.INSTANCE.register(IAction.class, new EmptyStorage<>(), DefaultAction::new);
    CapabilityManager.INSTANCE
        .register(IPaint.class, new EmptyStorage<>(), () -> new DefaultPaint(null, null));
    CapabilityManager.INSTANCE
        .register(IMagazine.class, new EmptyStorage<>(), DefaultMagazine::new);
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
