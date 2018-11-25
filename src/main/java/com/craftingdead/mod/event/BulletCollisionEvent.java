package com.craftingdead.mod.event;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BulletCollisionEvent extends Event {

	private final Entity entity;
	private final ItemStack itemStack;
	private final RayTraceResult rayTrace;

	public BulletCollisionEvent(Entity entity, ItemStack itemStack, RayTraceResult rayTrace) {
		this.entity = entity;
		this.itemStack = itemStack;
		this.rayTrace = rayTrace;
	}

	@Cancelable
	public static class PrimeTNTEvent extends BulletCollisionEvent {

		public PrimeTNTEvent(Entity entity, ItemStack itemStack, RayTraceResult rayTrace) {
			super(entity, itemStack, rayTrace);
		}

	}

	public Entity getEntity() {
		return entity;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public RayTraceResult getRayTrace() {
		return this.rayTrace;
	}

}
