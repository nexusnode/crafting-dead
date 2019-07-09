package com.craftingdead.mod.event;

import java.util.Optional;
import com.craftingdead.mod.capability.triggerable.GunController;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Getter
@RequiredArgsConstructor
public class GunEvent extends Event {

  private final GunController controller;
  private final Entity entity;
  private final ItemStack itemStack;

  @Getter
  public static class ShootEvent extends GunEvent {

    private final Optional<? extends RayTraceResult> rayTrace;

    public ShootEvent(GunController controller, Entity entity, ItemStack itemStack,
        Optional<? extends RayTraceResult> rayTrace) {
      super(controller, entity, itemStack);
      this.rayTrace = rayTrace;
    }

    @Cancelable
    public static class Pre extends ShootEvent {

      public Pre(GunController controller, Entity entity, ItemStack itemStack,
          Optional<? extends RayTraceResult> rayTrace) {
        super(controller, entity, itemStack, rayTrace);
      }
    }

    public static class Post extends ShootEvent {

      public Post(GunController controller, Entity entity, ItemStack itemStack,
          Optional<? extends RayTraceResult> rayTrace) {
        super(controller, entity, itemStack, rayTrace);
      }
    }
  }
}
