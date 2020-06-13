package com.craftingdead.mod.capability;

import com.craftingdead.mod.capability.action.DefaultAction;
import com.craftingdead.mod.capability.action.IAction;
import com.craftingdead.mod.capability.animation.DefaultAnimationController;
import com.craftingdead.mod.capability.animation.IAnimationController;
import com.craftingdead.mod.capability.gun.DefaultGun;
import com.craftingdead.mod.capability.gun.IGun;
import com.craftingdead.mod.capability.living.DefaultLiving;
import com.craftingdead.mod.capability.living.ILiving;
import com.craftingdead.mod.capability.magazine.DefaultMagazine;
import com.craftingdead.mod.capability.magazine.IMagazine;
import com.craftingdead.mod.capability.paint.DefaultPaint;
import com.craftingdead.mod.capability.paint.IPaint;
import com.craftingdead.mod.capability.scope.DefaultScope;
import com.craftingdead.mod.capability.scope.IScope;
import com.craftingdead.mod.capability.storage.DefaultStorage;
import com.craftingdead.mod.capability.storage.IStorage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ModCapabilities {

  @CapabilityInject(ILiving.class)
  public static final Capability<ILiving<? extends LivingEntity>> LIVING = null;

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

  @CapabilityInject(IStorage.class)
  public static final Capability<IStorage> STORAGE = null;

  @CapabilityInject(IScope.class)
  public static final Capability<IScope> SCOPE = null;

  public static void registerCapabilities() {
    CapabilityManager.INSTANCE.register(ILiving.class, new EmptyStorage<>(), DefaultLiving::new);
    CapabilityManager.INSTANCE
        .register(IAnimationController.class, new EmptyStorage<>(),
            DefaultAnimationController::new);
    CapabilityManager.INSTANCE.register(IGun.class, new EmptyStorage<>(), DefaultGun::new);
    CapabilityManager.INSTANCE.register(IAction.class, new EmptyStorage<>(), DefaultAction::new);
    CapabilityManager.INSTANCE.register(IPaint.class, new EmptyStorage<>(), DefaultPaint::new);
    CapabilityManager.INSTANCE
        .register(IMagazine.class, new EmptyStorage<>(), DefaultMagazine::new);
    CapabilityManager.INSTANCE.register(IStorage.class, new EmptyStorage<>(), DefaultStorage::new);
    CapabilityManager.INSTANCE.register(IScope.class, new EmptyStorage<>(), DefaultScope::new);
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
