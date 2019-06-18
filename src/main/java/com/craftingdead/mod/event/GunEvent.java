package com.craftingdead.mod.event;

import com.craftingdead.mod.item.GunItem;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class GunEvent extends Event {

	private final GunItem item;
	private final Entity entity;
	private final ItemStack itemStack;

	public GunEvent(GunItem item, Entity entity, ItemStack itemStack) {
		this.item = item;
		this.entity = entity;
		this.itemStack = itemStack;
	}

	public static class ShootEvent extends GunEvent {

		private final RayTraceResult rayTrace;

		public ShootEvent(GunItem item, Entity entity, ItemStack itemStack, RayTraceResult rayTrace) {
			super(item, entity, itemStack);
			this.rayTrace = rayTrace;

		}

		@Cancelable
		public static class Pre extends ShootEvent {

			public Pre(GunItem item, Entity entity, ItemStack itemStack, RayTraceResult rayTrace) {
				super(item, entity, itemStack, rayTrace);
			}
		}

		public static class Post extends ShootEvent {

			public Post(GunItem item, Entity entity, ItemStack itemStack, RayTraceResult rayTrace) {
				super(item, entity, itemStack, rayTrace);
			}
		}

		public RayTraceResult getRayTrace() {
			return this.rayTrace;
		}

	}

	public GunItem getItem() {
		return this.item;
	}

	public Entity getEntity() {
		return entity;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

}
