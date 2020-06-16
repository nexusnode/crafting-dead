package com.craftingdead.core.event;

import java.util.Optional;
import com.craftingdead.core.item.GunItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class GunEvent extends Event {

  private final ItemStack itemStack;
  private final Entity entity;

  public GunEvent(ItemStack itemStack, Entity entity) {
    this.itemStack = itemStack;
    this.entity = entity;
  }

  public ItemStack getItemStack() {
    return this.itemStack;
  }

  public GunItem getGunItem() {
    return (GunItem) this.itemStack.getItem();
  }

  public Entity getEntity() {
    return this.entity;
  }

  public static class ShootEvent extends GunEvent {

    private final Optional<? extends RayTraceResult> rayTrace;

    public ShootEvent(ItemStack itemStack, Entity entity,
        Optional<? extends RayTraceResult> rayTrace) {
      super(itemStack, entity);
      this.rayTrace = rayTrace;
    }

    public Optional<? extends RayTraceResult> getRayTrace() {
      return rayTrace;
    }

    @Cancelable
    public static class Pre extends ShootEvent {

      public Pre(ItemStack itemStack, Entity entity, Optional<? extends RayTraceResult> rayTrace) {
        super(itemStack, entity, rayTrace);
      }
    }

    public static class Post extends ShootEvent {

      public Post(ItemStack itemStack, Entity entity, Optional<? extends RayTraceResult> rayTrace) {
        super(itemStack, entity, rayTrace);
      }
    }
  }
}
