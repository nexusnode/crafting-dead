package com.craftingdead.mod.common.item;

import com.craftingdead.mod.common.multiplayer.PlayerMP;

import net.minecraft.item.ItemStack;

public interface TriggerableItem {

	void handleTrigger(PlayerMP player, ItemStack itemStack, boolean triggerDown);

}
