package com.craftingdead.mod.common.item;

import java.util.function.Supplier;

import com.craftingdead.mod.common.item.trigger.GunTriggerHandler;
import com.craftingdead.mod.common.item.trigger.TriggerHandler;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;

public abstract class ItemGun extends ExtendedItem {

	@SuppressWarnings("unchecked")
	public static final Predicate<Entity> GUN_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING,
			EntitySelectors.IS_ALIVE, Entity::canBeCollidedWith);

	public abstract boolean useBowAndArrowStance();

	public abstract Bullet createBullet(ItemStack itemStack);

	@Override
	public Supplier<? extends TriggerHandler> getTriggerHandlerSupplier() {
		return GunTriggerHandler::new;
	}

}
