package com.craftingdead.core.capability.living;

import java.util.Optional;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.inventory.InventorySlotType;
import net.minecraft.entity.player.PlayerEntity;

public interface IPlayer<E extends PlayerEntity>
    extends ILiving<E, IPlayerHandler>, IPlayerHandler {

  void openInventory();

  void openStorage(InventorySlotType slotType);

  void infect(float chance);

  int getWater();

  void setWater(int water);

  int getMaxWater();

  void setMaxWater(int maxWater);

  public static <E extends PlayerEntity> IPlayer<E> getExpected(E livingEntity) {
    return livingEntity.getCapability(ModCapabilities.LIVING)
        .filter(living -> living instanceof IPlayer)
        .<IPlayer<E>>cast()
        .orElseThrow(() -> new IllegalStateException("Missing living capability"));
  }

  public static <R extends IPlayer<E>, E extends PlayerEntity> Optional<R> getOptional(
      E livingEntity) {
    return livingEntity.getCapability(ModCapabilities.LIVING)
        .filter(living -> living instanceof IPlayer)
        .<R>cast()
        .map(Optional::of)
        .orElse(Optional.empty());
  }
}
