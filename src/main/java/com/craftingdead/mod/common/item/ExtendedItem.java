package com.craftingdead.mod.common.item;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.craftingdead.mod.common.item.trigger.TriggerHandler;

import net.minecraft.item.Item;

public class ExtendedItem extends Item {

	@Nullable
	public Supplier<? extends TriggerHandler> getTriggerHandlerSupplier() {
		return null;
	}

}
