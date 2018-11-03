package com.craftingdead.mod.common.item.trigger;

import com.craftingdead.mod.common.multiplayer.PlayerMP;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;

public interface TriggerHandler extends ITickable {

	void triggerDown(PlayerMP player, ItemStack itemStack);

	void triggerUp();

}
